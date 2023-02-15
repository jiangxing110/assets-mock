package com.qbit.assets.thirdparty.internal.circle.service;


import com.qbit.assets.domain.entity.PlatTransactions;

import java.util.Map;

/**
 * @author litao
 */
public interface CircleService {

    void notifications(Map<String, Object> params);


    public void handleTransaction(PlatTransactions transaction);
}
