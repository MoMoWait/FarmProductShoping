package cn.edu.fjnu.shop.utils;

import android.content.Context;
import cn.edu.fjnu.shop.dialog.LoadingDialog;

public class DialogUtils {

	private static LoadingDialog loadingDialog;
	
	public static void showLoadingDialog(Context context) {

		loadingDialog = new LoadingDialog(context);
		loadingDialog.show();
	}

	public static void closeLoadingDialog() {

		if (loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
}
