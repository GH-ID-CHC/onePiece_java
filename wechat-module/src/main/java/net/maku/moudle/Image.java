package net.maku.moudle;/**
 * Author: CHAI
 * Date: 2023/9/13
 */

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-13 22:52
 **/
@Data
@XStreamAlias("xml")
public class Image {

    /**
     * 通过素材管理中的接口上传多媒体文件，得到的id。
     */
    @XStreamAlias("MediaId")
    private String mediaId;
}
