package com.onepiece.chai.utils;/**
 * Author: CHAI
 * Date: 2023/9/18
 */

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-18 22:12
 **/
public class ClientUtil
{
    public static COSClient getTestClient()
    {
        // 1 初始化用户身份信息（secretId, secretKey）
        String secretId = "AKIDNSfcOA43Fl5buzpbWaOBwQdFafdjo6OU";
        String secretKey = "NTnqb5IFvtbEivgLmvtdPxIb8ZikZqtt";
        return getCosClient(secretId, secretKey, "ap-nanjing");
    }

    public static COSClient getCosClient(String secretId, String secretKey, String _region)
    {
        // 1 初始化用户身份信息（secretId, secretKey）。
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, CI 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 https), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region(_region);
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }
}
