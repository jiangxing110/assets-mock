package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;

/**
 * @author martin
 */
public interface CryptoAssetsTransactionService extends IService<CryptoAssetsTransaction> {

    CryptoAssetsTransaction getByTradeId(String tradeId);


}
