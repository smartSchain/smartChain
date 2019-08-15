package com.sequee.test.chain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class TransactionTest {

  /** 配置自己的账户私钥、owner_key、id*/
  private static String privateKey = "";
  private static String id = "";
  private static String memoKey = "";

  public static String getObjects() throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"method\":\"call\",\"params\":[0,\"get_objects\",[[\"2.1.0\"]]],\"id\":1}");
    Request request = new Request.Builder()
        .url("http://qb.adc.life/smartwallet/rpc")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .build();

    Response response = client.newCall(request).execute();
    return response.body() != null ? response.body().string() : null;
  }

  public static String getChainId() throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(mediaType, "{\"method\":\"call\",\"params\":[0,\"get_chain_id\",[]],\"id\":1}");
    Request request = new Request.Builder()
        .url("http://qb.adc.life/smartwallet/rpc")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("cache-control", "no-cache")
        .build();

    Response response = client.newCall(request).execute();
    return response.body() != null ? response.body().string() : null;
  }

  public static String sign(Integer headBlockNumber, String headBlockId, String time, String chainId) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    String requestContent = "{\"privKey\":\"%s\",\"from\":{\"id\":\"%s\",\"memoKey\":\"%s\"},\"to\":{\"id\":\"1.2.2586\",\"memoKey\":\"DBX7zPVDqXXAhcZiiG5NGvcJNoFakLazqYSHbg2suNUPsrWvSsoDM\"},\"fee\":{\"amount\":\"100\",\"assetId\":\"1.3.10\"},\"amount\":{\"amount\":\"1000000\",\"assetId\":\"1.3.10\"},\"memo\":\"\",\"blockHeader\":{\"time\":\"%s\",\"head_block_number\":\"%d\",\"head_block_id\":\"%s\"},\"chainId\":\"%s\"}";
    String content = String.format(requestContent, privateKey, id, memoKey, time, headBlockNumber, headBlockId, chainId);
    System.out.println("sign content is " + content);
    RequestBody body = RequestBody.create(mediaType, content);

    Request request = new Request.Builder()
        .url("http://127.0.0.1:3000/buildTransaction")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("cache-control", "no-cache")
        .build();

    Response response = client.newCall(request).execute();
    return response.body() != null ? response.body().string() : null;
  }

  public static String transaction(String signData) throws IOException {
    OkHttpClient client = new OkHttpClient();

    MediaType mediaType = MediaType.parse("application/json");
    String requestContent = "{\"id\":510,\"method\":\"call\",\"params\":[2,\"broadcast_transaction_with_callback\",[510,%s]]}";
    String content = String.format(requestContent, signData);
    System.out.println("transaction content is " + content);
    RequestBody body = RequestBody.create(mediaType, content);
    Request request = new Request.Builder()
        .url("http://qb.adc.life/smartwallet/rpc")
        .post(body)
        .addHeader("Content-Type", "application/json")
        .addHeader("cache-control", "no-cache")
        .build();

    Response response = client.newCall(request).execute();
    return response.body() != null ? response.body().string() : null;
  }

  public static void main(String[] args) throws IOException {
    String object = getObjects();
    if (StringUtils.isEmpty(object)) {
      return;
    }
    JSONObject jsonObject = JSONObject.parseObject(object);
    String data = jsonObject.getString("data");
    JSONObject dataObject = JSONObject.parseObject(data);
    JSONArray resultArray = dataObject.getJSONArray("result");
    JSONObject resultObject = resultArray.getJSONObject(0);

    // 获取headBlockNumber、headBlockId、time
    Integer headBlockNumber = resultObject.getInteger("head_block_number");
    String headBlockId = resultObject.getString("head_block_id");
    String time = resultObject.getString("time");

    String chain = getChainId();
    if (StringUtils.isEmpty(chain)) {
      return;
    }
    JSONObject chainObject = JSONObject.parseObject(chain);
    data = (String) chainObject.get("data");
    dataObject = JSONObject.parseObject(data);

    // 获取chainId
    String chainId = dataObject.getString("result");

    String sign = sign(headBlockNumber, headBlockId, time, chainId);
//    System.out.println("sign result is " + sign);
    if (StringUtils.isEmpty(sign)) {
      return;
    }
    String formatSign = signFormat(sign);
    String result = transaction(formatSign);
    System.out.println("result is " + result);

  }

  public static String signFormat(String sign) {
    String signStr = "{\n" +
        "    \"ref_block_num\": %d,\n" +
        "    \"ref_block_prefix\": %d,\n" +
        "    \"expiration\": \"%s\",\n" +
        "    \"operations\": [\n" +
        "      [\n" +
        "        0,\n" +
        "        {\n" +
        "          \"fee\": {\n" +
        "            \"amount\": \"%s\",\n" +
        "            \"asset_id\": \"%s\"\n" +
        "          },\n" +
        "          \"from\": \"%s\",\n" +
        "          \"to\": \"%s\",\n" +
        "          \"amount\": {\n" +
        "            \"amount\": \"%s\",\n" +
        "            \"asset_id\": \"%s\"\n" +
        "          },\n" +
        "          \"extensions\": []\n" +
        "        }\n" +
        "      ]\n" +
        "    ],\n" +
        "    \"extensions\": [],\n" +
        "    \"from_key\": \"%s\",\n" +
        "    \"to_key\": \"%s\",\n" +
        "    \"signatures\": [\n" +
        "      \"%s\"\n" +
        "    ]\n" +
        "  }";
    String signStrHasMemo = "{\n" +
        "    \"ref_block_num\": %d,\n" +
        "    \"ref_block_prefix\": %d,\n" +
        "    \"expiration\": \"%s\",\n" +
        "    \"operations\": [\n" +
        "      [\n" +
        "        0,\n" +
        "        {\n" +
        "          \"fee\": {\n" +
        "            \"amount\": \"%s\",\n" +
        "            \"asset_id\": \"%s\"\n" +
        "          },\n" +
        "          \"from\": \"%s\",\n" +
        "          \"to\": \"%s\",\n" +
        "          \"amount\": {\n" +
        "            \"amount\": \"%s\",\n" +
        "            \"asset_id\": \"%s\"\n" +
        "          },\n" +
        "          \"memo\": {\n" +
        "            \"from\": \"%s\",\n" +
        "            \"to\": \"%s\",\n" +
        "            \"nonce\": \"%s\",\n" +
        "            \"message\": \"%s\"\n" +
        "          },\n" +
        "          \"extensions\": []\n" +
        "        }\n" +
        "      ]\n" +
        "    ],\n" +
        "    \"extensions\": [],\n" +
        "    \"from_key\": \"%s\",\n" +
        "    \"to_key\": \"%s\",\n" +
        "    \"signatures\": [\n" +
        "      \"%s\"\n" +
        "    ]\n" +
        "  }";
    JSONObject signObject = JSONObject.parseObject(sign);
    JSONObject signData = signObject.getJSONObject("data");
    Integer refBlockNum = signData.getInteger("ref_block_num");
    String fromKey = signData.getString("from_key");
    String toKey = signData.getString("to_key");
    Long refBlockPrefix = signData.getLong("ref_block_prefix");
    String expiration = signData.getString("expiration");
    JSONArray operations = signData.getJSONArray("operations");
    JSONArray operations1 = operations.getJSONArray(0);
    JSONObject transData = operations1.getJSONObject(1);
    JSONObject feeData = transData.getJSONObject("fee");
    String feeAmount = feeData.getString("amount");
    String feeAssetId = feeData.getString("asset_id");
    String from = transData.getString("from");
    String to = transData.getString("to");
    JSONObject amountData = transData.getJSONObject("amount");
    String amount = amountData.getString("amount");
    String assetId = amountData.getString("asset_id");
    JSONArray signatures = signData.getJSONArray("signatures");
    String singature = signatures.getString(0);
    if (transData.containsKey("memo")) {
      JSONObject memo = transData.getJSONObject("memo");
      String memoFrom = memo.getString("from");
      String memoTo = memo.getString("to");
      String nonce = memo.getString("nonce");
      String message = memo.getString("message");
      return String.format(signStrHasMemo, refBlockNum, refBlockPrefix, expiration, feeAmount,
          feeAssetId, from, to, amount, assetId, memoFrom, memoTo, nonce, message, fromKey, toKey, singature);
    } else {
      return String.format(signStr, refBlockNum, refBlockPrefix, expiration, feeAmount,
          feeAssetId, from, to, amount, assetId, fromKey, toKey, singature);
    }

  }
}
