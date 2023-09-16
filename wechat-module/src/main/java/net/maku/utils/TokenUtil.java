//package net.maku.utils;/**
// * Author: CHAI
// * Date: 2023/9/14
// */
//
//import cn.hutool.http.HttpUtil;
//import jakarta.annotation.PostConstruct;
//import me.chanjar.weixin.mp.api.WxMpConfigStorage;
//import net.maku.config.WxAccountConfig;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * 获取 access_token的工具类
// * @program: onePiece
// * @author:
// * @create: 2023-09-14 23:18
// **/
//public class TokenUtil {
//
//    public static String getToken(String appid,String secret) {
////        使用format
//        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
//                appid, secret);
//        String access_token = HttpUtil.get(url);
//        return access_token;
//    }
//
//}
