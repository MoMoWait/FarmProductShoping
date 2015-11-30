package cn.edu.fjnu.shop.activity;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.utils.OPUtils;
import android.app.Activity;
import android.os.Bundle;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		OPUtils.startActivity(this, MainActivity.class);
		overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
		this.finish();
	}
}
