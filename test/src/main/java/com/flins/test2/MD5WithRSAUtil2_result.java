package com.flins.test2;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
public class MD5WithRSAUtil2_result {
	//private static final Logger logger = LoggerFactory.getLogger(MD5withRSA.class);

   /**
    * 
    * @Title: createMD5Sign
    * @Description: 用md5生成内容摘要，再用RSA的私钥加密，进而生成数字签名
    * @param content
    * @param privateKeyStr
    * @return
    * @throws Exception    参数
    * @throws
    */
    public static String createMD5Sign(String content , String privateKeyStr) throws Exception {
    	PrivateKey privateKey = getPrivateKeyByStr(privateKeyStr);
    	byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(privateKey);
        signature.update(contentBytes);
        byte[] signs = signature.sign();
        return Base64.encodeBase64String(signs);
    }

    
    
    /**
     * 
     * @Title: verifyMD5Sign
     * @Description: 对用md5和RSA私钥生成的数字签名进行验证
     * @param content
     * @param sign
     * @param publicKeyStr
     * @return
     * @throws Exception    参数
     * @throws
     */
    public static boolean verifyMD5Sign(String content, String sign, String publicKeyStr) throws Exception {
//    	System.out.println(String.format("content...%s   sign...%s  publicKey...%s", content, sign, publicKeyStr));
    	PublicKey publicKey = getPublicKeyByStr(publicKeyStr);
    	byte[] contentBytes = content.getBytes("utf-8");
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey);
        signature.update(contentBytes);
        return signature.verify(Base64.decodeBase64(sign));
    }

    /**
     * 
     * @Title: getPrivateKeyByStr
     * @Description: 字符串转PrivateKey
     * @param privateKey
     * @return
     * @throws Exception    参数
     * @throws
     */
    public static PrivateKey getPrivateKeyByStr(String privateKey) throws Exception {
        byte[] keyBytes;
        keyBytes = Base64.decodeBase64(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
	}
	
    /**
     * 
     * @Title: getPublicKeyByStr
     * @Description: 字符串转PublicKey
     * @param publicKey
     * @return
     * @throws Exception    参数
     * @throws
     */
	public static PublicKey getPublicKeyByStr(String publicKey) throws Exception {
        byte[] keyBytes;
        keyBytes =Base64.decodeBase64(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
	}
    
	/**
	 * 
	 * @Title: encryptRSA
	 * @Description: 公钥加密
	 * @param content
	 * @param publicKey
	 * @return
	 * @throws Exception    参数
	 * @throws
	 */
	public static String encryptRSA(String content, PublicKey publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] bytes = cipher.doFinal(content.getBytes("utf-8"));
		return Base64.encodeBase64String(bytes);
	}
	
	/**
	 * 
	 * @Title: decryptRSA
	 * @Description: 私钥解密
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception    参数
	 * @throws
	 */
	public static String decryptRSA(String content, PrivateKey privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] bytes = cipher.doFinal(Base64.decodeBase64(content));
		return new String(bytes,"utf-8");
	}

   /**
    * 
    * @Title: createKeyPair
    * @Description: 生成密钥对
    * @return
    * @throws Exception    参数
    * @throws
    */
	public static KeyPair createKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        KeyPair keyPair = keyGen.generateKeyPair();
        return keyPair;
    }
	
	public static String select() throws Exception {
		String privateKey = "MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAmQREzXAYmWcIjPyeNCV6SmVLX8XcrZWonh8i5XdSiqs/Pcj4Hxrh6DXbbOJzTVAkUXWbaWKcizydm7PAsNwauQIDAQABAkA9Cy/riTm3ERyfKjRu9ziXL1vnhEbqVRckpaVFR9n9CYA21m++U4I8MyGeNv0NV6GEeATmnsx4xhdnXzD6pfZRAiEAzUCuMD9vYtpXWQHNFDJkgMJKGB8r307CkgYvZ0azlg0CIQC+2Vlr3+mHsXP1qVKJZEChgwhyGXbzhlayLsS5qsr4XQIgVNbEaTr/PnwDmM3Ox+dgdXxZhgj0aHQ0pokZ9PVba80CICKf6DtMCmBV74XYTGMkzxz+Wt/ogs43xy7YkVRmCiD5AiEAxsurHTXqhpBy/2V7qUz6RKDzVM+1Qeu/KTfb3XX+fuw=";
		String pubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJkERM1wGJlnCIz8njQlekplS1/F3K2VqJ4fIuV3UoqrPz3I+B8a4eg122zic01QJFF1m2linIs8nZuzwLDcGrkCAwEAAQ==";		
		String content = " {\r\n" + 
				"		\"qaQuestList\": [{\r\n" + 
				"				\"code\": \"QL-100001\",\r\n" + 
				"				\"question\": \"您的性别？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 1,\r\n" + 
				"				\"answer\": \"A\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10001\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100003\",\r\n" + 
				"				\"question\": \"您的身高＿＿＿（厘米cm）\",\r\n" + 
				"				\"questType\": 3,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 3,\r\n" + 
				"				\"answer\": \"168\",\r\n" + 
				"				\"inputType\": 1,\r\n" + 
				"				\"id\": 10003\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100004\",\r\n" + 
				"				\"question\": \"体重＿＿＿（公斤kg）\",\r\n" + 
				"				\"questType\": 3,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 4,\r\n" + 
				"				\"answer\": \"60.98\",\r\n" + 
				"				\"inputType\": 2,\r\n" + 
				"				\"id\": 10004\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100005\",\r\n" + 
				"				\"question\": \"您生活的城市？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 5,\r\n" + 
				"				\"answer\": \"C\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10005\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100006\",\r\n" + 
				"				\"question\": \"您的文化程度？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 6,\r\n" + 
				"				\"answer\": \"C\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10006\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100007\",\r\n" + 
				"				\"question\": \"您以前有得过甲状腺疾病吗？\",\r\n" + 
				"				\"questType\": 2,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 7,\r\n" + 
				"				\"answer\": \"B\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10007\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100008\",\r\n" + 
				"				\"question\": \"您的亲属中有没有患甲状腺癌的？\",\r\n" + 
				"				\"questType\": 2,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 8,\r\n" + 
				"				\"answer\": \"B\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10008\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100009\",\r\n" + 
				"				\"question\": \"您是否经常（每周≥3次）吃以下食物？\",\r\n" + 
				"				\"questType\": 2,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 9,\r\n" + 
				"				\"answer\": \"A,B,C\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10009\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100010\",\r\n" + 
				"				\"question\": \"您是否有糖尿病或者血糖异常？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 10,\r\n" + 
				"				\"answer\": \"C\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10010\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100011\",\r\n" + 
				"				\"question\": \"您是否有血脂高（甘油三脂、胆固醇）？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 11,\r\n" + 
				"				\"answer\": \"B\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10011\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100012\",\r\n" + 
				"				\"question\": \"您曾经有没有接受过头颈部CT检查？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 12,\r\n" + 
				"				\"answer\": \"A\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10012\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100013\",\r\n" + 
				"				\"question\": \"您是否常常有下列情绪？\",\r\n" + 
				"				\"questType\": 2,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 13,\r\n" + 
				"				\"answer\": \"E\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10013\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100014\",\r\n" + 
				"				\"question\": \"在平常的生活、工作中您有没有感觉到压力大、焦虑？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 14,\r\n" + 
				"				\"answer\": \"B\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10014\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100015\",\r\n" + 
				"				\"question\": \"您与他人的相处的时候，通常是什么样的状态？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 15,\r\n" + 
				"				\"answer\": \"B\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10015\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100016\",\r\n" + 
				"				\"question\": \"您是否曾经怀孕次数达3次以上？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 16,\r\n" + 
				"				\"answer\": \"A\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10016\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"code\": \"QL-100018\",\r\n" + 
				"				\"question\": \"您是否患有乳腺增生？\",\r\n" + 
				"				\"questType\": 1,\r\n" + 
				"				\"epId\": 100,\r\n" + 
				"				\"qpId\": 100,\r\n" + 
				"				\"sortNo\": 18,\r\n" + 
				"				\"answer\": \"A\",\r\n" + 
				"				\"inputType\": 3,\r\n" + 
				"				\"id\": 10018\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}";
		System.out.println(content);
		content = JSON.toJSONString(JSON.parseObject(content, JSONObject.class), SerializerFeature.MapSortField);
		
		String sign = createMD5Sign(content, privateKey);
		System.out.println("sign:"+sign);
		System.out.println("verify sign result :" + verifyMD5Sign(content, sign, pubKey));
		return sign;
	}
	
	
	public static void main(String[] args) throws Exception {
		String id = "PFC20190617000024";
		MD5WithRSAUtil2_result.select();
	}

}
