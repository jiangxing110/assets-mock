package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author martinjiang
 */

public enum TransactionTypeEnum implements IEnum<String> {
    /**
     * 未知(老数据存在空的情况)
     */
    Unknown(""),

    /**
     * circle payments收款
     */
    CirclePaymentTransferIn("CirclePaymentTransferIn"),
    /**
     * 其他渠道入金
     */
    OtherChannelToVirtualUSDTransferIn("OtherChannelToVirtualUSDTransferIn"),
    /**
     * 加密资产转出到其他渠道手动出金
     */
    VirtualUSDTransferToOtherChannel("VirtualUSDTransferToOtherChannel"),

    /**
     * 数字货币转入
     */
    CryptoAssetsTransferIn("CryptoAssetsTransferIn"),
    /**
     * 数字货币转出
     */
    CryptoAssetsTransferOut("CryptoAssetsTransferOut"),

    /**
     * USDC钱包到wire bank
     */
    CircleWalletToWire("CircleWalletToWire"),
    /**
     * 解冻
     */
    Unfrozen("Unfrozen"),
    Frozen("Frozen"),

    /**
     * 暂无使用
     */
    Payout("Payout");


    @Override
    public String getValue() {
        return this.value;
    }

    @JsonValue
    public final String value;

    private TransactionTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
