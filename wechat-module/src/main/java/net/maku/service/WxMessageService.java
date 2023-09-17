package net.maku.service;/**
 * Author: CHAI
 * Date: 2023/8/31
 */

import org.springframework.stereotype.Service;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-08-31 21:13
 **/

public interface WxMessageService {
    void returnVerficationCode(String receiveId);
}
