package net.maku.moudle;/**
 * Author: CHAI
 * Date: 2023/9/14
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-14 22:37
 **/
@Data
@XStreamAlias("item")
public class ItemMessage {

    @XStreamAlias("Title")
    private String title;

    @XStreamAlias("Description")
    private String description;

    @XStreamAlias("PicUrl")
    private String picUrl;

    @XStreamAlias("Url")
    private String url;
}
