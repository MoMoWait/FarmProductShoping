package cn.edu.fjnu.shop.activity;
import java.util.List;
import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.FragmentIndex;
import cn.edu.fjnu.shop.data.FragmentSelectState;
import cn.edu.fjnu.shop.dialog.ExitDialog;
import cn.edu.fjnu.shop.domain.DetailCarShop;
import cn.edu.fjnu.shop.fragment.CartFragment;
import cn.edu.fjnu.shop.fragment.CategoryFragment;
import cn.edu.fjnu.shop.fragment.HomeFragment;
import cn.edu.fjnu.shop.fragment.MyFragment;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.shop.utils.CommonUtils;
import cn.jpush.android.api.JPushInterface;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


public class MainActivity extends FragmentActivity implements View.OnClickListener{
	private HomeFragment homeFragment=new HomeFragment();
	private CategoryFragment categoryFragment=new CategoryFragment();
	private CartFragment cartFragment=new CartFragment();
	private MyFragment myFragment=new MyFragment();
	private int currentFragmentIndex=FragmentIndex.HOME_FRAGMENT;
	private ImageView homeImageView;
	private ImageView categoryImageView;
	private ImageView cartImageView;
	private ImageView myImageView;
	public static MainActivity mainActivity;
	//private int newFragmentIndex=currentFragmentIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setFragmentIndex(FragmentIndex.HOME_FRAGMENT);
        mainActivity=this;
        
    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			showExitDialog();
			//MainActivity.this.finish();
			//overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
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
     
        	
        if(id==R.id.feedback){
        	startActivity(new Intent(MainActivity.this, FeedBackActivity.class));
			overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
        }else if(id==R.id.about){
        	
        }else if(id==R.id.exit){
        	if(UserInfo.isLogin)
        		showSelectExitDialog();
        	else
        		showExitDialog();
        }
        return true;
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	super.onActivityResult(requestCode,resultCode,data);
    	
