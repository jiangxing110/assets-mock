package com.qbit.assets.thirdparty.internal.circle.service;


import com.qbit.assets.domain.vo.AccountBalanceVO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.*;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author liang
 */
public interface CircleSDKService {

    /**
     * circle健康检查
     *
     * @return boolean
     */
    boolean health();

    /**
     * 获取circle配置
     *
     * @return circle配置
     */
    ConfigurationVO getConfiguration();

    /**
     * 获取主钱包id
     *
     * @return wallet id
     */
    String getMasterWalletId();

    /**
     * 获取主账户余额
     *
     * @return wallet id
     */
    AccountBalanceVO getAccountBalances();

    /**
     * 创建circle钱包
     *
     * @param body body
     * @return WalletVO
     */
    WalletVO createWallet(WalletDTO body);

    /**
     * 获取circle钱包详情
     *
     * @param id 钱包id
     * @return WalletVO
     */
    WalletVO getWallet(String id);

    /**
     * 获取circle钱包列表
     *
     * @return List
     */
    List<WalletVO> getWallets();

    /**
     * 获取钱包列表
     *
     * @param params get params
     * @return List
     */
    List<WalletVO> getWallets(Map<String, String> params);

    /**
     * 创建chain address
     *
     * @param walletId 钱包id
     * @param body     address params
     * @return AddressVO
     */
    AddressVO createAddress(String walletId, AddressDTO body);

    /**
     * 获取钱包地址列表
     *
     * @param walletId 钱包id
     * @return List
     */
    List<AddressVO> getAddresses(String walletId);

    /**
     * circle交易
     *
     * @param body params
     * @return TransferVO
     */
    TransferVO createTransfer(TransferDTO body);

    /**
     * 获取交易记录
     *
     * @param params params
     * @return {@link List<TransferVO>}
     */
    List<TransferVO> getTransfers(Map<String, String> params);

    /**
     * 获取circle交易详情
     *
     * @param id 交易id
     * @return TransferVO
     */
    TransferVO getTransfer(String id);

    /**
     * 创建电汇银行账户
     *
     * @param data 电汇银行RequestBody
     * @return com.qbit.assets.circle.domain.vo.BankWireVo
     * @author martinjiang
     * @date 2022/4/9 3:39 下午
     */
    BankWireVO createBanksWires(BankWireDTO data);

    /**
     * 银行电汇详情
     *
     * @param bankAccountId 银行账户id
     * @return com.qbit.assets.circle.domain.vo.BankAccountVo
     * @author martinjiang
     * @date 2022/4/9 3:44 下午
     */
    BankWireVO instructions(String bankAccountId);

    /**
     * 模拟电汇
     *
     * @param dto {@link MockWirePaymentVO}
     * @return {@link MockWirePaymentVO}
     * @author martinjiang
     * @date 2022/4/9 3:45 下午
     */
    MockWirePaymentVO mockPaymentsWire(MockWirePaymentVO dto);

    /**
     * createPayout 创建payout
     *
     * @param payout {@link PayoutDTO}
     * @return {@link PayoutVO}
     * @author martinjiang
     * @date 2022/4/11 2:11 下午
     */
    PayoutVO createPayout(PayoutDTO payout);

    /**
     * 查询payout
     *
     * @param data 分页面参数
     * @return {@link List<PayoutVO>}
     * @author martinjiang
     * @date 2022/4/11 2:11 下午
     */
    List<PayoutVO> getPayouts(PayoutPageDTO data);

    /**
     * 根据 payout Id 查询
     *
     * @param id payout id
     * @return {@link PayoutVO}
     * @author martinjiang
     * @date 2022/4/11 2:35 下午
     */
    PayoutVO getPayoutById(String id);

    /**
     * 保存信息
     *
     * @param detail {@link CardDTO}
     * @return {@link CardVO}
     */
    CardVO saveCard(CardDTO detail);

    /**
     * 获取卡信息
     *
     * @param cardId card id
     * @return {@link CardVO}
     */
    CardVO getCard(String cardId);

    /**
     * 获取加密公钥
     *
     * @return {@link EncryptionPublicVO}
     */
    EncryptionPublicVO getEncryptionPublic();

    /**
     * 创建付款订单
     *
     * @param data {@link PaymentDTO}
     * @return {@link PaymentVO}
     */
    PaymentVO createPayments(PaymentDTO data);
}
