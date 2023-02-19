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

    List<AddressVO> getAddresses(String walletId);

    AddressVO createAddress(String walletId, AddressDTO body);

    Balance getBalanceByAddress(ChainType chain, String destinationAddress);
}
