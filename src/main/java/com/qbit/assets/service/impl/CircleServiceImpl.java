package com.qbit.assets.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.service.CircleService;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.TransferVO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author martinjiang
 * @description CircleServiceImpl
 * @date 2023/2/19 15:38
 */
@Slf4j
@Service
public class CircleServiceImpl implements CircleService {

    @Resource
    private CryptoAssetsTransactionService cryptoAssetsTransactionService;

    /**
     * @param body
     * @return
     */
    /**
     * 链上充值
     */
    @Override
    @Transactional
    public CryptoAssetsTransfer chainDeposit(TransferVO body) {
        CryptoAssetsTransaction transaction = this.convert(body);
        return handleTransaction(transaction);
    }


    private CryptoAssetsTransfer handleTransaction(CryptoAssetsTransaction transaction) {
        CryptoAssetsTransfer transfer;
        CryptoAssetsTransaction record = cryptoAssetsTransactionService.getByTradeId(transaction.getTradeId());
        boolean result;
        if (record != null) {
            CircleTransactionStatusEnum status = record.getStatus();
            if (status == CircleTransactionStatusEnum.COMPLETE) {
                log.info("已处理订单被重复推送, 订单id: {}", transaction.getTradeId());
                return null;
            }
            transaction.setId(record.getId());
            result = cryptoAssetsTransactionService.updateById(transaction);
        } else {
            result = cryptoAssetsTransactionService.save(transaction);
        }
        log.info("transaction {} save result: {}", transaction.getId(), result);
        transfer = cryptoAssetsTransactionService.hook(transaction);
        //transactionService.handle(transaction);
        return transfer;
    }

    private CryptoAssetsTransaction convert(TransferVO data) {
        CryptoAssetsTransaction transaction = new CryptoAssetsTransaction();
        transaction.setTradeId(data.getId());
        transaction.setSourceType(data.getSource().getType());
        transaction.setSourceType(data.getSource().getType());
        transaction.setSourceId(data.getSource().getId());
        transaction.setDestinationType(data.getDestination().getType());
        transaction.setDestinationAddress(data.getDestination().getAddress());
        transaction.setDestinationId(data.getDestination().getId());
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data.getAmount().getAmount()));
        transaction.setAmount(amount);
        BigDecimal total = amount;

        BigDecimal fee = calculationFee(data.getFees());
        transaction.setFee(fee);
        total = total.add(fee);

        transaction.setTotalAmount(total);
        //设置pending
        transaction.setStatus(data.getStatus());
        ChainType chain = data.getDestination().getChain();
        if (chain == null) {
            chain = data.getSource().getChain();
        }
        transaction.setCurrency(data.getAmount().getCurrency());
        transaction.setChain(chain);
        transaction.setTransactionHash(data.getTransactionHash());
        transaction.setCreateTime(data.getCreateDate());
        return transaction;
    }

    /**
     * 计算总fee
     */
    private BigDecimal calculationFee(Object data) {
        List<AmountBO> fees = getFees(data);
        BigDecimal ret = BigDecimal.ZERO;
        for (AmountBO fee : fees) {
            ret = ret.add(new BigDecimal(fee.getAmount()));
        }
        return ret;
    }

    /**
     * 处理fee, circle 那边会更改数据结构
     */
    private List<AmountBO> getFees(Object data) {
        List<AmountBO> fees = new ArrayList<>();
        if (data instanceof Map<?, ?>) {
            AmountBO fee = JsonUtil.toBean(data, AmountBO.class);
            if (fee != null) {
                fees.add(fee);
            }
        } else if (data instanceof List<?>) {
            JavaType javaType = JsonUtil.getCollectionType(List.class, AmountBO.class);
            List<AmountBO> list = JsonUtil.toBean(JsonUtil.toJSONString(data), javaType);
            if (list != null) {
                fees.addAll(list);
            }
        }
        return fees;
    }
}
