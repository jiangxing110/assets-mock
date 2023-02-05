package com.qbit.assets.thirdparty.internal.okx.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * okx分页查询
 *
 * @author litao
 */
@Data
public class PageVO<T> implements Serializable {
    /**
     * serial version UID
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总页数
     */
    private String totalPage;

    /**
     * 当前页
     */
    private String page;

    /**
     * 分页数据
     */
    private List<T> details;
}
