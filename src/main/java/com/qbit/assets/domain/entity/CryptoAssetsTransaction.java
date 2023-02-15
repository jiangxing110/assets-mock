package com.qbit.assets.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoAssetsPlatform;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.domain.base.BaseV2;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("assets_plat_transaction")
public class CryptoAssetsTransaction extends BaseV2 {

    private static final long serialVersionUID = 1L;

    /**
     * 三方交易id
     */
    private String tradeId;

    /**
     * 发送方类型(wallet/blockchain)
     */
    private CircleWalletTypeEnum sourceType;

    /**
     * 发送方id(wallet id)
     */
    private String sourceId;

    /**
     * 接收方类型(wallet/blockchain)
     */
    private CircleWalletTypeEnum destinationType;

    /**
     * 接收方id(wallet id)
     */
    private String destinationId;

    /**
     * 接收方地址(address)
     */
    private String destinationAddress;

    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     * 交易手续费
     */
    private BigDecimal fee;

    /**
     * 交易总金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易hash
     */
    private String transactionHash;


    /**
     * 链
     */
    private ChainType chain;

    /**
     * 币种
     */
    private CryptoConversionCurrencyEnum currency;

    /**
     * 发送方地址(address)
     */
    private String sourceAddress;


    /**
     * 交易状态
     */
    private CircleTransactionStatusEnum status;

    /**
     * 平台
     */
    private CryptoAssetsPlatform platform;
}
