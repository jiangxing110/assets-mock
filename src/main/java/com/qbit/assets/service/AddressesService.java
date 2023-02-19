package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.domain.entity.Addresse;
import com.qbit.assets.domain.entity.Balance;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.AddressDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.AddressVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface AddressesService extends IService<Addresse> {
    /**
     * 根据钱包ID获取地址
     */
    List<AddressVO> getAddresses(String walletId);

    /**
     * 创建钱包地址
     */
    AddressVO createAddress(String walletId, AddressDTO body);

    /**
     * 更加地址获取钱包
     */
    Balance getBalanceByAddress(ChainType chain, String destinationAddress);
}
