package com.qbit.assets.domain.vo;

import com.qbit.assets.thirdparty.internal.circle.domain.vo.BalanceVO;
import lombok.Data;

import java.util.List;

/**
 * @author martinjiang
 * @description AccountBalanceVO
 * @date 2022/9/27 17:48
 */
@Data
public class AccountBalanceVO {
    private List<BalanceVO> available;
    private List<BalanceVO> unsettled;
}