    	if(requestCode==1){
    		if(resultCode==RESULT_OK){
    			/**替换fragment*/
    			FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
    			myFragment=new MyFragment();
    			fragmentTransaction.replace(R.id.fragment_container,myFragment);
    			fragmentTransaction.commit();
    		}
    	}else if(requestCode==2){
    		if(resultCode==3){
    			/**
    			 * 切换到购物车页面*/
    			//System.out.println("即将进入购物车页面");
    			setFragmentIndex(FragmentIndex.CART_FRAGMENT);
    		}else if(resultCode==4){
    			
    		}
    	}else if(requestCode==3){
    		
    		if(resultCode==RESULT_OK){
    			FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        		fragmentTransaction.detach(cartFragment);
            	cartFragment = new CartFragment();
        		fragmentTransaction.replace(R.id.fragment_container,cartFragment);
        		fragmentTransaction.commit();
    		}else{
    			
    		}
    		
    	}else if(requestCode==4){
    		if(resultCode==RESULT_OK){
    			setFragmentIndex(FragmentIndex.CART_FRAGMENT);
    			
    		}
    	}else if(requestCode==5){
    		
    		if(resultCode==RESULT_OK){
    			//setFragmentIndex(FragmentIndex.CART_FRAGMENT);
    			FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        		fragmentTransaction.detach(cartFragment);
            	cartFragment = new CartFragment();
        		fragmentTransaction.replace(R.id.fragment_container,cartFragment);
        		fragmentTransaction.commit();
    		}
    	}else if(requestCode==6){
    		if(resultCode==RESULT_OK){
    			/**重新打开登录页面*/
    			Intent intent=new Intent(this, LoginActivity.class);
    			startActivityForResult(intent, 7);
				//OPUtils.startActivityForResult(mainActivity, LoginActivity.class,new Bundle(), 1);
				overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
    		}
    	}else if(requestCode==7){
    		if(resultCode==RESULT_OK){
    			
    		}else{
    			
    			deleteFromXML();
    			
       			/**替换fragment*/
    			FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
    			myFragment=new MyFragment();
    			fragmentTransaction.replace(R.id.fragment_container,myFragment);
    			fragmentTransaction.commit();
    		}
    	}
    }
        
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	JPushInterface.onPause(this);
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	JPushInterface.onResume(this);
    }
    
    public void setCartFragment(List<DetailCarShop> shops) {
    	FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
		fragmentTransaction.detach(cartFragment);
    	cartFragment = new CartFragment(shops);
		fragmentTransaction.replace(R.id.fragment_container,cartFragment);
		fragmentTransaction.commit();
	}
    
    public void setFragmentIndex(int fragmentIndex){
    	//newFragmentIndex=fragmentIndex;
    	setFragmentState(currentFragmentIndex, FragmentSelectState.NORMAL);
    	currentFragmentIndex=fragmentIndex;
    	setFragmentState(currentFragmentIndex, FragmentSelectState.SELECTED);
    	
    	FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
    	//fragmentTransaction.
    	switch (fragmentIndex) {
		case FragmentIndex.HOME_FRAGMENT:
			fragmentTransaction.replace(R.id.fragment_container, homeFragment).commit();
			break;
	    case FragmentIndex.CATEGORY_FRAGMENT:
	    	fragmentTransaction.replace(R.id.fragment_container, categoryFragment).commit();
			break;
	    case FragmentIndex.CART_FRAGMENT:
			fragmentTransaction.replace(R.id.fragment_container, cartFragment).commit();
			break;
	    case FragmentIndex.MY_FRAGMENT:
	    	fragmentTransaction.replace(R.id.fragment_container, myFragment).commit();
			break;
		default:
			break;
		}
    }
    
    
    public void setFragmentState(int fragmentIndex,int state){
    	if(fragmentIndex==FragmentIndex.HOME_FRAGMENT){
    		if(state==FragmentSelectState.NORMAL)
    			homeImageView.setImageResource(R.drawable.home_normal);
    		else
    			homeImageView.setImageResource(R.drawable.home_selected);
    	}else if(fragmentIndex==FragmentIndex.CATEGORY_FRAGMENT){
    		if(state==FragmentSelectState.NORMAL)
    			categoryImageView.setImageResource(R.drawable.category_normal);
    		else
    			categoryImageView.setImageResource(R.drawable.category_selected);
    	}else if(fragmentIndex==FragmentIndex.CART_FRAGMENT){
    		if(state==FragmentSelectState.NORMAL)
    			cartImageView.setImageResource(R.drawable.cart_normal);
    		else
    			cartImageView.setImageResource(R.drawable.cart_selected);
    	}else{
    		if(state==FragmentSelectState.NORMAL)
    			myImageView.setImageResource(R.drawable.my_normal);
    		else
    			myImageView.setImageResource(R.drawable.my_selected);
    	}
    }
    
    public void initView(){
    	homeImageView=(ImageView)findViewById(R.id.img_home);
    	categoryImageView=(ImageView)findViewById(R.id.img_category);
    	cartImageView=(ImageView)findViewById(R.id.img_cart);
    	myImageView=(ImageView)findViewById(R.id.img_my);
    	
    	homeImageView.setOnClickListener(this);
    	categoryImageView.setOnClickListener(this);
    	cartImageView.setOnClickListener(this);
    	myImageView.setOnClickListener(this);
    }
    
    
    public HomeFragment getHomeFragment() {
		return homeFragment;
	}


	public void setHomeFragment(HomeFragment homeFragment) {
		this.homeFragment = homeFragment;
	}


	public CategoryFragment getCategoryFragment() {
		return categoryFragment;
	}


	public void setCategoryFragment(CategoryFragment categoryFragment) {
		this.categoryFragment = categoryFragment;
	}


	public CartFragment getCartFragment() {
		return cartFragment;
	}


	public void setCartFragment(CartFragment cartFragment) {
		this.cartFragment = cartFragment;
	}


	public MyFragment getMyFragment() {
		return myFragment;
	}


	public void setMyFragment(MyFragment myFragment) {
		this.myFragment = myFragment;
	}

	public  MainActivity getSelf(){
		return MainActivity.this;
	}
	
	@Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.img_home:
			setFragmentIndex(FragmentIndex.HOME_FRAGMENT);
			break;
		case R.id.img_category:
			setFragmentIndex(FragmentIndex.CATEGORY_FRAGMENT);
			break;
		case R.id.img_cart:
			setFragmentIndex(FragmentIndex.CART_FRAGMENT);
			break;
		case R.id.img_my:
			setFragmentIndex(FragmentIndex.MY_FRAGMENT);
			break;
		default:
			break;
		}
    }
    
    public void showExitDialog(){
    	
    	final ExitDialog exitDialog=new ExitDialog(this);
		exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exitDialog.setCancelable(true);
		exitDialog.setContentView(R.layout.dialog_exit);
		exitDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
		exitDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(MainActivity.this)*0.8f), -2);
		//exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		exitDialog.getWindow().findViewById(R.id.btn_exit_cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				exitDialog.dismiss();
			}
		});
		
		exitDialog.getWindow().findViewById(R.id.btn_exit_commit).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitDialog.dismiss();
				MainActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
		exitDialog.show();
    }
    
    public void  showSelectExitDialog(){
    	final ExitDialog exitDialog=new ExitDialog(this);
		exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exitDialog.setCancelable(true);
		//exitDialog.setContentView(R.layout.dialog_exit);
		ListView exitListView=new ListView(exitDialog.getContext());
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(exitDialog.getContext(), R.layout.list_item_sex, 
				R.id.text_sex_item, new String[]{"退出当前帐号","完全退出应用"});
		exitListView.setAdapter(adapter);
		exitDialog.setContentView(exitListView);
		exitDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
		exitDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(MainActivity.this)*0.8f), -2);
		exitListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				if(position==0){
					
					//UserInfo.isLogin=false;
					//UserInfo.is
					exitDialog.dismiss();
					reloadMyFragment();
					
					
				}else{
					exitDialog.dismiss();
					MainActivity.this.finish();
					overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);

				}
				
			}
		});
		exitDialog.show();
    }
    
   public void deleteFromXML(){
	   
		SharedPreferences sharedPreferences=getSharedPreferences("LoginState",Context.MODE_PRIVATE);
		SharedPreferences.Editor editor=sharedPreferences.edit();
		editor.clear();
		editor.commit();
		
		UserInfo.isEmptyShopCar=true;
		UserInfo.isLogin=false;
		UserInfo.userID=-1;
   }
   
   public void reloadMyFragment(){
	   
	   deleteFromXML();
	   
	   if(currentFragmentIndex==FragmentIndex.MY_FRAGMENT){
			/**替换fragment*/
			FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
			myFragment=new MyFragment();
			fragmentTransaction.replace(R.id.fragment_container,myFragment);
			fragmentTransaction.commit();
		}else{
			setFragmentIndex(FragmentIndex.MY_FRAGMENT);
		}
		
	   
   }
   
   
   public void reloadCartFragment(){
	   
	   	deleteFromXML();
		FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
		fragmentTransaction.remove(cartFragment);
    	cartFragment = new CartFragment();
		fragmentTransaction.replace(R.id.fragment_container,cartFragment);
		fragmentTransaction.commit();
   }
}
