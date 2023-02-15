package com.qbit.assets.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.qbit.assets.domain.entity.Payees;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.BankWireDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.BankWireVO;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
public interface PayeesService extends IService<Payees> {

    BankWireVO wires(BankWireDTO body);

    BankWireVO instructions(String bankAccountId);
}
