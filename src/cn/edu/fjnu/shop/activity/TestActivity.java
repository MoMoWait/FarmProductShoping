package cn.edu.fjnu.shop.activity;

import java.util.ArrayList;
import java.util.List;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.adapter.CategoryAdapter;
import cn.edu.fjnu.shop.dialog.ExitDialog;
import cn.edu.fjnu.shop.domain.CategoryInfo;
import cn.edu.fjnu.utils.system.CommonValues;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;

public class TestActivity extends Activity {

	private ListView categoryListView;
	private List<CategoryInfo> categoryInfos=new ArrayList<CategoryInfo>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.package_carshop_content);
		initView();
	}
	
	public void initView(){
		
		//CategoryAdapter categoryAdapter=new CategoryAdapter(this, categoryInfos);
		//categoryListView.setAdapter(categoryAdapter);
	}
}
