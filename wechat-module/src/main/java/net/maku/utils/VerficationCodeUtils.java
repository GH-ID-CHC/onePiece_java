package net.maku.utils;
/**
 * Author: CHAI
 * Date: 2023/8/31
 */

import java.security.SecureRandom;
import java.util.Random;

/**
 * 获取验证码的工具类
 * @program: onePiece
 * @author:
 * @create: 2023-08-31 21:17
 **/

public class VerficationCodeUtils {

    private static final String SYMBOLS = "0123456789"; // 数字
    private static final Random RANDOM = new SecureRandom();

    /**
     * 生成指定位数的数字验证码
     *
     * @return
     */
    public static String getVerficationCode(int length) {

        // 如果需要4位，那 new char[4] 即可，其他位数同理可得
        char[] nonceChars = new char[length];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }
}


