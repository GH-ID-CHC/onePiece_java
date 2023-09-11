package net.maku.controller;/**
 * Author: CHAI
 * Date: 2023/9/11
 */

import io.swagger.v3.oas.annotations.tags.Tag;
import net.maku.constant.WxConfigConstant;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 微信公众号公共接口
 *
 * @program: onePiece
 * @author:
 * @create: 2023-09-11 21:21
 **/
@Tag(name = "微信公众号")
@RestController
@RequestMapping("/wx")
public class WxController {

    @GetMapping("/hello")
    public String hello() {
        return "hello wechat";
    }

    /**
     * 验证消息的确来自微信服务器
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串
     * @return {@code String}
     */
    @GetMapping("/verify")
    public String verify(String signature, String timestamp, String nonce, String echostr) {
        //进行signature校验，是否来自微信公众平台
        /**
         * 校验方式：
         * 1）将token、timestamp、nonce三个参数进行字典序排序
         * 2）将三个参数字符串拼接成一个字符串进行sha1加密
         * 3）开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
         * */
        List<String> list = Arrays.asList(WxConfigConstant.token, timestamp, nonce);
        //排序
        Collections.sort(list);
        //拼接
        StringBuilder builder = new StringBuilder();
        for (String s : list) {
            builder.append(s);
        }
        //进行sha1加密
        try {
            MessageDigest sha1 = MessageDigest.getInstance("sha1");
            byte[] digest = sha1.digest(builder.toString().getBytes());
            StringBuilder stringBuilder = new StringBuilder();
            //将加密的数据进行拼接与signature进行比较
            for (byte b : digest) {
                stringBuilder.append(Integer.toHexString((b >> 4) & 15));
                stringBuilder.append(Integer.toHexString(b & 15));
            }
            if (!signature.isEmpty() && signature.equals(stringBuilder.toString())) {
                return echostr;
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        System.out.println("执行==>" + signature);
        return null;
    }
}
