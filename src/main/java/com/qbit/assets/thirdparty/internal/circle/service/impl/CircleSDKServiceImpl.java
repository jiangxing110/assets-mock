package com.qbit.assets.thirdparty.internal.circle.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qbit.assets.common.error.CustomException;
import com.qbit.assets.common.utils.JsonUtil;
import com.qbit.assets.domain.vo.AccountBalanceVO;
import com.qbit.assets.thirdparty.HttpService;
import com.qbit.assets.thirdparty.internal.circle.domain.dto.*;
import com.qbit.assets.thirdparty.internal.circle.domain.vo.*;
import com.qbit.assets.thirdparty.internal.circle.service.CircleSDKService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author litao
 */
@Slf4j
@Service
@ConfigurationProperties("circle")
public class CircleSDKServiceImpl implements CircleSDKService {
    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private HttpService httpService;

    @Setter
    private String key;

    @Setter
    private String host;

    @Setter
    @Getter
    private String masterWalletId;

    private Map<String, String> getHeaders() {
        String authorization = "Bearer " + key;
        HashMap<String, String> headers = new HashMap<>(1);
        headers.put("Authorization", authorization);
        return headers;
    }

    private String get(String url, Map<String, String> params) {
        return httpService.get(host + url, params, getHeaders());
    }


    private String post(String url, BaseDTO body) {
        body.setIdempotencyKey(UUID.randomUUID().toString());
        Map<String, Object> params = objectMapper.convertValue(body, new TypeReference<>() {
        });
        return httpService.post(host + url, params, getHeaders());
    }

    private JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    private JavaType getCollectionType(JavaType... parameterTypes) {
        return objectMapper.getTypeFactory().constructParametricType(Res.class, parameterTypes);
    }

