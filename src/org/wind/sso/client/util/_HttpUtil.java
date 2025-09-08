package org.wind.sso.client.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * @描述 : HttpClient工具类（模仿HttpClient工具类）
 * @作者 : 胡璐璐
 * @时间 : 2019年3月11日 15:10:35
 */
public class _HttpUtil {
	
	/**本地构造方法**/
	private _HttpUtil() {
		
	}
	/**忽略SSL验证**/
	static {
        try {
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String urlHostName, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }
	private static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[1];
        trustAllCerts[0] = new TrustAllManager();
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    private static class TrustAllManager implements X509TrustManager {
    	
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        	
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        	
        }
    }
	
    /**创建HttpURLConnection**/
    public static HttpURLConnection getHttpURLConnection(String url){
    	try {
			URL url_obj = new URL(url);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url_obj.openConnection();
//	    	httpURLConnection.setRequestMethod("POST");		// 提交模式
//	    	httpURLConnection.setUseCaches(false);   //不允许缓存
	    	httpURLConnection.setConnectTimeout(_SysConstant.conTimeout);		//连接超时（单位：毫秒）
	    	httpURLConnection.setReadTimeout(_SysConstant.readTimeout);			//读取超时（单位：毫秒）
	    	httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//	    	httpURLConnection.setRequestProperty("Connection", "Keep-Alive");	//维持长连接
	    	httpURLConnection.setRequestProperty("Charset", "UTF-8");
	        // 发送POST请求必须设置如下两行
	        httpURLConnection.setDoOutput(true);
	        httpURLConnection.setDoInput(true);
	        //
	        return httpURLConnection;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
    }
	
    /**
     * post提交（通用，返回字节流）
     * @param url：提交的URL
     * @param paramMap：post提交的参数（Map式）
     * @param headMap：头部参数（Map式）
     * */
    public static ByteArrayOutputStream post_global(String url,Map<Object,Object> paramMap,Map<Object,Object> headMap) throws Exception {
    	HttpURLConnection hc=getHttpURLConnection(url);
    	hc.setRequestMethod("POST");		// 提交模式
    	//
    	StringBuffer paramStr=new StringBuffer();
    	if(paramMap!=null && paramMap.size()>0) {
	    	for(Entry<Object, Object> t_entry:paramMap.entrySet()){
	    		Object t_key=t_entry.getKey();
	    		Object t_value=t_entry.getValue();
	    		if(t_value!=null) {
	    			paramStr.append(t_key+"="+URLEncoder.encode(t_value.toString(),"UTF-8")+"&");
	    		}
	    	}
    	}
    	OutputStream os=null;
    	DataOutputStream dos=null;
    	InputStream is=null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream bos = null;
        try{
        	// 获取URLConnection对象对应的输出流
        	os=hc.getOutputStream();
        	dos = new DataOutputStream(os);
            // 发送请求参数
        	dos.writeBytes(paramStr.toString());	//post的参数 xx=xx&yy=yy
            // flush输出流的缓冲
        	dos.flush();
        	
        	int statusCode=hc.getResponseCode();
        	if(HttpURLConnection.HTTP_OK==statusCode){
	            //开始获取数据
        		is=hc.getInputStream();
	        	bis = new BufferedInputStream(is);
	        	bos = new ByteArrayOutputStream();
	        	//
		        int len;
		        byte[] arr = new byte[1024];
		        while((len=bis.read(arr))!= -1){
		            bos.write(arr,0,len);
		        }
		        return bos;
        	}else{
        		throw new RuntimeException("客户端异常："+statusCode);
        	}
        }catch(Exception e){
        	throw new IllegalArgumentException(e);
        }finally{
        	_SystemUtil.close(bis,is,dos,os);
        }
    }
    /**post提交（字符集：默认"UTF-8"）**/
    public static String post(String url,Map<Object,Object> paramMap,Map<Object,Object> headMap) throws Exception {
    	return post_global(url, paramMap, headMap).toString("UTF-8");
    }
    /** post提交（流）**/
    public static byte[] post_stram(String url,Map<Object,Object> paramMap,Map<Object,Object> headMap) throws Exception {
    	return post_global(url, paramMap, headMap).toByteArray();
    }
}
