package com.bia;

import java.io.IOException;

import android.app.Activity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Handler handler=new Handler();
		// handler.postDelayed(new Runnable() {
		// private int abcd=0;//�ڲ������ֻ�����ڲ����з���
		//
		// public void run() {
		// //�ڲ����з����ⲿ����
		// //MainActivity.this.------
		// startLoginActivity();
		// }
		// }, 2000);

		OkHttpClient client = new OkHttpClient();// �����ͻ���ʵ������
													// �ǵ���manifest�м�������user-permission
		okhttp3.Request request = new okhttp3.Request.Builder().url("http://172.27.0.21:8080/membercenter/api/hello")// ��������
				.method("GET", null).build();// �������ͣ�һ����GET POST PUT DELECT

		client.newCall(request).enqueue(new Callback() { // newCall().execute()��ͬ�����ã���������ܻῨ��ǰ��UI���棬����ʹ��enqueue,����������̨����̨�����ص�һ��callback�������ؽ��

			@Override
			public void onResponse(Call arg0, final Response arg1) throws IOException {
				MainActivity.this.runOnUiThread(new Runnable() { // �첽�߳�

					@Override
					public void run() {
						try {
							Toast.makeText(MainActivity.this,"��ȡ�����ݣ�"+ arg1.body().string(), Toast.LENGTH_LONG).show();// �����˰���˾show����						
						} catch (Exception e) {
							e.printStackTrace();
						}						
						startLoginActivity();
					}
				});

			}

			@Override
			public void onFailure(Call arg0, final IOException arg1) {
				// TODO Auto-generated method stub
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Toast.makeText(MainActivity.this, arg1.getLocalizedMessage(), Toast.LENGTH_LONG).show();

					}
				});
			}
		});

	}

	// ������������ת�Ʒ���
	void startLoginActivity() {
		Intent itnt = new Intent(this, LoginActivity.class);
		startActivity(itnt);
		finish();// ת�ƺ�����Լ�
	}
}
