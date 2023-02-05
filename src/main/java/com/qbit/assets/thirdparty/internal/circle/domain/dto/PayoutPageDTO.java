package com.qbit.assets.thirdparty.internal.circle.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * payout查询
 *
 * @author martinjiang
 * @version 1.0
 * @date 2022/4/11 2:20 下午
 **/
@Data
public class PayoutPageDTO {

    /**
     * source id
     */
    private String source;
    /**
     * Destination bank account type 源帐户类型
     */
    private List<String> type;

    private String status;

    /**
     * 开始时间
     */
    private LocalDateTime from;

    /**
     * 结束时间
     */
    private LocalDateTime to;

    /**
     * 它标志着页面的唯一结束。提供时，集合资源将返回 idn之前的下一个项目，由 指定
     * pageSize这些项目将按集合的自然顺序返回
     * pageAfter如果既不指定也不指定资源将返回第一页pageBefore不应与 pageAfter 结合使用
     */
    private String pageBefore;

    /**
     * 它标志着页面的唯一开始。提供时，集合资源将返回 idn之后的下一个项目，由 指定。
     * pageSize这些项目将按集合的自然顺序返回
     * pageAfter如果既不指定也不指定资源将返回第一页pageBefore.不应与 pageBefore 结合使用。
     */
    private String pageAfter;

    /**
     * 页面大小限制要返回的项目数。
     */
    private String pageSize;
}
