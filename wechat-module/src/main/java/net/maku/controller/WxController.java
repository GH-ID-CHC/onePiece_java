package net.maku.controller;/**
 * Author: CHAI
 * Date: 2023/9/11
 */

import com.thoughtworks.xstream.XStream;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.service.WxOAuth2Service;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import net.maku.constant.WxConsts;
import net.maku.constant.WxConfigConstant;
import net.maku.moudle.ArticlesMessage;
import net.maku.moudle.ItemMessage;
import net.maku.moudle.TextMessage;
import net.maku.utils.VerficationCodeUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Slf4j
public class WxController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;

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
//    @GetMapping("/verify")
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

    @GetMapping("/verify")
    public String configAccess(String signature,String timestamp,String nonce,String echostr) {
        // 校验签名
        if (wxMpService.checkSignature(timestamp, nonce, signature)){
            // 校验成功原样返回echostr
            return echostr;
        }
        // 校验失败
        return null;
    }


    /**
     * 接收用户的普通消息
     * @param request
     * @return {@code Map<String, String>}
     */
//    @PostMapping("/verify")
    public String verify(HttpServletRequest request){
        Map<String, String> msgMap = new HashMap<>();
        try {
            ServletInputStream inputStream = request.getInputStream();
            // dom4j 用于读取 XML 文件输入流的类
            SAXReader saxReader = new SAXReader();
            // 读取 XML 文件输入流, XML 文档对象
            Document document = saxReader.read(inputStream);
            // XML 文件的根节点
            Element root = document.getRootElement();
            // 所有的子节点
            List<Element> childrenElement = root.elements();
            for (Element element : childrenElement) {
                msgMap.put(element.getName(), element.getStringValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("收到消息"+msgMap);
        String replyMessage=null;
        if ("图文".equals(msgMap.get("Content"))){
            replyMessage=getPictographMessage(msgMap);
        }else {
            replyMessage = getReplyMessage(msgMap);
        }
        return replyMessage;
    }

    @PostMapping(value = "/verify", produces = "application/xml; charset=UTF-8")
    public String handleMessage(@RequestBody String requestBody,
                                @RequestParam("signature") String signature,
                                @RequestParam("timestamp") String timestamp,
                                @RequestParam("nonce") String nonce) {
        log.info("handleMessage调用");
        // 校验消息是否来自微信
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
        }
        // 解析消息体，封装为对象
        WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
        WxMpXmlOutMessage outMessage;
        try {
            // 将消息路由给对应的处理器，获取响应
            outMessage = wxMpMessageRouter.route(inMessage);
//            wxMpService.oauth2buildAuthorizationUrl("http://daud7r.natappfree.cc/wx/callback",WxConsts.OAuth2Scope.SNSAPI_USERINFO,null);
//            WxMpUser wxMpUser = wxMpService.getUserService().userInfo(inMessage.getFromUser());
//            System.out.println("==>"+wxMpUser.getHeadImgUrl());
        } catch (Exception e) {
            log.error("微信消息路由异常", e);
            outMessage = null;
        }
        // 将响应消息转换为xml格式返回
        return outMessage == null ? "" : outMessage.toXml();
    }


    /**
     * 自定义菜单
     * @return {@code String}
     * @throws WxErrorException
     */
    @GetMapping("/createMenu")
    public String createMenu() throws WxErrorException {
        // 创建菜单对象
        WxMenu menu = new WxMenu();
        // 创建按钮1
        WxMenuButton button1 = new WxMenuButton();
        button1.setType(WxConsts.MenuButtonType.CLICK);
        button1.setName("今日歌曲");
        button1.setKey("V1001_TODAY_MUSIC");
        // 创建按钮2
        WxMenuButton button2 = new WxMenuButton();
        button2.setName("菜单");
        // 创建按钮2的子按钮1
        WxMenuButton button21 = new WxMenuButton();
        button21.setType(WxConsts.MenuButtonType.VIEW);
        button21.setName("搜索");
        button21.setUrl("https://www.baidu.com/");
        // 创建按钮2的子按钮2
        WxMenuButton button22 = new WxMenuButton();
        button22.setType(WxConsts.MenuButtonType.VIEW);
        button22.setName("视频");
        button22.setUrl("https://v.qq.com/");
        // 创建按钮2的子按钮3
        WxMenuButton button23 = new WxMenuButton();
        button23.setType(WxConsts.MenuButtonType.CLICK);
        button23.setName("赞一下我们");
        button23.setKey("V1001_GOOD");
        // 将子按钮添加到按钮2
        button2.getSubButtons().add(button21);
        button2.getSubButtons().add(button22);
        button2.getSubButtons().add(button23);
        // 将按钮1和你按钮2添加到菜单
        menu.getButtons().add(button1);
        menu.getButtons().add(button2);
        // 创建按钮
        return wxMpService.getMenuService().menuCreate(menu);
    }

    /**
     * 发送模板消息
     * @return {@code String}
     * @throws WxErrorException
     */
    @GetMapping("/sendTemplateMessage")
    public String sendTemplateMessage() throws WxErrorException {
        log.info(wxMpConfigStorage.getAccessToken());
        // 创建模板消息，设置模板id、指定模板消息要发送的目标用户
        WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                .templateId("rUJkadf0wFDE0u9GGYdOL4uQfcB75gErEX8s9Iubzs0")
                .toUser("oZFId6902xln3WO-VCfgd8fhHqOg")
                .build();
        // 填充模板消息中的变量
        String code = VerficationCodeUtils.getVerficationCode(6);
        wxMpTemplateMessage.addData(new WxMpTemplateData("code",code));
        wxMpTemplateMessage.addData(new WxMpTemplateData("validity","5"));
        String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        // 发送模板消息，返回消息id
        log.info(wxMpConfigStorage.getAccessToken());
        return msgId;
    }

    /**
     * 网页授权
     * @return {@code String}
     */
    @GetMapping("/buildAuthPage")
    public String auth() {
        WxOAuth2Service oAuth2Service = wxMpService.getOAuth2Service();
        // 构建授权url
        return oAuth2Service.buildAuthorizationUrl("http://gcf6gj.natappfree.cc/wx/callback",
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, null);
    }


    @GetMapping("/callback")
    public WxOAuth2UserInfo callback(String code) throws WxErrorException {
        WxOAuth2Service oAuth2Service = wxMpService.getOAuth2Service();
        // 利用code获取accessToken
        WxOAuth2AccessToken accessToken = oAuth2Service.getAccessToken(code);
        // 利用accessToken获取用户信息
        WxOAuth2UserInfo userInfo = oAuth2Service.getUserInfo(accessToken, null);
        return userInfo;
    }

    /**
     * 获取回复图文消息内容
     * @param msgMap
     * @return {@code String(xml类型)}
     */
    private String getPictographMessage(Map<String, String> msgMap) {
        ArticlesMessage articlesMessage = new ArticlesMessage();
        articlesMessage.setToUserName(msgMap.get("FromUserName"));
        articlesMessage.setFromUserName(msgMap.get("ToUserName"));
        articlesMessage.setCreateTime(new Date().toString());
        articlesMessage.setMsgType(WxConsts.XmlMsgType.NEWS);
        articlesMessage.setArticleCount("1");
        ItemMessage itemMessage = new ItemMessage();
        itemMessage.setTitle("路飞五档");
        itemMessage.setDescription("最高顶点！最强形态！");
        itemMessage.setPicUrl("http://mmbiz.qpic.cn/sz_mmbiz_jpg/XTDxMhlYfPgvlPibZ6JfpOc4AWQfAxGOV2AkowLQniaTbbdHpntMVOPwIce88Ww1FZrJInSiaRc1kdico2TwoDvbFQ/0");
        itemMessage.setUrl("http://www.onepiece.com");
        List<ItemMessage> itemMessages = new ArrayList<>();
        itemMessages.add(itemMessage);
        articlesMessage.setArticles(itemMessages);
        XStream xStream = new XStream();
        xStream.processAnnotations(ArticlesMessage.class);
        String xmlStr = xStream.toXML(articlesMessage);
        System.out.println(xmlStr);
        return xmlStr;
    }

    /**
     * 获取回复文本消息内容
     * @param msgMap
     * @return {@code String(xml类型)}
     */
    private String getReplyMessage(Map<String, String> msgMap) {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(msgMap.get("FromUserName"));
        textMessage.setFromUserName(msgMap.get("ToUserName"));
        textMessage.setCreateTime(new Date().toString());
        textMessage.setMsgType(WxConsts.XmlMsgType.TEXT);
        textMessage.setContent("你好");

        //将对象转换为xml字符串
        XStream xStream = new XStream();
        xStream.processAnnotations(TextMessage.class);
        String xmlStr = xStream.toXML(textMessage);
        System.out.println(xmlStr);
        return xmlStr;
    }
}
