package com.qbit.assets.service;

import com.qbit.assets.domain.entity.Balance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author martinjiang
 * @description balanceTest
 * @date 2023/2/4 23:25
 */
@SpringBootTest
public class balanceTest {
    @Resource
    private BalanceService balanceService;

    @Test
    void listTest() {
        List<Balance> balanceList = balanceService.list();
        for (Balance s : balanceList) {
            System.err.println(s);
        }

    }


}
