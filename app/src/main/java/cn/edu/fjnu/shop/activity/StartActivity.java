package cn.edu.fjnu.shop.activity;

import cn.edu.fjnu.shop.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		startActivity(new Intent(StartActivity.this, MainActivity.class));
		overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
		this.finish();
	}
}
