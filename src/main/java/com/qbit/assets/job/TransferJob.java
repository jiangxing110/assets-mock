package com.qbit.assets.job;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qbit.assets.common.enums.CryptoAssetsTransferStatus;
import com.qbit.assets.domain.entity.CryptoAssetsTransaction;
import com.qbit.assets.domain.entity.CryptoAssetsTransfer;
import com.qbit.assets.service.CryptoAssetsTransactionService;
import com.qbit.assets.service.CryptoAssetsTransferService;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleTransactionStatusEnum;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author martinjiang
 * @description TransferJob
 * @date 2023/2/19 19:42
 */
@Component
@EnableScheduling
@EnableAsync
public class TransferJob {

    @Resource
    private CryptoAssetsTransferService cryptoAssetsTransferService;
    @Resource
    private CryptoAssetsTransactionService cryptoAssetsTransactionService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void test() {
        LambdaQueryWrapper<CryptoAssetsTransfer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CryptoAssetsTransfer::getDisplayStatus, CryptoAssetsTransferStatus.Pending);
        List<CryptoAssetsTransfer> list = cryptoAssetsTransferService.list(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            for (CryptoAssetsTransfer cryptoAssetsTransfer : list) {
                CryptoAssetsTransfer reviewVo = cryptoAssetsTransferService.review(cryptoAssetsTransfer.getId(), CryptoAssetsTransferStatus.Closed);
            }
        }
    }


    @Scheduled(cron = "0 */1 * * * ?")
    public void testConcurrence() {
        LambdaQueryWrapper<CryptoAssetsTransaction> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CryptoAssetsTransaction::getStatus, CryptoAssetsTransferStatus.Pending);
        List<CryptoAssetsTransaction> list = cryptoAssetsTransactionService.list(lambdaQueryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            for (CryptoAssetsTransaction cryptoAssetsTransaction : list) {
                cryptoAssetsTransaction.setStatus(CircleTransactionStatusEnum.COMPLETE);
                CryptoAssetsTransfer cryptoAssetsTransfer = cryptoAssetsTransactionService.hook(cryptoAssetsTransaction);
                //WebHook
            }
        }
    }
}
