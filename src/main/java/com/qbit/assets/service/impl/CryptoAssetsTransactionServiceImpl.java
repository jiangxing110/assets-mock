package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.mapper.CryptoAssetsTransactionMapper;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.UUID;


/**
 * @author martinjiang
 */
@Service
public class CryptoAssetsTransactionServiceImpl extends ServiceImpl<CryptoAssetsTransactionMapper, CryptoAssetsTransaction> implements CryptoAssetsTransactionService {

    @Resource
    private CryptoAssetsTransferService transferService;


    @Override
    public CryptoAssetsTransaction getByTradeId(String tradeId) {
        LambdaQueryWrapper<CryptoAssetsTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransaction::getTradeId, tradeId);
        return this.getOne(wrapper);
    }

    /**
     * 处理交易数据
     * 从链上获取交易详情解析sender address
     *
     * @param transaction entity
     */
    @Async
    @Override
    public void handle(CryptoAssetsTransaction transaction) {
        String hash = transaction.getTransactionHash();
        String id = transaction.getId();
        if (transaction.getSourceAddress() != null || StringUtils.isEmpty(hash) || id == null || transaction.getSourceType() != CircleWalletTypeEnum.BLOCKCHAIN) {
            return;
        }
        String address = UUID.randomUUID().toString();
        //factory.getSenderAddress(transaction.getChain(), hash);
        CryptoAssetsTransaction entity = new CryptoAssetsTransaction();
        entity.setId(id);
        entity.setSourceAddress(address);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public CryptoAssetsTransfer hook(CryptoAssetsTransaction entity) {
        CryptoAssetsTransfer transfer = new CryptoAssetsTransfer();
        // 不是完成状态，不能操作订单
        if (entity.getStatus() == CircleTransactionStatusEnum.PENDING) {
            return null;
        }
        CircleWalletTypeEnum destinationType = entity.getDestinationType();
        CircleWalletTypeEnum sourceType = entity.getSourceType();
        // 外部转入
        if (sourceType == CircleWalletTypeEnum.BLOCKCHAIN && destinationType == CircleWalletTypeEnum.WALLET) {
            // 生成transfer审核订单
            transfer = transferService.transferIn(entity);
        }
        // 退款
        if (sourceType == CircleWalletTypeEnum.WIRE && destinationType == CircleWalletTypeEnum.WALLET) {
            // 生成transfer审核订单
            transfer = transferService.handleReturn(entity);
        }
        // 转出到外部
        if (sourceType == CircleWalletTypeEnum.WALLET && destinationType != CircleWalletTypeEnum.WALLET) {
            // transfer表需要同步状态
            transfer = transferService.hook(entity);
        }
        // sourceType destinationType同时为wallet现在只有向主账户转账的操作
        return transfer;
    }


}
