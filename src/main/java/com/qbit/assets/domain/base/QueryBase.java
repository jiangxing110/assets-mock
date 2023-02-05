package com.qbit.assets.domain.base;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author martinjiang
 */
@Data
public class QueryBase implements Serializable {
    private int limit = 10;

    private int page = 0;

    private String displayOrAccountId;

    /**
     * account id搜索条件
     */
    private String accountId;

    private String verifiedName;

    private String operationManagerName;

    private String saleName;

    private List<String> accountIds = null;

    private List<String> salesIds;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
