package com.flins.test3;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.util.json.JSONParser;

import com.alibaba.fastjson.JSONObject;
import com.flins.test3.All_util;


public class Auto_business {

	private static HttpClient httpClient = new DefaultHttpClient();
    private static HttpPost httppost;
    private static HttpResponse response;
    private HttpEntity entity;
    private String postResult = null;
    public static String baseUrl = "http://192.168.5.111";
    public static String benefitCode="cpbm"+All_util.generateNum(3);
    public static String productName="cpmc"+All_util.generateString(3);
    
    //发起甲状腺测评请求
    public String dispatch(int mer_id) throws Exception {

		//bzid
    	String bzId = All_util.generateString(10);
    	//guId
    	String guId = All_util.generateNum(10);
    	//channelCode
    	String num = All_util.generateNum(5);
    	String channelCode = "channelCode"+num;
    	String channelName = "channelName"+num;
    	benefitCode = "productCode"+num;
    	String productName = "productName"+num;
    	//appId
    	OperateMysql o = new OperateMysql();
    	String appId = o.getAppID(mer_id);
    	Map<String,String> keys = o.getSecretKey(mer_id);
    	String appKey = keys.get("key");
    	String apiCode = "thyroidEvaluate";
    	String public_key = keys.get("public");
    	String private_key = keys.get("private");
    	//sign
    	String data2 = "{\r\n" + 
				"		\"bzId\": \""+bzId+"\",\r\n" + 
				"		\"guId\": \""+guId+"\",\r\n" + 
				"		\"gender\":1,\r\n" + 
				"		\"channelCode\": \""+channelCode+"\",\r\n" + 
				"		\"channelName\": \""+channelName+"\",\r\n" + 
				"		\"benefitCode\": \""+benefitCode+"\",\r\n" + 
				"		\"productName\": \""+productName+"\",\r\n" + 
				"		\"cpCode\": \"cpCode\",\r\n" + 
				"		\"cpName\": \"cpName\",\r\n" + 
				"		\"planCode\": \"planCode\",\r\n" + 
				"		\"successUrl\": \"http://flins.com.cn\",\r\n" + 
				"		\"failUrl\": \"http://www.baidu.com\"\r\n" + 
				"	}";
		String sign = MD5WithRSAUtil1.type_1(private_key,public_key,data2);
   
		String loginURL = baseUrl+"/business/api/dispatch";
        httppost = new HttpPost(loginURL);      
        JSONObject data = new JSONObject();
        data.put("bzId", bzId);
        data.put("guId", guId);
        data.put("gender", "1");
        data.put("channelCode", channelCode);
        data.put("channelName", channelName);
        data.put("benefitCode", benefitCode);
        data.put("productName", productName);
        data.put("cpCode", "cpCode");
        data.put("cpName", "cpName");
        data.put("planCode", "planCode");
        data.put("successUrl", "http://flins.com.cn");
        data.put("failUrl", "http://www.baidu.com");
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("appId", appId);
        jsonParam.put("appKey",appKey);
        jsonParam.put("apiCode",apiCode);
        jsonParam.put("version","1.0");
        jsonParam.put("data",data);
        jsonParam.put("sign",sign);  
        String strResult =null;
        try {
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json;charset=UTF-8");
            httppost.setEntity(entity);
            response = httpClient.execute(httppost); 
            strResult = EntityUtils.toString(response.getEntity());
            System.out.println(strResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strResult;
	}
    
    //获取七牛token
    public String qiNiuToken(String token) throws Exception {
    	String url = baseUrl+"/business/zb/getQiNiuToken";
    	httppost = new HttpPost(url); 
    	StringEntity entity = new StringEntity("utf-8");
        entity.setContentEncoding("UTF-8");
        entity.setContentType("application/json;charset=UTF-8");
        httppost.setEntity(entity);
        httppost.addHeader("token",token);
        response = httpClient.execute(httppost); 
        String strResult = EntityUtils.toString(response.getEntity());
        System.out.println(strResult);
        return strResult;
    }
    
    //上传图片流
    public String upload(String qiniuToken) throws Exception {
    	//解决ssl域名不匹配问题
//    	SSLSocketFactory.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
    	String base = All_util.base64("d://images/TR6.jpg");
    	String url = "http://upload-z2.qiniup.com/putb64/-1";
    	httppost = new HttpPost(url);
    	StringEntity entity = new StringEntity(base,"utf-8");
    	entity.setContentEncoding("UTF-8");
    	entity.setContentType("application/octet-stream");
    	httppost.setEntity(entity);
    	httppost.addHeader("Authorization","UpToken "+qiniuToken);
    	response = httpClient.execute(httppost);
    	String strResult = EntityUtils.toString(response.getEntity());
        return strResult;
    }
    
    //提交图片
    public String submit(String imgUrl,String token) throws Exception {
    	String url = baseUrl+"/business/zb/imgupload";
    	
    	//参数
    	JSONObject data = new JSONObject();
    	data.put("imgUrl", imgUrl);
    	
    	//发送post请求
    	httppost = new HttpPost(url);
    	StringEntity entity = new StringEntity(data.toString(),"utf-8");
    	entity.setContentEncoding("UTF-8");
    	entity.setContentType("application/json;charset=UTF-8");
    	httppost.addHeader("token",token);
    	
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(2000).build();
    	
    	httppost.setConfig(requestConfig);
    	httppost.setEntity(entity);
    	response = httpClient.execute(httppost);
    	String strResult = EntityUtils.toString(response.getEntity());
    	System.out.println(strResult);
        return strResult;
    	}
    
    //前端查询测评结果
    public String getResult(String ser,String token) throws Exception {
    	String url = baseUrl+"/business/zb/getresult";
    	
    	//参数
    	JSONObject data = new JSONObject();
    	data.put("serialNumber", ser);
    	
    	//请求
    	httppost = new HttpPost(url);
    	StringEntity entity = new StringEntity(data.toString(),"utf-8");
    	entity.setContentEncoding("UTF-8");
    	entity.setContentType("application/json;charset=UTF-8");
    	httppost.addHeader("token",token);
    	
    	CloseableHttpClient httpClient = HttpClients.createDefault();
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(2000).build();
    	
    	httppost.setConfig(requestConfig);
    	httppost.setEntity(entity);
    	response = httpClient.execute(httppost);
    	String strResult = EntityUtils.toString(response.getEntity());
    	System.out.println(strResult);
        return strResult;	
    }
    
    //查询测评单信息
    public String dispatch_result(String bzId,int mer_id) throws Exception {
    	String url = baseUrl+"/business/api/dispatch";
    	
    	//参数
    	JSONObject data = new JSONObject();
    	data.put("bzId", bzId);
    	//appId
    	OperateMysql o = new OperateMysql();
    	String appId = o.getAppID(mer_id);
    	Map<String,String> keys = o.getSecretKey(mer_id);
    	String appKey = keys.get("key");
    	String apiCode = "evaluateInfo";
    	String public_key = keys.get("public");
    	String private_key = keys.get("private");
    	//sign
    	String datas = "{\r\n" + 
    			"	\"bzId\": \""+bzId+"\"\r\n" + 
    			"}";
		String sign = MD5WithRSAUtil1.type_1(private_key,public_key,datas);
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("appId", appId);
        jsonParam.put("appKey",appKey);
        jsonParam.put("apiCode",apiCode);
        jsonParam.put("version","1.0");
        jsonParam.put("data",data);
        jsonParam.put("sign",sign); 
        
        //发送post
        httppost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(2000).build();
    	httppost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json;charset=UTF-8");
        httppost.setEntity(entity);
        System.out.println(jsonParam);
        response = httpClient.execute(httppost);
        String strResult = EntityUtils.toString(response.getEntity());
        return strResult;
    }
    
    //反馈承保数据
    public String feedback(String bzId,String ser,int mer_id) throws Exception {
    	String url = baseUrl+"/business/api/dispatch";
    	String ins = "BDH2019"+All_util.generateNum(5);
    	JSONObject data = new JSONObject();
    	data.put("bzId", bzId);
    	data.put("serialNumber", ser);
    	data.put("benefitCode", benefitCode);
    	data.put("cpCode", "cpCode");
    	data.put("insuranceNo", ins);
    	data.put("premium", 5000);
    	data.put("cpName", "cpName");
    	data.put("productName", productName);
    	data.put("insuranceStatus", "正常");
    	data.put("cname", "cname");
    	data.put("idNumber", "idNumber");
    	data.put("insuranceTime", "2019-8-29 08:08:00");
    	data.put("isCancel", "false");
    	data.put("cancelTime", "");
    	
    	//appId
    	OperateMysql o = new OperateMysql();
    	String appId = o.getAppID(mer_id);
    	Map<String,String> keys = o.getSecretKey(mer_id);
    	String appKey = keys.get("key");
    	String apiCode = "feedbackInsurance";
    	String public_key = keys.get("public");
    	String private_key = keys.get("private");
    	//sign
    	String datas = "{\r\n" + 
    			"        \"bzId\":\""+bzId+"\",\r\n" + 
    			"        \"serialNumber\":\""+ser+"\",\r\n" + 
    			"        \"benefitCode\":\""+benefitCode+"\",\r\n" + 
    			"        \"cpCode\":\"cpCode\",\r\n" + 
    			"        \"insuranceNo\":\""+ins+"\",\r\n" + 
    			"        \"premium\":\"5000\",\r\n" + 
    			"        \"cpName\":\"cpName\",\r\n" + 
    			"        \"productName\":\""+productName+"\",\r\n" + 
    			"        \"insuranceStatus\":\"正常\" ,\r\n" + 
    			"        \"cname\":\"cname\",\r\n" + 
    			"        \"idNumber\":\"idNumber\",\r\n" + 
    			"        \"insuranceTime\":\"2019-8-29 08:08:00\",\r\n" + 
    			"        \"isCancel\":\"false\",\r\n" + 
    			"        \"cancelTime\":\"\"\r\n" + 
    			"      }";
		String sign = MD5WithRSAUtil1.type_1(private_key,public_key,datas);
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("appId", appId);
        jsonParam.put("appKey",appKey);
        jsonParam.put("apiCode",apiCode);
        jsonParam.put("version","1.0");
        jsonParam.put("data",data);
        jsonParam.put("sign",sign); 
        
      //发送post
        httppost = new HttpPost(url);
        CloseableHttpClient httpClient = HttpClients.createDefault();
    	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(2000).build();
    	httppost.setConfig(requestConfig);
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json;charset=UTF-8");
        httppost.setEntity(entity);
        System.out.println(jsonParam);
        response = httpClient.execute(httppost);
        String strResult = EntityUtils.toString(response.getEntity());
        return strResult;
    }
    
    public static void main(String[] args) throws Exception {
    	int mer_id = 28;
		Auto_business a = new Auto_business();
		String response_data = a.dispatch(mer_id);
		JSONObject object = JSONObject.parseObject(response_data);
		JSONObject data = object.getJSONObject("data");
		String s = data.getString("redirectUrl");
		int len = s.length();
		String token = s.substring(len-32, len);
		System.out.println(token);
		
		String qiniu = a.qiNiuToken(token);
		JSONObject response2 = JSONObject.parseObject(qiniu);
		JSONObject qiniudata = response2.getJSONObject("data");
		String qiniuToken = qiniudata.getString("qiNiuToken");
		System.out.println(qiniuToken);
		
		String upload_data = a.upload(qiniuToken);
		JSONObject response_upload = JSONObject.parseObject(upload_data);
		String key = response_upload.getString("key");
		System.out.println(key);
		
		String submit_data = a.submit(key,token);
		JSONObject response_submit = JSONObject.parseObject(submit_data);
		JSONObject data_ser = response_submit.getJSONObject("data");
		String serialNumber = data_ser.getString("serialNumber");
		System.out.println(serialNumber);
		
		String getResult = a.getResult(serialNumber, token);
		JSONObject response_getResult = JSONObject.parseObject(getResult);
		JSONObject data_getResult = response_getResult.getJSONObject("data");
		String bzId = data_getResult.getString("bzId");
		System.out.println(bzId);
		
		System.out.println(a.dispatch_result(bzId, mer_id));
		
		String feedback = a.feedback(bzId, serialNumber, mer_id);
		System.out.println(feedback);
		
		
	}
}
