package com.onepiece.chai.controller;
/**
 * Author: CHAI
 * Date: 2023/9/18
 */

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;
import jakarta.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-18 22:11
 **/
@Controller
public class LoginController
{

    /**
     * @Description: QQ登录控制器
     * @Param:
     * @return:
     * @Author: 朝花不迟暮
     * @Date: 2021/2/25
     */
    @RequestMapping("/")
    public String login()
    {
        return "login";
    }

    /**
     * @Description: 请求QQ登录
     * @Param:
     * @return:
     * @Author: 朝花不迟暮
     * @Date: 2021/2/25
     */
    @RequestMapping("/loginByQQ")
    public void loginByQQ(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=utf-8");
        try
        {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
            System.out.println("请求QQ登录,开始跳转...");
        } catch (QQConnectException | IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * @Description: QQ登录的回调方法
     * @Param:
     * @return:
     * @Author: 朝花不迟暮
     * @Date: 2021/2/25
     */
    //@RequestMapping("/connect")
    public String connection(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) throws ServletException, IOException
    {
        try
        {
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            System.out.println(accessTokenObj);
            String accessToken = null,
                    openID = null;
            long tokenExpireIn = 0L;
            if (accessTokenObj.getAccessToken().equals(""))
            {
                System.out.print("没有获取到响应参数");
            } else
            {
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();
                OpenID openIDObj = new OpenID(accessToken);
                openID = openIDObj.getUserOpenID();
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                System.out.println("用户基本信息： "+userInfoBean);
                if (userInfoBean.getRet() == 0)
                {
                    String name = removeNonBmpUnicode(userInfoBean.getNickname());
                    String imgUrl = userInfoBean.getAvatar().getAvatarURL100();
                    map.put("openId", openID);
                    map.put("name", name);
                    map.put("imgUrl", imgUrl);
                } else
                {
                    System.out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return "index";
    }

    /**
     * @Description: 处理掉QQ网名中的特殊表情
     * @Param:
     * @return:
     * @Author: 朝花不迟暮
     * @Date: 2021/2/25
     */
    public String removeNonBmpUnicode(String str)
    {
        if (str == null)
        {
            return null;
        }
        str = str.replaceAll("[^\\u0000-\\uFFFF]", "");
        if ("".equals(str))
        {
            str = "($ _ $)";
        }
        return str;
    }
}
