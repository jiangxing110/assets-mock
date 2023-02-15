package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.mapper.CryptoAssetsTransactionMapper;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import org.springframework.stereotype.Service;


/**
 * @author martinjiang
 */
@Service
public class CryptoAssetsTransactionServiceImpl extends ServiceImpl<CryptoAssetsTransactionMapper, CryptoAssetsTransaction> implements CryptoAssetsTransactionService {


    @Override
    public CryptoAssetsTransaction getByTradeId(String tradeId) {
        LambdaQueryWrapper<CryptoAssetsTransaction> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CryptoAssetsTransaction::getTradeId, tradeId);
        return this.getOne(wrapper);
    }


}
