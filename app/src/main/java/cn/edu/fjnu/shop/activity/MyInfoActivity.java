package cn.edu.fjnu.shop.activity;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;

import cn.edu.fjnu.shop.R;
import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.service.UploadImage;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.shop.utils.CommonUtils;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity {

	private AQuery aQuery;
	private String userName;
	private String userSex;
	private String userAddress;
	private String userPhone;
	private AQuery changeAQuery;
	private Uri photoUri;
	private ProgressDialog mProgressDialog;
	private Uri imageUri;
	
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if(msg.what==1){
				closeProgressDialog();
			}
			//super.handleMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_info);
		initView();
		initData();
		initEvent();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			MyInfoActivity.this.finish();
			overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==5){
			if(resultCode==RESULT_OK){
				//裁剪图片
				startPhotoZoom(photoUri);
				//OPUtils.showToast("hello",Toast.LENGTH_SHORT);
				
			}
		}else if(requestCode==6){
			if(resultCode==RESULT_OK){
				try {
					Uri localUri = data.getData();
					String[] projStrings = { MediaStore.Images.Media.DATA };
					Cursor cursor = managedQuery(localUri, projStrings, null, null,
							null);
					int cloum_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String pathString = cursor.getString(cloum_index);
					startPhotoZoom(Uri.fromFile(new File(pathString)));
					//Uri.fromFile(new File(pathString))
					/* cursor.close(); */
					/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(pathString, options);
					int imageWidth, imageHeight;
					imageWidth = options.outWidth;
					imageHeight = options.outHeight;
					int scaleX, scaleY;
					scaleX = imageWidth /300;
					scaleY = imageHeight / 300;
					options.inSampleSize = Math.max(scaleX, scaleY);
					options.inJustDecodeBounds = false;
					Bitmap bm = BitmapFactory.decodeFile(pathString, options);
					//canvansImageView.setImageBitmap(bm);
*/					//aQuery.id(R.id.img_head_photo).image(bm);
					
				} catch (Exception e) {
					// TODO: handle exception
					Toast.makeText(this, "出问题了,请多试几次...", Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				}
				
			}
		}else if(requestCode==7){
			if(resultCode==RESULT_OK){
				
				//获取uri
				if(imageUri!=null){
					
					//BitmapFactory.decodef
					File targetFile=new File(Environment.getExternalStorageDirectory(), "head_myphoto.jpg");
					Options options=new Options();
					options.inJustDecodeBounds=true;
					BitmapFactory.decodeFile(targetFile.getAbsolutePath(), options);
					int scaleX, scaleY, imageWidth, imageHeight;
					imageWidth = options.outWidth;
					imageHeight = options.outHeight;
					scaleX = imageWidth/200;
					scaleY = imageHeight/200;
					options.inSampleSize = Math.max(scaleX, scaleY);
					options.inJustDecodeBounds = false;
					//BitmapFactory.decodeFile(targetFile.getAbsolutePath(), opts)
					Bitmap bitmap= BitmapFactory.decodeFile(targetFile.getAbsolutePath(), options);
					//存储小图片
					File minFile=new File(Environment.getExternalStorageDirectory(), "min_head.jpg");
					FileOutputStream minPhotoFileOutputStream;
					try {
						minPhotoFileOutputStream = new FileOutputStream(minFile);
						bitmap.compress(CompressFormat.JPEG, 50, minPhotoFileOutputStream);
						minPhotoFileOutputStream.flush();
						minPhotoFileOutputStream.close();
						Bitmap minBitmap=Bitmap.createScaledBitmap(bitmap, 200, 200, false);
						aQuery.id(R.id.img_head_photo).image(minBitmap);
						UploadImage.uploadFile(minFile, String.valueOf( UserInfo.userID), this);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					
					
					
					//UploadImage.uploadFile(uploadImage, String.valueOf( UserInfo.userID), this);
				}
				
			}
			
		}else if(requestCode==8){
			if(resultCode==RESULT_OK){
				/**密码修改成功*/
				//需要重新登录
				setResult(RESULT_OK);
				MyInfoActivity.this.finish();
			}
		}
	}
	
	public void  initView(){
		
	}
	
	public void  initData(){
		
		aQuery=new AQuery(this);
		changeAQuery=new AQuery(this);
		
		Map<String,String> params=new HashMap<String, String>();
		params.put("command","1");
		params.put("ID",String.valueOf(UserInfo.userID));
		aQuery.ajax(URLForService.MYSERVICE, params, JSONObject.class, this, "responseUserInfo");
		//aQuery.ajax(URLForService, params, null, this, null);
		
	}
	
	public void initEvent(){
		aQuery.id(R.id.img_myinfo_back).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyInfoActivity.this.finish();
				overridePendingTransition(R.anim.activity_exit_right, R.anim.activity_exit_left);
			}
		});
		
		aQuery.id(R.id.layout_user_name).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showInfoDialog("name");
			}
		});
		
		aQuery.id(R.id.layout_sex).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showInfoDialog("sex");
			}
		});
		
		aQuery.id(R.id.layout_address).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showInfoDialog("address");
			}
		});
		
		aQuery.id(R.id.layout_phone_number).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showInfoDialog("phone");
			}
		});
		
		aQuery.id(R.id.layout_head_photo).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final Dialog headPhotoDialog=new Dialog(MyInfoActivity.this);
				headPhotoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				headPhotoDialog.setCancelable(true);
				headPhotoDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
				ListView wayUploadListView=new ListView(headPhotoDialog.getContext());
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(headPhotoDialog.getContext(), R.layout.list_item_sex,
						R.id.text_sex_item, new String[]{"拍照上传","本地上传"});
				wayUploadListView.setAdapter(adapter);
				headPhotoDialog.setContentView(wayUploadListView);
				headPhotoDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(MyInfoActivity.this)*0.8f), -2);
				headPhotoDialog.show();
				wayUploadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						headPhotoDialog.dismiss();
						if(position==0){
							//启动相机拍照		
							File dirFile=getFilesDir();
							if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
								File file=new File(Environment.getExternalStorageDirectory(),"test.jpg");
								//photoPath=file.getAbsolutePath();
								Uri uri= Uri.fromFile(file);
								photoUri=uri;
								Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								startActivityForResult(intent, 5);
							}else{
								Toast.makeText(getApplicationContext(), "SD卡不存在", Toast.LENGTH_SHORT).show();
							}
							
							
							
						}else{
							//启动图片搜索
							Intent intent = new Intent();
							intent.setType("image/*");//打开图片方式
							intent.setAction(Intent.ACTION_GET_CONTENT); 
							startActivityForResult(intent,6);
							
						}
						
						
					}
				});
			}
		});
		
		aQuery.id(R.id.layout_password_change).clicked(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MyInfoActivity.this,PasswordChangeActivity.class);
				startActivityForResult(intent, 8);
				overridePendingTransition(R.anim.activity_enter_right, R.anim.activity_enter_left);
			}
		});
	}
	
	public void responseUserInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
			
		}
		if(data!=null){
			try {
				userName=data.getString("name");
				aQuery.id(R.id.text_user_name).text(userName);
				DecimalFormat decimalFormat=new DecimalFormat();
				decimalFormat.setMaximumFractionDigits(2);
				aQuery.id(R.id.text_money_left).text(decimalFormat.format(Float.parseFloat(data.getString("account"))));
				aQuery.id(R.id.img_head_photo).image(data.getString("headPhoto"),false,true);
				//aQuery.id(R.id.)
				userPhone=data.getString("phoneNumber");
				aQuery.id(R.id.text_phonenumber).text(userPhone);
				userSex=data.getString("sex");
				aQuery.id(R.id.text_sex).text(userSex);
				userAddress=data.getString("address");
				//System.out.println("用户地址:"+userAddress);
				aQuery.id(R.id.text_my_adress).text(userAddress);
				aQuery.id(R.id.text_user_id).text(String.valueOf(UserInfo.userID));
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}else{
			
		}
	}
	
	
	public void showInfoDialog(final String type){
		
		final EditText contentEditText;
		TextView titleTextView;
		final Dialog typeDialog=new Dialog(this);
		typeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		typeDialog.setCancelable(true);
		if(!type.equals("sex"))
			typeDialog.setContentView(R.layout.dialog_name);
		typeDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
		typeDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(MyInfoActivity .this)*0.8f), -2);
		if(!type.equals("sex")){
			contentEditText=(EditText)typeDialog.getWindow().findViewById(R.id.text_access_content);
			titleTextView=(TextView)typeDialog.getWindow().findViewById(R.id.text_dialog_title);
			if(type.equals("name")){
				contentEditText.setText(userName);
				titleTextView.setText("名字修改");
			}else if(type.equals("address")){
				titleTextView.setText("地址修改");
				/**调整界面布局*/
				LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, CommonUtils.dp2px(this, 100));
				layoutParams.leftMargin=CommonUtils.dp2px(this, 10);
				layoutParams.rightMargin=CommonUtils.dp2px(this,10);
				layoutParams.topMargin=CommonUtils.dp2px(this, 10);
				contentEditText.setLayoutParams(layoutParams);
				contentEditText.setText(userAddress);
			}else if(type.equals("phone")){
				titleTextView.setText("电话修改");
				contentEditText.setText(userPhone);
			}
			typeDialog.getWindow().findViewById(R.id.btn_exit_cancel).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					typeDialog.dismiss();
				}
			});
			
			typeDialog.getWindow().findViewById(R.id.btn_exit_commit).setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(TextUtils.isEmpty(contentEditText.getText().toString())){
						Toast.makeText(getApplicationContext(), "输入内容为空", Toast.LENGTH_SHORT).show();
						return ;
					}
					String changeContent=contentEditText.getText().toString();
					//if(type)
					Map<String, String> params=new HashMap<String, String>();
					params.put("ID",String.valueOf(UserInfo.userID));
					params.put("userID",String.valueOf(UserInfo.userID));
					params.put("command", "2");
					params.put("type", type);
					if(type.equals("name")&&!changeContent.equals(userName)){
						userName=changeContent;
						changeAQuery.id(R.id.text_user_name).text(userName);
						params.put("value", contentEditText.getText().toString());
						aQuery.ajax(URLForService.MYSERVICE, params,JSONObject.class, MyInfoActivity.this, "responseChangeInfo");
						
					}else if(type.equals("address")&&!changeContent.equals(userAddress)){
						userAddress=changeContent;
						changeAQuery.id(R.id.text_my_adress).text(userAddress);
						params.put("value", contentEditText.getText().toString());
						aQuery.ajax(URLForService.MYSERVICE, params,JSONObject.class, MyInfoActivity.this, "responseChangeInfo");
						
					}else if(type.equals("phone")&&!changeContent.equals(userPhone)){
						
						userPhone=changeContent;
						changeAQuery.id(R.id.text_phonenumber).text(userPhone);
						params.put("value", contentEditText.getText().toString());
						aQuery.ajax(URLForService.MYSERVICE, params,JSONObject.class, MyInfoActivity.this, "responseChangeInfo");
					}
						
					typeDialog.dismiss();
				}
			});
			
			typeDialog.show();
		}else{
			//typeDialog.setcon
			/**调整页面布局*/
			final Dialog sexDialog=new Dialog(this);
			sexDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			sexDialog.setCancelable(true);
			sexDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_backgoroud_exit);
			
			sexDialog.setContentView(R.layout.dialog_sex);
			sexDialog.getWindow().setLayout((int)(CommonUtils.getScreenPixWidth(this)*0.8f), -2);
			ListView sexListView=(ListView)sexDialog.getWindow().findViewById(R.id.list_sex);
			ArrayAdapter<String> adapter=new ArrayAdapter<String>(sexDialog.getContext(), R.layout.list_item_sex,R.id.text_sex_item,
					new String[]{"男","女"});
			
			sexListView.setAdapter(adapter);
			sexDialog.show();
			sexListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String selectSex=(String)parent.getAdapter().getItem(position);
					if(!userSex.equals(selectSex)){
						userSex=selectSex;
						changeAQuery.id(R.id.text_sex).text(userSex);
						//发送ajax请求
						Map<String, String> params=new HashMap<String, String>();
						params.put("ID",String.valueOf(UserInfo.userID));
						params.put("userID",String.valueOf(UserInfo.userID));
						params.put("command", "2");
						params.put("type", type);
						params.put("value",selectSex);
						aQuery.ajax(URLForService.MYSERVICE, params,JSONObject.class, MyInfoActivity.this, "responseChangeInfo");
						//typeDialog.dismiss();
					}
						
					sexDialog.dismiss();
				}
			});
		}
		
	}
	
	 
	public void responseChangeInfo(String url,JSONObject data,AjaxStatus status){
		if(status.getCode()!=200){
			Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();			
		}
		
		if(data!=null){
			try {
				String state=data.getString("state");
				Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();			
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();			
				e.printStackTrace();
			}
		}
	}
	
	
	// 裁剪图片
	public void startPhotoZoom(Uri uri) {
		
		//if(Environment.getExternalStorageState().equals(m))
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			// 裁剪图片
			imageUri=Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"head_myphoto.jpg"));
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			//intent.putExtra("outputX", 256);
			//intent.putExtra("outputY", 256);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", false);
			intent.putExtra("output", imageUri);
			intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
			intent.putExtra("noFaceDetection", false);
			startActivityForResult(intent, 7);
		}
		
		//intent.putExtra("output", )
		
	}
	
	//头像发送线程
	public class HeadThread extends Thread{
		private int mUserID;
		private Handler mHandler;
		private Socket mSocket;
		public HeadThread(int userID,Handler handler){
			mUserID=userID;
			mHandler=handler;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				mSocket=new Socket("192.168.1.203",9090);
				File dirFile= getFilesDir();
				File headFile=new File(dirFile, "head.jpg");
				FileInputStream fileInputStream=new FileInputStream(headFile);
				OutputStream outputStream=mSocket.getOutputStream();
				InputStream inputStream=mSocket.getInputStream();
				DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
				DataInputStream dataInputStream=new DataInputStream(inputStream);
				dataOutputStream.writeUTF("userID;"+mUserID);
				dataOutputStream.flush();
				dataOutputStream.writeUTF("fileLength;"+headFile.length());
				dataOutputStream.flush();
				byte[] readContents=new byte[1024];
				int readLength=fileInputStream.read(readContents);
				System.out.println(readLength);
				while(readLength>0){
					dataOutputStream.write(readContents, 0, readLength);
					dataOutputStream.flush();
					readLength=fileInputStream.read(readContents);
					System.out.println(readLength);
				}
				//System.out.println("goOut");
				fileInputStream.close(); 
				//dataOutputStream.close();
				while(true){
					
					String finish= dataInputStream.readUTF();
					if("finish".equals(finish)){
						mHandler.sendEmptyMessage(1);
						dataInputStream.close();
					//	dataOutputStream.close();
						if(mSocket!=null&&!mSocket.isClosed())
							mSocket.close();
						break;
					}
				}
				
				//socket
			} catch (Exception e) {
				// TODO: handle exception
				if(mSocket!=null&&!mSocket.isClosed())
					try {
						mSocket.close();
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				e.printStackTrace();
			}
		}
	}
	
	public Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			// 先通过getContentResolver方法获得一个ContentResolver实例，
			// 调用openInputStream(Uri)方法获得uri关联的数据流stream
			// 把上一步获得的数据流解析成为bitmap
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	public void showProgressDialog(){
		mProgressDialog=new ProgressDialog(this);
		mProgressDialog.setMessage("正在上传");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.show();
	}
	
	public void closeProgressDialog(){
		
		if(mProgressDialog!=null&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		
	}
}
