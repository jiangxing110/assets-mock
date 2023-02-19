package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;

/**
 * @author martin
 */
public interface CryptoAssetsTransactionService extends IService<CryptoAssetsTransaction> {

    CryptoAssetsTransaction getByTradeId(String tradeId);

    /**
     * 异步处理订单数据
     * 比如通过区块浏览器api更新发送方address
     *
     * @param entity transaction
     */
    void handle(CryptoAssetsTransaction entity);

    /**
     * 处理三方hook
     *
     * @param entity entity
     * @return boolean
     */
    CryptoAssetsTransfer hook(CryptoAssetsTransaction entity);
}
