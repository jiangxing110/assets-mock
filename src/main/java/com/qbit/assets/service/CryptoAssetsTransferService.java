package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.common.enums.BalanceOperationEnum;
import com.qbit.assets.common.enums.CryptoAssetsTransferStatus;
import com.qbit.assets.domain.dto.AssetTransferDto;
import com.qbit.assets.domain.dto.BalanceChangeDTO;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.domain.entity.Transaction;

import java.math.BigDecimal;


/**
 * @author martinjiang
 */
public interface CryptoAssetsTransferService extends IService<CryptoAssetsTransfer> {

    CryptoAssetsTransfer deposit(AssetTransferDto body);

    CryptoAssetsTransfer withdraw(AssetTransferDto body);

    CryptoAssetsTransfer review(String transferId, CryptoAssetsTransferStatus status);

    /**
     * 封装业务表实体
     *
     * @param transaction transaction
     * @return CryptoAssetsTransfer
     */
    CryptoAssetsTransfer createEntity(Transaction transaction);

    /**
     * 构建内部交易params
     *
     * @param balance   balance
     * @param amount    交易金额
     * @param fee       交易手续费
     * @param operation sub/add
     * @return params
     */
    BalanceChangeDTO buildTransferDTO(Balance balance, BigDecimal amount, BigDecimal fee, BalanceOperationEnum operation);
}
