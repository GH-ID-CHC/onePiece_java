package net.maku.moudle;
/**
 * Author: CHAI
 * Date: 2023/9/13
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.awt.*;

/**
 * 图片文本
 * @program: onePiece
 * @author:
 * @create: 2023-09-13 22:50
 **/
@Data
@XStreamAlias("xml")
public class ImgMessage extends BaseMessage{

    @XStreamAlias("Image")
    private Image image;
}
