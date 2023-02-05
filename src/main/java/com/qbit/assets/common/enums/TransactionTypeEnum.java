package com.qbit.assets.common.enums;


import com.baomidou.mybatisplus.core.enums.IEnum;
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
     * 虚拟USD钱包到全球账户
     */
    VirtualUSDToQbitGlobal("VirtualUSDToQbitGlobal"),
    /**
     * 虚拟USD钱包到量子账户
     */
    VirtualUSDToQbitCardAccount("VirtualUSDToQbitCardAccount"),
    /**
     * 其他渠道入金
     */
    OtherChannelToVirtualUSDTransferIn("OtherChannelToVirtualUSDTransferIn"),
    /**
     * 加密资产转出到其他渠道手动出金
     */
    VirtualUSDTransferToOtherChannel("VirtualUSDTransferToOtherChannel"),
    /**
     * 量子账户到虚拟USD
     */
    QbitCardAccountToVirtualUSD("QbitCardAccountToVirtualUSD"),

    /**
     * 量子账户转到粒子理财
     */
    QbitCardAccountToFinancing("QbitCardAccountToFinancing"),
    /**
     * 粒子理财转到量子账户
     */
    FinancingToQbitCardAccount("FinancingToQbitCardAccount"),
    /**
     * 虚拟USD转到粒子理财
     */
    VirtualUSDToFinancing("VirtualUSDToFinancing"),
    /**
     * 粒子理财转到虚拟USD
     */
    FinancingToVirtualUSD("FinancingToVirtualUSD"),
    /**
     * USDC转到粒子理财
     */
    CircleWalletToFinancing("CircleWalletToFinancing"),
    /**
     * 粒子理财转到USDC
     */
    FinancingToCircleWallet("FinancingToCircleWallet"),

    /**
     * 理财收益
     */
    FinancingProfit("FinancingProfit"),

    /**
     * 全球账户到虚拟USD钱包
     */
    QbitGlobalToToVirtualUSDTransferIn("QbitGlobalToToVirtualUSDTransferIn"),
    /**
     * 量子账户转到虚拟USD钱包
     */
    QbitCardAccountToVirtualUSDTransferIn("QbitCardAccountToVirtualUSDTransferIn"),

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
     * 量子账户充值(内部活动充值) 比如 开卡返现 问卷返现 等
     */
    QbitCardAccountTransferIn("QbitCardAccountTransferIn"),
    /**
     * 量子账户转出 开卡返现-误操作回冲 客户退款 等
     */
    QbitCardAccountTransferOut("QbitCardAccountTransferOut"),
    /**
     * 量子账户充值到预算组
     */
    QbitCardAccountRechargeQbitCardGroup("QbitCardAccountRechargeQbitCardGroup"),
    /**
     * 预算组转出到量子账户
     */
    QbitCardGroupReturnQbitCardAccount("QbitCardGroupReturnQbitCardAccount"),
    /**
     * 量子账户充值到量子卡
     */
    QbitCardAccountToQbitCard("QbitCardAccountToQbitCard"),
    /**
     * 量子卡转出到量子账户
     */
    QbitCardToQbitCardAccount("QbitCardToQbitCardAccount"),
    /**
     * 量子卡退款 例如 QbitCardTransactionTypeEnum.Refund QbitCardTransactionTypeEnum.Credit
     */
    QbitCardTransferIn("QbitCardTransferIn"),
    /**
     * 量子卡消费 例如 QbitCardTransactionTypeEnum.Consumption
     */
    QbitCardConsumption("QbitCardConsumption"),
    /**
     * 量子卡开卡费
     */
    QbitCardFee("QbitCardFee"),
    /**
     * 量子账户充值(外部充值)
     */
    Deposit("Deposit"),
    /**
     * 量子账户钱包转到全球账户
     */
    QbitCardWalletToQbitGlobal("QbitCardWalletToQbitGlobal"),
    /**
     * 全球账户到量子账户钱包
     */
    QbitGlobalToQbitCardWallet("QbitGlobalToQbitCardWallet"),
    /**
     * 子账户到母账户的量子账户
     */
    QbitCardWalletSubToMaster("QbitCardWalletSubToMaster"),
    /**
     * 母账户到子账户的量子账户
     */
    QbitCardWalletMasterToSub("QbitCardWalletMasterToSub"),
    /**
     * 全球账户入账
     */
    QbitGlobalInbound("QbitGlobalInbound"),
    /**
     * 全球账户出账
     */
    QbitGlobalOutbound("QbitGlobalOutbound"),

    /**
     * 2.0 迁移数据 量子卡开卡费
     */
    CreateCard("CreateCard"),
    /**
     * 2.0 迁移数据 退款
     */
    Credit("Credit"),
    /**
     * 2.0 旧系统标记 暂无使用
     */
    OldVersion("OldVersion"),

    /**
     * 暂无使用
     */
    QbitCardTransferOut("QbitCardTransferOut"),
    /**
     * 暂无使用
     */
    InternalTransfer("InternalTransfer"),
    /**
     * 暂无使用
     */
    Payout("Payout"),
    /**
     * 暂无使用
     */
    Fx("Fx"),
    /**
     * 暂无使用
     */
    DeleteCardPayout("DeleteCardPayout"),
    /**
     * 用于量子账户退款或清退 暂无使用
     */
    Refund("Refund"),
    /**
     * 钱包可用金额冻结
     */
    Frozen("Frozen"),
    /**
     * 钱包冻结金额解冻
     */
    UnFrozen("UnFrozen"),
    /**
     * 卡提现
     */
    CardWithdraw("CardWithdraw"),
    /**
     * 退款费
     */
    Fee_Credit("Fee_Credit"),
    /**
     * 客户买大礼包
     */
    BuyVipPackage("BuyVipPackage"),
    /**
     * 加密资产到量子账户钱包
     */
    QbitCryptoToQbitCardWallet("QbitCryptoToQbitCardWallet"),
    /**
     * 量子账户钱包到加密资产
     */
    QbitCardWalletToQbitCrypto("QbitCardWalletToQbitCrypto"),
    /**
     * 订单消费手续费
     */
    Fee_Consumption("Fee_Consumption"),

    /**
     * 交易撤销
     */
    Reversal("Reversal");

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
