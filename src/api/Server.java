package api;

import java.net.CookiePolicy;

import android.webkit.CookieManager;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Server {

	static OkHttpClient client;
	
	static{
		java.net.CookieManager cookieManager = new java.net.CookieManager();
		cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
		
		client = new OkHttpClient.Builder()
				.cookieJar(new JavaNetCookieJar(cookieManager))
				.build();
	}
	
	
	//ͨ�� ���� getsharedClient���ṩ��������ʹ��
	public static OkHttpClient getsharedClient(){
		return client;
	}
	
	
	//  ����        ʡȥ���������úܳ���ǰ�沿��
	public static Request.Builder requestBuilderWithApi(String api){
		return new Request.Builder()
		.url("http://172.27.0.21:8080/membercenter/api/"+api);
	}
}
