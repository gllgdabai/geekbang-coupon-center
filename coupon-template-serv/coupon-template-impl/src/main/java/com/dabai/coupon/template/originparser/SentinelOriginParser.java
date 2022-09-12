package com.dabai.coupon.template.originparser;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 借助Sentinel提供的RequestOriginParser扩展接口
 * 编写一个自定义解析器：
 *  识别来自上游的标记，并将其加入到Sentinel的链路统计中
 * @author
 * @create 2022-09-12 16:14
 */
@Slf4j
@Component
public class SentinelOriginParser implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest request) {
        log.info("requset {}, header={}", request, request.getHeaderNames());
        //从服务请求的Header中获取SentinelSource变量的值，作为调用源的name
        return request.getHeader("SentinelSource");
    }
}
