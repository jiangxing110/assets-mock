package com.qbit.assets.thirdparty.internal.circle.service.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.qbit.assets.common.enums.ChainType;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.domain.entity.PlatTransactions;
import com.qbit.assets.thirdparty.HttpService;
import com.qbit.assets.thirdparty.internal.circle.domain.bo.AmountBO;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.SubscriptionDTO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.BankWireVO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.PayoutVO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.ReturnVO;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.TransferVO;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleNotificationTypeEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.CircleWalletTypeEnum;
import com.qbit.assets.thirdparty.internal.circle.enums.SubscriptionType;
import com.qbit.assets.thirdparty.internal.circle.service.CircleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author litao
 */
@Slf4j
@Service
public class CircleServiceImpl implements CircleService {
    @Resource
    private CircleSDKServiceImpl circleSdk;
    @Resource
    private HttpService httpService;
    public static Pattern pattern = Pattern.compile("^arn:aws:sns:.*:908968368384:(sandbox|prod)_platform-notifications-topic$");

    @Override
    public void notifications(Map<String, Object> params) {
        SubscriptionDTO body = JsonUtil.toBean(params, SubscriptionDTO.class);

        boolean matches = CircleServiceImpl.pattern.matcher(body.getTopicArn()).matches();
        if (!matches) {
            throw new CustomException("Valid topic arn must match " + body.getTopicArn(), HttpStatus.BAD_REQUEST);
        }
        SubscriptionType type = body.getType();
        switch (type) {
            case SubscriptionConfirmation:
                handleSubscriptionConfirmation(body);
                break;
            case Notification:
                handleNotification(body.getMessage());
                break;
            default:
                log.error("unknown subscription type: {}", type);
                break;
        }
    }

    private void handleSubscriptionConfirmation(SubscriptionDTO body) {
        String response = httpService.get(body.getSubscribeUrl(), null, null);
        log.info("aws subscription url response: {}", response);
    }

    @Override
    public void handleTransaction(PlatTransactions transaction) {

    }

    private void handleNotification(String messageStr) {
        if (messageStr == null) {
            log.info("handleNotification messageStr is null");
            return;
        }
        SubscriptionDTO.MessageDTO message = JsonUtil.toBean(messageStr, SubscriptionDTO.MessageDTO.class);
        if (message == null) {
            log.info("handleNotification message is null");
            return;
        }
        CircleNotificationTypeEnum type = message.getNotificationType();
        switch (type) {
            case TRANSFERS:
                handleTransfers(message.getTransfer());
                break;
            case PAYOUTS:
                handlePayouts(message.getPayout());
                break;
            case RETURNS:
                handleReturns(message.getReturns());
                break;
            case WIRE:
                handleWire(message.getWire());
                break;
            default:
                log.error("unknown notification type: {}", type);
                break;
        }
    }


    private void handleTransfers(TransferVO data) {
        if (data == null) {
            log.info("handleTransfers data is null");
            return;
        }
        PlatTransactions transaction = convert(data);
        handleTransaction(transaction);
    }

    private void handlePayouts(PayoutVO data) {
        if (data == null) {
            log.info("handlePayouts data is null");
            return;
        }
        PlatTransactions transaction = convert(data);
        handleTransaction(transaction);
    }

    private void handleReturns(ReturnVO data) {
        if (data == null) {
            log.info("handleReturns data is null");
            return;
        }
        String payoutId = data.getPayoutId();
        if (StringUtils.isBlank(payoutId)) {
            log.info("不是payout退款:\n{}", data);
            return;
        }
        PlatTransactions transaction = convert(data);
        if (transaction == null) {
            log.info("找不到退款订单:\n{}", data);
            return;
        }
        handleTransaction(transaction);
    }

    private void handleWire(BankWireVO data) {
        if (data == null) {
            log.info("handleWire data is null");
            return;
        }
    }


    private PlatTransactions convert(PayoutVO data) {
        PlatTransactions transaction = new PlatTransactions();

        transaction.setTradeId(data.getId());

        transaction.setSourceType(CircleWalletTypeEnum.WALLET);
        transaction.setSourceId(data.getSourceWalletId());

        transaction.setDestinationType(data.getDestination().getType());
        transaction.setDestinationId(data.getDestination().getId());

        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data.getAmount().getAmount()));
        transaction.setAmount(amount);
        transaction.setTotalAmount(amount);
        transaction.setCurrency(data.getAmount().getCurrency());
        transaction.setStatus(data.getStatus());
        return transaction;
    }

    private PlatTransactions convert(TransferVO data) {
        PlatTransactions transaction = new PlatTransactions();
        transaction.setTradeId(data.getId());
        transaction.setSourceType(data.getSource().getType());
        transaction.setSourceType(data.getSource().getType());
        transaction.setSourceId(data.getSource().getId());
        transaction.setDestinationType(data.getDestination().getType());
        transaction.setDestinationAddress(data.getDestination().getAddress());
        transaction.setDestinationId(data.getDestination().getId());
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(data.getAmount().getAmount()));
        transaction.setAmount(amount);
        BigDecimal total = amount;

        BigDecimal fee = calculationFee(data.getFees());
        transaction.setFee(fee);
        total = total.add(fee);

        transaction.setTotalAmount(total);
        transaction.setStatus(data.getStatus());
        ChainType chain = data.getDestination().getChain();
        if (chain == null) {
            chain = data.getSource().getChain();
        }
        transaction.setCurrency(data.getAmount().getCurrency());
        transaction.setChain(chain);
        transaction.setTransactionHash(data.getTransactionHash());
        transaction.setCreateTime(data.getCreateDate());
        return transaction;
    }


    private PlatTransactions convert(ReturnVO data) {
        return new PlatTransactions();
    }

    /**
     * 计算总fee
     *
     * @param data circle fee数据
     * @return fee
     */
    private BigDecimal calculationFee(Object data) {
        List<AmountBO> fees = getFees(data);
        BigDecimal ret = BigDecimal.ZERO;
        for (AmountBO fee : fees) {
            ret = ret.add(new BigDecimal(fee.getAmount()));
        }
        return ret;
    }

    /**
     * 处理fee, circle 那边会更改数据结构
     *
     * @param data circle fee数据
     * @return {@link List<AmountBO>}
     */
    private List<AmountBO> getFees(Object data) {
        List<AmountBO> fees = new ArrayList<>();
        if (data instanceof Map<?, ?>) {
            AmountBO fee = JsonUtil.toBean(data, AmountBO.class);
            if (fee != null) {
                fees.add(fee);
            }
        } else if (data instanceof List<?>) {
            JavaType javaType = JsonUtil.getCollectionType(List.class, AmountBO.class);
            List<AmountBO> list = JsonUtil.toBean(JsonUtil.toJSONString(data), javaType);
            if (list != null) {
                fees.addAll(list);
            }
        }
        return fees;
    }

}
