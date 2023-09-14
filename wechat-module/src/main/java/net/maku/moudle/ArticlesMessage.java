package net.maku.moudle;/**
 * Author: CHAI
 * Date: 2023/9/14
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.List;

/**
 * 图文消息
 * @program: onePiece
 * @author:
 * @create: 2023-09-14 22:34
 **/
@Data
@XStreamAlias("xml")
public class ArticlesMessage extends BaseMessage {

    @XStreamAlias("ArticleCount")
    private String articleCount;

    @XStreamAlias("Articles")
    private List<ItemMessage> articles;
}
