package net.maku.config;/**
 * Author: CHAI
 * Date: 2023/9/16
 */

import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import net.maku.constant.WxConsts;
import net.maku.handler.ImageHandler;
import net.maku.handler.SubscribeHandler;
import net.maku.handler.TextHandler;
import net.maku.handler.UnSubscribeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置回复消息的路由配置
 * @program: onePiece
 * @author:
 * @create: 2023-09-16 14:39
 **/
@Configuration
public class WxJavaConfig {
    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private TextHandler textHandler;

    @Autowired
    private ImageHandler imageHandler;

    @Autowired
    private SubscribeHandler subscribeHandler;

    @Autowired
    private UnSubscribeHandler unSubscribeHandler;

    @Bean
    public WxMpMessageRouter messageRouter() {
        // 创建消息路由
        final WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
        // 添加文本消息路由
        router.rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(textHandler).end();
        // 添加图片消息路由
        router.rule().async(false).msgType(WxConsts.XmlMsgType.IMAGE).handler(imageHandler).end();
        router.rule().async(false).msgType(WxConsts.XmlMsgType.IMAGE).handler(imageHandler).end();
        router.rule().async(false).msgType(WxConsts.EventType.EVENT).event(WxConsts.EventType.SUBSCRIBE).handler(subscribeHandler).end();
        router.rule().async(false).msgType(WxConsts.EventType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE).handler(unSubscribeHandler).end();
        return router;
    }
}

