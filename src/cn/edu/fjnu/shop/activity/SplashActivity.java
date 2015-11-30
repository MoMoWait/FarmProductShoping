package cn.edu.fjnu.shop.activity;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

public class SplashActivity extends Activity {

	private Handler mHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what==1){
				OPUtils.startActivity(SplashActivity.this, MainActivity.class);
				SplashActivity.this.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
				SplashActivity.this.finish();
			}
			
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		new TimeThread(mHandler).start();
	}
	
	
	private class TimeThread extends Thread{
		
		private Handler handler;
		public  TimeThread(Handler handler){
			this.handler=handler;
		}
		
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//super.run();
			try {
				Thread.sleep(2000);
				handler.sendEmptyMessage(1);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
	}
	
	//屏蔽back键和home键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return true;
	}
}
