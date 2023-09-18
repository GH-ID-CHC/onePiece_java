package com.onepiece.chai.utils;/**
 * Author: CHAI
 * Date: 2023/9/18
 */

/**
 * @program: onePiece
 * @author:
 * @create: 2023-09-18 22:14
 **/
public class QcloudDemo {
    public static void main(String[] args) {
        COSClient client = ClientUtil.getTestClient();
        List<Bucket> buckets = client.listBuckets();
        for (Bucket bucketElement : buckets) {
            String bucketName = bucketElement.getName();
            String bucketLocation = bucketElement.getLocation();
            System.out.println(bucketName + ":" + bucketLocation);
        }
        client.shutdown();
    }
}
