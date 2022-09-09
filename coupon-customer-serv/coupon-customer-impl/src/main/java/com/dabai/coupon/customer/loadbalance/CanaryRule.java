package com.dabai.coupon.customer.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.dabai.coupon.customer.constant.Constant.TRAFFIC_VERSION;

/**
 * 自定义负载均衡策略
 *  1.CanaryRule如何识别测试流量？
 *      如果WebClient发出一个请求，其Header的key-value列表中包含了特定的
 *      流量key：traffic-version，那么这个请求就被识别为测试流量，只能发送到特定的金丝雀服务器。
 *
 *  2.CanaryRule如何对测试流量进行负载均衡？
 *      包含了新的代码改动的服务器就是金丝雀，我们会在这台服务器的Nacos元数据中插入同样的
 *      流量key: traffic-version。如果Nacos元数据中的traffic-version值与测试流量的Header
 *      中的一样，那么这个Instance就是我们要找的那只金丝雀。
 *
 * @author
 * @create 2022-09-09 15:28
 */
@Slf4j
public class CanaryRule implements ReactorServiceInstanceLoadBalancer {

    private ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    /**
     * 服务名称，用于提示报错信息的
     */
    private String serviceId;
    /**
     * 定义一个轮询策略的种子，AtomicInteger确保自增操作线程安全
     */
    final AtomicInteger position;

    public CanaryRule(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider,
                      String serviceId) {
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.serviceId = serviceId;
        position = new AtomicInteger(new Random().nextInt(1000));
    }

    /**
     * 这个服务是LoadBalance的标准接口，也是负载均衡策略选择服务器的入口方法。
     */
    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        //从服务提供者中获取到当前request请求中的serviceInstances并且遍历
        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider
                .getIfAvailable(NoopServiceInstanceListSupplier::new);

        return supplier.get(request).next()
                .map(serviceInstances -> processInstanceResponse(supplier, serviceInstances, request));
    }

    /**
     * 本方法主要完成了对getInstanceResponse的调用
     * @param supplier
     * @param serviceInstances
     * @param request
     * @return
     */
    private Response<ServiceInstance> processInstanceResponse(
            ServiceInstanceListSupplier supplier,
            List<ServiceInstance> serviceInstances,
            Request request) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances, request);

        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }

        return serviceInstanceResponse;
    }

    /**
     * 根据金丝雀的规则返回目标节点
     * @param instances
     * @param request
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances, Request request) {
        //注册中心无可用实例，返回空
        if (CollectionUtils.isEmpty(instances)) {
            log.warn("No instance available {}", serviceId);
            return new EmptyResponse();
        }
        //从WebClient请求的Header中获取特定的流量打标值
        //注意：以下代码仅适用于WebClient调用，使用RestTemplate或者Feign则需要额外适配
        DefaultRequestContext context = (DefaultRequestContext) request.getContext();
        RequestData requestData = (RequestData) context.getClientRequest();
        HttpHeaders headers = requestData.getHeaders();
        //获取到headers中的流量标记
        String trafficVersion = headers.getFirst(TRAFFIC_VERSION);

        //如果没有找到打标标记，或者标记为空，则使用RoundRobin规则进行查找
        if (StringUtils.isBlank(trafficVersion)) {
            //过滤掉所有金丝雀测试的节点，即Nacos Metadaba中包含流量标记的节点 
            //从剩余的节点中进行RoundRobin查找
            List<ServiceInstance> noneCanaryInstances = instances.stream()
                    .filter(e -> !e.getMetadata().containsKey(TRAFFIC_VERSION))
                    .collect(Collectors.toList());
            return getRoundRobinInstance(noneCanaryInstances);
        }
        
        //找出所有的金丝雀服务器，使用RoundRobin算法挑选出一台
        List<ServiceInstance> canaryInstances  = instances.stream()
                .filter(e -> {
                    String trafficVersionInMetadata = e.getMetadata().get(TRAFFIC_VERSION);
                    return StringUtils.equalsIgnoreCase(trafficVersionInMetadata, trafficVersion);
                })
                .collect(Collectors.toList());

        return getRoundRobinInstance(canaryInstances);
    }

    /**
     * 使用RoundRobin机制获取节点
     * @param instances 服务实例集合
     * @return
     */
    private Response<ServiceInstance> getRoundRobinInstance(List<ServiceInstance> instances) {
        //如果没有可用节点，则返回空
        if (instances.isEmpty()) {
            log.warn("No servers available for service: " + serviceId);
            return new EmptyResponse();
        }
        //每一次计数器都自动+1，实现轮询的效果
        int pos = Math.abs(this.position.incrementAndGet());
        ServiceInstance instance = instances.get(pos % instances.size());
        return new DefaultResponse(instance);
    }
}
