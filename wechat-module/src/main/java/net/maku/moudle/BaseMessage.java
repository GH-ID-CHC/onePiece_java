package net.maku.moudle;/**
 * Author: CHAI
 * Date: 2023/9/13
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 返回消息的公共类
 * @program: onePiece
 * @author:
 * @create: 2023-09-13 22:46
 **/
@Data
@XStreamAlias("xml")
public class BaseMessage {

    @XStreamAlias("ToUserName")
    private String toUserName;

    @XStreamAlias("FromUserName")
    private String fromUserName;

    @XStreamAlias("CreateTime")
    private String createTime;

    @XStreamAlias("MsgType")
    private String msgType;
}
