package httprequest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class SdLoginAutoRequest {

    public static void main(String[] args) {
        //loginSd() ;
        int index = 1 ;
        while (loginSd()) {
            System.out.println("...................................................................>>>> LOGIN SUCCESS TIMES : " + index++);
        }
    }

    private static final int BYTE_LEN = 1024*100; // 100KB  
    private static final String CHARSET = "UTF-8";  // 编码格式  
    private static boolean loginSd() {
        HttpClient httpClient = null;  
        HttpPost httpPost = null;  
        try{  
            httpClient = HttpClients.createDefault(); 
//            httpPost = new HttpPost("http://shandong.temp.eascs.com/easd/loginAction.do");  
            httpPost = new HttpPost("http://fujian.temp.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://guangdong.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://shandong.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://shanghai.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://zhejiang.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://jiangsu.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://guangxi.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://hunan.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://fujian.eascs.com/easd/loginAction.do");  
//            httpPost = new HttpPost("http://henan.eascs.com/easd/loginAction.do");  
            // post 请求参数  
            List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();  
            pairs.add(new BasicNameValuePair("username", "weidong.yang"));  
            pairs.add(new BasicNameValuePair("password", "123456"));  
            HttpEntity entity = new UrlEncodedFormEntity(pairs, CHARSET);         
            httpPost.setEntity(entity);       
            // 执行情趣，获取相应  
            // 获取响应对象  
            HttpResponse response = httpClient.execute(httpPost);  
            Header[] h =response.getHeaders("Set-Cookie");  
            for (Header header : h) {  
                System.out.println(header.toString());  
            }  
            // 获取Entity对象  
            HttpEntity responseEntity = response.getEntity();  
            // 获取响应信息流  
            InputStream in = null;  
            if (responseEntity != null) {  
                in =  responseEntity.getContent();  
            }  
            String resp = getRespString(in);   
            if (resp == null) {
                resp = "" ;
            }
            if (!resp.contains("src=\"menu.do\"")) {
                System.out.println(resp);  
                System.out.println("=========================================");
                return false ;
            }
            return getSdLoginMenu(httpClient) ;
        }catch(Exception ex){  
            ex.printStackTrace();  
            return false ;
        }  
    }
    
    private static boolean getSdLoginMenu(HttpClient httpClient) {
        try {
//            httpClient = HttpClients.createDefault() ;
//            HttpGet get = new HttpGet("http://shandong.temp.eascs.com/easd/menu.do");  
            HttpGet get = new HttpGet("http://fujian.temp.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://guangdong.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://shandong.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://shanghai.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://zhejiang.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://jiangsu.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://guangxi.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://hunan.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://fujian.eascs.com/easd/menu.do");  
//            HttpGet get = new HttpGet("http://henan.eascs.com/easd/menu.do");  
            // 获取响应对象  
            HttpResponse response = httpClient.execute(get);  
            // 获取Entity对象  
            HttpEntity responseEntity = response.getEntity();  
            // 获取响应信息流  
            InputStream in = null;  
            if (responseEntity != null) {  
                in =  responseEntity.getContent();  
            }  
            String resp = getRespString(in);       
            if (resp == null) {
                resp = "" ;
            }
            if (resp.contains("请输入您的用户名和密码")) {
                System.out.println(resp);  
                return false ;
            }
            return true ;
        } catch (Exception e) {
            e.printStackTrace();
            return false ;
        }
    }

    private static String getRespString(InputStream in) throws Exception{  
        if (in == null) {
            return "啥也没有" ;
        }
        // 流转字符串  
        StringBuffer sb = new StringBuffer();  
        byte[]b = new byte[BYTE_LEN];  
        int len = 0;  
        while ((len = in.read(b)) != -1) {  
            sb.append(new String(b,0,len,CHARSET));  
        }  
        return sb.toString();  
    }  
}
