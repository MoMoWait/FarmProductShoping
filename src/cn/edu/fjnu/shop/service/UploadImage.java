/**
 * 
 */
package cn.edu.fjnu.shop.service;

import java.io.File;

import cn.edu.fjnu.shop.data.URLForService;
import cn.edu.fjnu.shop.dialog.LoadingDialog;
import cn.edu.fjnu.shop.http.AsyncHttpClient;
import cn.edu.fjnu.shop.http.AsyncHttpResponseHandler;
import cn.edu.fjnu.shop.http.RequestParams;
import cn.edu.fjnu.shop.system.UserInfo;
import cn.edu.fjnu.utils.OPUtils;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author GaoFei
 * 
 */
public class UploadImage {

	private static Context context;
	private static LoadingDialog loadingDialog;

	public static String uploadFile(File file, String username, Context context) {

		UploadImage.context = context;
		Uri uri = Uri.fromFile(file);
		AsyncHttpClient client = new AsyncHttpClient();
		// System.out.println(file.getAbsolutePath());
		if (file.exists() && file.length() > 0) {
			RequestParams params = new RequestParams();
			try {
				params.put("userID", username);
				params.put("profile_picture", file);

				// System.out.println("clientID"+username);
			} catch (Exception e) {
				e.printStackTrace();
			}

			final ProgressDialog mProgressDialog = new ProgressDialog(context);
			/*
			 * mProgressDialog.setMessage("正在上传");
			 * mProgressDialog.setCancelable(false);
			 * mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			 * mProgressDialog.show();
			 */
			showLoadingDialog();
			client.post(
					URLForService.UPLOADSERVICE,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							// 成功上传
							super.onSuccess(content);
							closeLoadingDialog();
							OPUtils.showToast("上传成功", Toast.LENGTH_SHORT);
							
						}

						@Override
						public void onFailure(Throwable error, String content) {
							super.onFailure(error, content);
							closeLoadingDialog();
							OPUtils.showToast("上传失败", Toast.LENGTH_SHORT);

						}
					});
			return "error";

		} else {
			return "error";

		}

	}

	public static void showLoadingDialog() {

		loadingDialog = new LoadingDialog(context);
		loadingDialog.show();
	}

	public static void closeLoadingDialog() {

		if (loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

}
