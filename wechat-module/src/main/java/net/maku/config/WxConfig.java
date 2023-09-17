package net.maku.config;/**
 * Author: CHAI
 * Date: 2023/8/31
 */

import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpStoreService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpStoreServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-08-31 21:08
 **/
@Configuration
public class WxConfig {

    @Autowired
    private WxAccountConfig wxAccountConfig;

    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
//        wxMpService.setStoreService();
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        // 在这里我们要设置appid 和 appsecret 需要在配置文件里面设置两个变量，这样全局都可以用
        // 然后设置一个WexAccountConfig类，来注入这两个参数，这样在使用的时候就可以直接调用这两个类
        wxMpConfigStorage.setAppId(wxAccountConfig.getAppId());
        wxMpConfigStorage.setSecret(wxAccountConfig.getAppSecret());
        wxMpConfigStorage.setToken(wxAccountConfig.getToken());
        return wxMpConfigStorage;
    }

}

