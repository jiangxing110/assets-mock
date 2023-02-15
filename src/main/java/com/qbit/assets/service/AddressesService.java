package com.qbit.assets.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.Addresses;
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
public interface AddressesService extends IService<Addresses> {

    List<AddressVO> getAddresses(String walletId);

    AddressVO createAddress(String walletId, AddressDTO body);
}
