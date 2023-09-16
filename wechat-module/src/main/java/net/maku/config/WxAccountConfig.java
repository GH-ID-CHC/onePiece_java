package net.maku.config;

/**
 * Author: CHAI
 * Date: 2023/8/31
 */

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 微信账号配置类
 * ConfigurationProperties:将会读取properties文件中所有以wx开头的属性，并和bean中的字段进行匹配：
 * @program: onePiece
 * @author:
 * @create: 2023-08-31 21:05
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "wx")
public class WxAccountConfig {

    // 公众号ID
    private String appId;

    // 公众号secret
    private String appSecret;

    //公众号token
    private String token;

}

