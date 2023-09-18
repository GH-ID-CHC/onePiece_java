package com.onepiece.chai.utils;/**
 * Author: CHAI
 * Date: 2023/9/18
 */

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-18 22:13
 **/
public class FileUtil
{
    public static void buildTestFile(String fileName, long fileSize) throws IOException
    {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fileName)))
        {
            final int buffSize = 1024;
            byte[] tmpBuf = new byte[buffSize];
            long byteWriten = 0;
            while (byteWriten < fileSize)
            {
                ThreadLocalRandom.current().nextBytes(tmpBuf);
                long maxWriteLen = Math.min(buffSize, fileSize - byteWriten);
                bos.write(tmpBuf, 0, new Long(maxWriteLen).intValue());
                byteWriten += maxWriteLen;
            }
        }
    }
}
