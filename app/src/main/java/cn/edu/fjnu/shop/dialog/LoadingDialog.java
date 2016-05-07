package cn.edu.fjnu.shop.dialog;

import cn.edu.fjnu.shop.R;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadingDialog extends Dialog {
	
	private Context context;

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	
	public LoadingDialog(Context context) {
		super(context);
		this.context=context;		//this.context=context;
		// TODO Auto-generated constructor stub
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_show_progressbar);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		setCancelable(false);
		ImageView loadImageView= (ImageView)findViewById(R.id.img_loading);
		Animation loadAnimation= AnimationUtils.loadAnimation(context,R.anim.dialog_progress);
		loadImageView.startAnimation(loadAnimation);
	}

}
