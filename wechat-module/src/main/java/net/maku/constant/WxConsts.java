package net.maku.constant;

/**
 * 发送消息的类型
 * Author: CHAI
 * Date: 2023/9/13
 */
public class WxConsts {

    public static class XmlMsgType {
        /**
         * 文本消息
         */
        public static final String TEXT = "text";

        /**
         * 图片消息
         */
        public static final String IMAGE = "image";

        /**
         * 语音消息
         */
        public static final String VOICE = "voice";

        /**
         * 视频消息
         */
        public static final String VIDEO = "video";

        /**
         * 音乐消息
         */
        public static final String MUSIC = "music";

        /**
         * 图文消息
         */
        public static final String NEWS = "news";
    }

    public static class EventType {
        /**
         * 关注/取消关注消息
         */
        public static final String EVENT = "event";

        /**
         * 订阅
         */
        public static final String SUBSCRIBE = "subscribe";

        /**
         * 取消订阅
         */
        public static final String UNSUBSCRIBE = "unsubscribe";
    }

    public static class MenuButtonType {
        public static final String CLICK = "click";
        public static final String VIEW = "view";
    }

    public static class OAuth2Scope {
        /**
         * 弹出授权页面，可通过openid拿到昵称、性别、所在地。并且， 即使在未关注的情况下，只要用户授权，也能获取其信息
         * */
        public static final String SNSAPI_USERINFO = "snsapi_userinfo";
        /**
         * 不弹出授权页面，直接跳转，只能获取用户openid
         * */
        public static final String SNSAPI_BASE = "snsapi_base";
    }
}
