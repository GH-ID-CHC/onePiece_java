package net.maku.moudle;/**
 * Author: CHAI
 * Date: 2023/9/13
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * 文本消息
 * @program: onePiece
 * @author:
 * @create: 2023-09-13 22:49
 **/
@Data
@XStreamAlias("xml")
public class TextMessage extends BaseMessage{

    @XStreamAlias("Content")
    private String content;
}
