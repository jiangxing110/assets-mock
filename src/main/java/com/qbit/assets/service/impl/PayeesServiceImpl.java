package com.qbit.assets.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qbit.assets.common.enums.SpecialUUID;
import com.qbit.assets.domain.entity.Payees;
import com.qbit.assets.domain.entity.PayeesAddresses;
import com.qbit.assets.domain.entity.PayeesBanks;
import com.qbit.assets.mapper.PayeesAddressesMapper;
import com.qbit.assets.mapper.PayeesBanksMapper;
import com.qbit.assets.mapper.PayeesMapper;
import com.qbit.assets.service.PayeesService;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.BankWireDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.BankWireVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author martin
 * @since 2023-02-05
 */
@Service
@Slf4j
public class PayeesServiceImpl extends ServiceImpl<PayeesMapper, Payees> implements PayeesService {
    @Resource
    private PayeesMapper payeesMapper;
    @Resource
    private PayeesAddressesMapper payeesAddressesMapper;
    @Resource
    private PayeesBanksMapper payeesBanksMapper;

    @Override
    public BankWireVO wires(BankWireDTO body) {
        Payees payees = new Payees();
        PayeesAddresses bankAddress = new PayeesAddresses();
        BeanUtils.copyProperties(body.getBankAddress(), bankAddress);

        PayeesAddresses billingAddress = new PayeesAddresses();
        BeanUtils.copyProperties(body.getBillingDetails(), billingAddress);
        payeesAddressesMapper.insert(billingAddress);
        payeesAddressesMapper.insert(bankAddress);
        PayeesBanks payeesBanks = new PayeesBanks();
        payeesBanks.setId(body.getIdempotencyKey());
        payeesBanks.setBankName(body.getBankAddress().getBankName());
        payeesBanks.setRoutingNumber(body.getRoutingNumber());
        //账户持有人名称
        payeesBanks.setName(body.getBillingDetails().getName());
        //开户银行地址
        payeesBanks.setBankAddressId(bankAddress.getId());
        //持卡人/账单地址
        payeesBanks.setBillingAddressId(billingAddress.getId());
        payeesBanksMapper.insert(payeesBanks);
        payees.setAccountId(SpecialUUID.NullUUID.value);
        payeesBanks.setId(body.getIdempotencyKey());
        payees.setCountry(body.getBillingDetails().getCountry());
        payees.setAccountNumber(body.getAccountNumber());
        payees.setType("wire");
        String accountNumber = body.getAccountNumber();
        String description = body.getBankAddress().getBankName() + " ****" + accountNumber.substring(accountNumber.length() - 4, accountNumber.length());
        payees.setDescription(description);
        payees.setFingerprint(UUID.randomUUID().toString());
        payees.setStatus("complete");
        payees.setTrackingRef("CIR3534MST");
        payees.setVirtualAccountEnabled(Boolean.TRUE);
        payees.setId(body.getIdempotencyKey());
        payeesMapper.insert(payees);
        BankWireVO bankWireVO = this.instructions(body.getIdempotencyKey());
        return bankWireVO;
    }

    @Override
    public BankWireVO instructions(String bankAccountId) {
        BankWireVO wireVO = new BankWireVO();
        Payees payees = payeesMapper.selectById(bankAccountId);
        BeanUtils.copyProperties(payees, wireVO);
        PayeesBanks payeesBanks = payeesBanksMapper.selectById(bankAccountId);
        if (payeesBanks != null) {
            BankWireDTO.BillingDetailsDTO billingVo = new BankWireDTO.BillingDetailsDTO();
            BankWireDTO.BankAddressDTO bankVo = new BankWireDTO.BankAddressDTO();
            PayeesAddresses bankAddress = payeesAddressesMapper.selectById(payeesBanks.getBankAddressId());
            BeanUtils.copyProperties(bankAddress, bankVo);
            PayeesAddresses billingDetails = payeesAddressesMapper.selectById(payeesBanks.getBillingAddressId());
            BeanUtils.copyProperties(billingDetails, billingVo);
            billingVo.setName(payeesBanks.getName());
            bankVo.setBankName(payeesBanks.getBankName());
            wireVO.setBankAddress(bankVo);
            wireVO.setBillingDetails(billingVo);
        }
        wireVO.setCreateDate(payees.getCreateTime().toString());
        wireVO.setUpdateDate(payees.getUpdateTime().toString());
        return wireVO;
    }

}