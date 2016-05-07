package cn.edu.fjnu.shop.activity;
import com.androidquery.AQuery;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.utils.CommonUtils;
import cn.jpush.android.api.JPushInterface;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InformationActivity extends Activity{

	
	private String photoUrl="";
	private String des="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_information);
		
		Intent intent = getIntent();
		if (null != intent) {
			String information=intent.getExtras().getString(JPushInterface.EXTRA_ALERT);
			
			String[] content=information.split("#");
			if(content.length==1)
				des=content[0];
			else{
				photoUrl=information.split("#")[1];
				des=information.split("#")[0];
			}
		
			
		}
		
		initView();
		
	}
	
	public void initView(){
		if(!photoUrl.equals("")){
			DisplayMetrics displayMetrics = new DisplayMetrics();
			ImageView informationImageView=(ImageView)findViewById(R.id.img_information);
			informationImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							(int)(CommonUtils.getScreenPixWidth(this)*0.75f)));
		}
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		initData();
	}
	
	
	public void initData(){
		AQuery aQuery=new AQuery(this);
		//aQuery.id(R.id.text)
		aQuery.id(R.id.img_information_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InformationActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
		
		aQuery.id(R.id.img_information).image(photoUrl);
		aQuery.id(R.id.text_information).text(des);
	}
}
