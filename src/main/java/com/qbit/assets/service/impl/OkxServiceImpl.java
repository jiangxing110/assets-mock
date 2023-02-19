package com.qbit.assets.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qbit.assets.common.enums.*;
import com.qbit.assets.common.utils.OkxUtil;
import com.qbit.assets.domain.dto.CryptoAssetsTransferDTO;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.service.BalanceService;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.service.OkxService;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import com.qbit.assets.thirdparty.internal.okx.domain.dto.WithdrawalDTO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.SubAccountDepositVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.WithdrawalHistoryVO;
import com.qbit.assets.thirdparty.internal.okx.domain.vo.WithdrawalVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author martinjiang
 * @description OkxServiceImpl
 * @date 2023/2/19 15:50
 */
@Service
@Slf4j
public class OkxServiceImpl implements OkxService {

    @Resource
    private CryptoAssetsTransactionService transactionService;
    @Resource
    private CryptoAssetsTransferService cryptoAssetsTransferService;
    @Resource
    private BalanceService balanceService;

    /**
     * 链上充值
     */
    @Override
    @Transactional
    public CryptoAssetsTransfer chainDeposit(SubAccountDepositVO body) {
        CryptoAssetsTransaction transaction = this.convert(body);
        return this.handleTransaction(transaction, body);
    }

    /**
     * 链上提现记录
     */
    @Override
    public List<WithdrawalHistoryVO> getWithdrawals(Map<String, Object> params) {
        return null;
    }

    /**
     * 链上提现
     */
    @Override
    public WithdrawalVO withdrawal(WithdrawalDTO body) {
        CryptoAssetsTransferDTO transferDto = new CryptoAssetsTransferDTO();
        LambdaQueryWrapper<Balance> balanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        balanceLambdaQueryWrapper.eq(Balance::getAccountId, SpecialUUID.NullUUID.value());
        balanceLambdaQueryWrapper.eq(Balance::getWalletType, WalletTypeEnum.OkxWallet);
        balanceLambdaQueryWrapper.eq(Balance::getCurrency, body.getCcy());
        Balance balances = balanceService.getOne(balanceLambdaQueryWrapper);
        transferDto.setFromBalanceId(balances.getId());
        transferDto.setTransferId(body.getClientId());
        String[] chains = body.getChain().split("-");
        String chain = chains[1];
        ChainType chainType = OkxUtil.convertChain(chain);
        transferDto.setChain(chainType);
        transferDto.setToAddress(body.getToAddr());
        transferDto.setAmount(Double.valueOf(body.getAmt()));
        CryptoAssetsTransfer cryptoAssetsTransfer = cryptoAssetsTransferService.transferToChain(transferDto);
        WithdrawalVO withdrawalVO = new WithdrawalVO();
        BeanUtils.copyProperties(body, withdrawalVO);
        withdrawalVO.setWdId(cryptoAssetsTransfer.getTradeId());
        return withdrawalVO;
    }

    private CryptoAssetsTransaction convert(SubAccountDepositVO depositVo) {
        CryptoAssetsTransaction transaction = new CryptoAssetsTransaction();
        transaction.setSourceType(CircleWalletTypeEnum.BLOCKCHAIN);
        transaction.setDestinationType(CircleWalletTypeEnum.WALLET);
        transaction.setDestinationId(depositVo.getSubAcct());
        String chain = depositVo.getChain();
        String[] chains = chain.split("-");
        chain = chains[1];
        ChainType chainType = OkxUtil.convertChain(chain);
        transaction.setChain(chainType);
        transaction.setTradeId(depositVo.getDepId());
        transaction.setAmount(new BigDecimal(depositVo.getAmt()));
        transaction.setFee(BigDecimal.ZERO);
        transaction.setPlatform(CryptoAssetsPlatform.OKX);
        transaction.setTotalAmount(new BigDecimal(depositVo.getAmt()).add(BigDecimal.ZERO));
        transaction.setTransactionHash(depositVo.getTxId());
        transaction.setCurrency(CryptoConversionCurrencyEnum.getItem(depositVo.getCcy()));
        //状态处理
        //if ("0".equals(depositVo.getState()) || "1".equals(depositVo.getState()) || "8".equals(depositVo.getState())) {
        transaction.setStatus(CircleTransactionStatusEnum.PENDING);
        /*} else if ("2".equals(depositVo.getState())) {
            transaction.setStatus(CircleTransactionStatusEnum.COMPLETE);
        } else if ("12".equals(depositVo.getState()) || "13".equals(depositVo.getState())) {
            transaction.setStatus(CircleTransactionStatusEnum.FAILED);
        }*/
        // 获取接受地址
        transaction.setDestinationAddress(depositVo.getTo());
        return transaction;
    }

    @Transactional
    public CryptoAssetsTransfer handleTransaction(CryptoAssetsTransaction transaction, SubAccountDepositVO depositVo) {
        CryptoAssetsTransaction record = transactionService.getByTradeId(transaction.getTradeId());
        boolean result;
        if (record != null) {
            CircleTransactionStatusEnum status = record.getStatus();
            if (status == CircleTransactionStatusEnum.COMPLETE) {
                log.info("已处理订单被重复推送, 订单id: {}", transaction.getTradeId());
                return null;
            }
            transaction.setId(record.getId());
            result = transactionService.updateById(transaction);
        } else {
            result = transactionService.save(transaction);
        }
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        try {
            transfer = transactionService.hook(transaction);
            //OKX外部钱包转入写三方原始信息
            transactionService.handle(transaction);
            log.info("transaction {} hook result: {}", transaction.getId(), result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transfer;
    }
}
