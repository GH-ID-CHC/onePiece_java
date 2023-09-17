//package net.maku.controller;/**
// * Author: CHAI
// * Date: 2023/8/31
// */
//
//import net.maku.service.WxMessageService;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 微信公众号
// * @program: onePiece
// * @author:
// * @create: 2023-08-31 21:09
// **/
//@Tag(name="微信公众号")
//@RestController
//@RequestMapping("/wx-gzh")
//public class WxMessageController {
//
//    @Autowired
//    private WxMessageService wxMessageService;
//
//    @Operation(summary = "获取公众号验证码")
//    @PostMapping(value = "/sendVertficationCode", produces = { "application/json;charset=utf-8" })
//    public String sendVertficationCode(HttpServletRequest request, @RequestParam(required = true) String echostr,
//                                       @RequestParam String userId) {
////      userId = o3FqD1sJQdv0oQz_dEPvbgk3AFbE;
//        wxMessageService.returnVerficationCode(userId);
//        return echostr;
//    }
//}
