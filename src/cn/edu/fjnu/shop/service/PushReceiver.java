package cn.edu.fjnu.shop.service;
import cn.edu.fjnu.shop.activity.InformationActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class PushReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		// TODO Auto-generated method stub
		if(intent.getAction().equals("cn.jpush.android.intent.NOTIFICATION_OPENED")){
			//用户打开通知栏
		  	//打开自定义的Activity
        	Intent i = new Intent(context, InformationActivity.class);
        //	i.putExtra(name, value)
        	i.putExtras(bundle);
        	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //	i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        	context.startActivity(i);
        	//context.overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
		}
	}

}