    private <T> T getData(Res<T> value) {
        if (value == null) {
            return null;
        }
        if (value.getMessage() != null) {
            throw new CustomException(value.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return value.getData();
    }

    private <T> T readValue(String response, JavaType valueType) {
        log.info("response text: {}", response);
        return getData(JsonUtil.toBean(response, valueType));
    }

    private <T> T convert(String response, JavaType valueType) {
        return readValue(response, getCollectionType(valueType));
    }

    private <T> T convert(String response, Class<T> valueType) {
        return readValue(response, getCollectionType(Res.class, valueType));
    }

    @Override
    public boolean health() {
        String response = get("/ping", null);
        PingVO vo = JsonUtil.toBean(response, PingVO.class);
        if (vo == null) {
            return false;
        }
        return "pong".equals(vo.getMessage());
    }

    @Override
    public ConfigurationVO getConfiguration() {
        String response = get("/v1/configuration", null);
        return convert(response, ConfigurationVO.class);
    }

    /**
     * 获取主账户余额
     *
     * @return wallet id
     */
    @Override
    public AccountBalanceVO getAccountBalances() {
        String response = get("/v1/balances", null);
        log.info("/v1/balances-->" + response);
        return convert(response, AccountBalanceVO.class);
    }

    @Override
    public WalletVO createWallet(WalletDTO body) {
        String response = post("/v1/wallets", body);
        return convert(response, WalletVO.class);
    }

    @Override
    public WalletVO getWallet(String id) {
        String response = get("/v1/wallets/" + id, null);
        return convert(response, WalletVO.class);
    }

    @Override
    public List<WalletVO> getWallets() {
        return getWallets(null);
    }

    @Override
    public List<WalletVO> getWallets(Map<String, String> params) {
        String response = get("/v1/wallets", params);
        return convert(response, getCollectionType(ArrayList.class, WalletVO.class));
    }

    @Override
    public AddressVO createAddress(String walletId, AddressDTO body) {
        String response = post("/v1/wallets/" + walletId + "/addresses", body);
        return convert(response, AddressVO.class);
    }

    @Override
    public List<AddressVO> getAddresses(String walletId) {
        String response = get("/v1/wallets/" + walletId + "/addresses", null);
        return convert(response, getCollectionType(ArrayList.class, AddressVO.class));
    }

    @Override
    public TransferVO createTransfer(TransferDTO body) {
        String response = post("/v1/transfers", body);
        return convert(response, TransferVO.class);
    }

    @Override
    public List<TransferVO> getTransfers(Map<String, String> params) {
        String response = get("/v1/transfers", params);
        JavaType type = JsonUtil.getCollectionType(List.class, TransferVO.class);
        return convert(response, type);
    }

    @Override
    public TransferVO getTransfer(String id) {
        String response = get("/v1/transfers/" + id, null);
        return convert(response, TransferVO.class);
    }

    /**
     * 创建电汇银行账户
     *
     * @param data 电汇银行RequestBody
     * @return com.qbit.assets.circle.domain.vo.BankWireVo
     * @author martinjiang
     * @date 2022/4/9 3:39 下午
     */
    @Override
    public BankWireVO createBanksWires(BankWireDTO data) {
        //String response = post("/v1/businessAccount/banks/wires", data);
        String response = post("/v1/banks/wires", data);
        return convert(response, BankWireVO.class);
    }

    /**
     * 银行电汇详情
     *
     * @param bankAccountId 银行账户id
     * @return com.qbit.assets.circle.domain.vo.BankAccountVo
     * @author martinjiang
     * @date 2022/4/9 3:44 下午
     */
    @Override
    public BankWireVO instructions(String bankAccountId) {
        //String response = get("/v1/businessAccount/banks/wires/" + bankAccountId, null);
        String response = get("/v1/banks/wires/" + bankAccountId + "/instructions", null);
        return convert(response, BankWireVO.class);
    }

    /**
     * 模拟电汇
     *
     * @param dto {@link MockWirePaymentVO}
     * @return com.qbit.assets.circle.domain.vo.MockWirePaymentVo
     * @author martinjiang
     * @date 2022/4/9 3:45 下午
     */
    @Override
    public MockWirePaymentVO mockPaymentsWire(MockWirePaymentVO dto) {
        String url = "/v1/mocks/payments/wire";
        Map<String, Object> params = objectMapper.convertValue(dto, new TypeReference<>() {
        });
        String response = httpService.post(host + url, params, getHeaders());
        return convert(response, MockWirePaymentVO.class);
    }

    /**
     * creatPayOut 创建payout
     *
     * @param payout {@link PayoutDTO}
     * @return {@link PayoutVO}
     * @author martinjiang
     * @date 2022/4/11 2:11 下午
     */
    @Override
    public PayoutVO createPayout(PayoutDTO payout) {
        //String response = post("/v1/businessAccount/payouts", payout);
        String response = post("/v1/payouts", payout);
        return convert(response, PayoutVO.class);
    }

    /**
     * 根据 payout Id 查询
     *
     * @param id payout id
     * @return {@link PayoutVO}
     * @author martinjiang
     * @date 2022/4/11 2:35 下午
     */
    @Override
    public PayoutVO getPayoutById(String id) {
        //String response = get("/v1/businessAccount/payouts/" + id, null);
        String response = get("/v1/payouts/" + id, null);
        return convert(response, PayoutVO.class);
    }

    /**
     * 查询payout
     *
     * @param data pay 分页面参数{@link PayoutPageDTO}
     * @return com.qbit.assets.circle.domain.vo.PayOutVo
     * @author martinjiang
     * @date 2022/4/11 2:11 下午
     */
    @Override
    public List<PayoutVO> getPayouts(PayoutPageDTO data) {
        //String url = "/v1/businessAccount/payouts";
        String url = "/v1/payouts";
        JavaType collectionType = getCollectionType(Map.class, String.class, String.class);
        Map<String, String> map = JsonUtil.toBean(JsonUtil.toJSONString(data), collectionType);
        String response = get(url, map);
        return convert(response, getCollectionType(ArrayList.class, PayoutVO.class));
    }

    @Override
    public CardVO saveCard(CardDTO detail) {
        String response = post("/v1/cards", detail);
        return convert(response, CardVO.class);
    }

    @Override
    public CardVO getCard(String cardId) {
        String response = get(String.format("/v1/cards/%s", cardId), null);
        return convert(response, CardVO.class);
    }

    @Override
    public EncryptionPublicVO getEncryptionPublic() {
        String response = get("/v1/encryption/public", null);
        return convert(response, EncryptionPublicVO.class);
    }

    @Override
    public PaymentVO createPayments(PaymentDTO data) {
        String response = post("/v1/payments", data);
        return convert(response, PaymentVO.class);
    }


}
