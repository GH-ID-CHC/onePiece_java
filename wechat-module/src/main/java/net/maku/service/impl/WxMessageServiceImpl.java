package net.maku.service.impl;
/**
 * Author: CHAI
 * Date: 2023/8/31
 */

import me.chanjar.weixin.common.error.WxErrorException;
import net.maku.constant.WxConfigConstant;
import net.maku.utils.VerficationCodeUtils;
import net.maku.service.WxMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-08-31 21:14
 **/
@Service
@Slf4j
public class WxMessageServiceImpl implements WxMessageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private WxMpService wxMpService;

    @Override
    public void returnVerficationCode(String receiveId) {
        //模板消息封装的对象
        WxMpTemplateMessage wxMpTemplateMessage = new WxMpTemplateMessage();
        //消息模板ID
        wxMpTemplateMessage.setTemplateId(WxConfigConstant.VERFICATION_CODE_TEMPLATE_ID);
        wxMpTemplateMessage.setToUser(receiveId);
        wxMpTemplateMessage.setData(wrapperTemplateData());
        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
        }catch (WxErrorException errorException){
            logger.error("推送出现错误！" );
        }
    }

    /**
     *  得到验证码封装数据
     * @return
     */
    private List<WxMpTemplateData> wrapperTemplateData(){
        //得到4为验证码
        String code = VerficationCodeUtils.getVerficationCode(4);
        List<WxMpTemplateData> wxMpTemplateData = new ArrayList<>();
        wxMpTemplateData.add(new WxMpTemplateData("code",code));
        wxMpTemplateData.add(new WxMpTemplateData("validity",WxConfigConstant.VERFICATION_CODE_VALIDITY_TIME));
        return wxMpTemplateData;
    }


}
