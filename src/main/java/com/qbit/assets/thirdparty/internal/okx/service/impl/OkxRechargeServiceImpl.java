package com.qbit.assets.thirdparty.internal.okx.service.impl;


import cn.hutool.core.collection.CollectionUtil;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.enums.CryptoConversionCurrencyEnum;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.OkxUtil;
import com.qbit.assets.common.utils.SpringContextUtil;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.DestinationBO;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.SourceBO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.TransferDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.BalanceVO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.ConfigurationVO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.TransferVO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import com.qbit.assets.thirdparty.internal.circle.service.CircleSDKService;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.AssetsBalanceVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.AssetsDepositAddressVo;
import com.qbit.assets.thirdparty.internal.okx.service.OkxAssetsService;
import com.qbit.assets.thirdparty.internal.okx.service.OkxRechargeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author martinjiang
 * @description OkxRechargeServiceImpl
 * @date 2022/9/27 16:16
 */
@Service
@Slf4j
public class OkxRechargeServiceImpl extends OkxBaseServiceImpl implements OkxRechargeService {
    @Resource
    private OkxAssetsService okxAssetsService;
    @Resource
    private CircleSDKService circleSDKService;

    private final static String DEFAULT_TRX_ADDRESS = "THGTenLmvqWycGLGtgRvX4wURiHQeDvNps";

    /**
     * okx 自动充值
     *
     * @param
     * @return com.qbit.assets.circle.domain.vo.TransferVO
     * @author martinjiang
     * @version 1.0
     * @date 2022/9/27 16:19
     */
    @Override
    public TransferVO autoRecharge(CryptoConversionCurrencyEnum ccy, ChainType chainType, BigDecimal balanceLimit, BigDecimal amount) {
        //TODO amount 金额判断待优化
        TransferVO transferVO = null;
        String address = null;
        CryptoConversionCurrencyEnum circleCcy = ccy;
        if (CryptoConversionCurrencyEnum.USDC == ccy) {
            circleCcy = CryptoConversionCurrencyEnum.USD;
        }
        // 1获取交易账户余额
        List<AssetsBalanceVO> balances = okxAssetsService.getBalances(ccy.getValue());
        if (CollectionUtil.isNotEmpty(balances)) {
            AssetsBalanceVO okxBalanceVo = balances.get(0);
            BigDecimal availBal = new BigDecimal(okxBalanceVo.getAvailBal());
            if (amount != null && availBal.compareTo(amount) == 1) {
                return null;
            }
        }
        // 2获取交易账户余额充值地址
        List<AssetsDepositAddressVo> depositAddressVos = new ArrayList<>();
        if (SpringContextUtil.isProd()) {
            depositAddressVos = okxAssetsService.getDepositAddress(ccy.getValue());
        }
        if (CollectionUtil.isNotEmpty(depositAddressVos)) {
            String chain = ccy.getValue() + "-" + OkxUtil.convertChain(chainType);
            List<AssetsDepositAddressVo> firstObject = depositAddressVos.stream().filter(a -> chain.equals(a.getChain())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(firstObject)) {
                AssetsDepositAddressVo depositAddressVo = depositAddressVos.get(0);
                address = depositAddressVo.getAddr();
            }
        }
        // 3获取Circle 钱包余额
        List<BalanceVO> balanceVOS = circleSDKService.getAccountBalances().getAvailable();
        if (CollectionUtil.isNotEmpty(balanceVOS)) {
            CryptoConversionCurrencyEnum finalCircleCcy = circleCcy;
            List<BalanceVO> firstObject = balanceVOS.stream().filter(a -> finalCircleCcy.getValue().equals(a.getCurrency())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(firstObject)) {
                BalanceVO circleBalanceVO = firstObject.get(0);
                BigDecimal circleAmount = new BigDecimal(circleBalanceVO.getAmount());
                if (circleAmount.compareTo(balanceLimit) == -1 || circleAmount.compareTo(amount) == -1) {
                    //this.sendCircleInsufficientBalanceRecharge(CryptoConversionCurrencyEnum.USD, amount, circleAmount);
                    throw new CustomException("Circle balances " + finalCircleCcy + " not sufficient funds ");
                }
            }
            // 4发起交易充值到OKX 钱包地址
            if (StringUtils.isNotBlank(address)) {
                transferVO = this.createTransferToOkx(circleCcy, address, chainType, amount);
            } else {
                transferVO = this.createTransferToOkx(circleCcy, DEFAULT_TRX_ADDRESS, chainType, amount);
            }
            //this.sendOkxAutoRecharge(ccy, chainType, amount, address);
        }
        return transferVO;
    }

    private TransferVO createTransferToOkx(CryptoConversionCurrencyEnum ccy, String address, ChainType chainType, BigDecimal amount) {
        TransferDTO transfer = new TransferDTO();
        SourceBO source = new SourceBO();
        source.setType(CircleWalletTypeEnum.WALLET);
        ConfigurationVO configurationVO = circleSDKService.getConfiguration();
        source.setId(configurationVO.getPayments().getMasterWalletId());
        transfer.setSource(source);

        DestinationBO destination = new DestinationBO();
        destination.setType(CircleWalletTypeEnum.BLOCKCHAIN);
        destination.setAddress(address);
        destination.setChain(chainType);

        transfer.setDestination(destination);

        AmountBO amountBo = new AmountBO();
        if (SpringContextUtil.isProd()) {
            amountBo.setAmount(amount.toString());
        } else {
            amountBo.setAmount("5");
        }
        amountBo.setCurrency(ccy);

        transfer.setAmount(amountBo);
        TransferVO transferVO = circleSDKService.createTransfer(transfer);
        return transferVO;
    }


}
