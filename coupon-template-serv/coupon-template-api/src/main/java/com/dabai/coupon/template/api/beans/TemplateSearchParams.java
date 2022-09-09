package com.dabai.coupon.template.api.beans;

import lombok.Data;

/**
 * 模板查询的参数对象
 * @author
 * @create 2022-09-06 9:14
 */
@Data
public class TemplateSearchParams {

    private Long id;

    private String name;

    private String type;

    private Long shopId;

    private Boolean available;

    private int page;

    private int pageSize;
}
