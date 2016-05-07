/**
 * 
 */
package cn.edu.fjnu.shop.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @author GaoFei
 * 通用工具类
 */
public class CommonUtils {
	/**
	 * 获取屏幕的宽度
	 * @param activity
	 * @return
	 */
	public static int  getScreenPixWidth(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.widthPixels;
	}
	
	/**
	 * 获取屏幕的高度
	 * @param activity
	 * @return
	 */
	public static int  getScreenPixHeight(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return displayMetrics.heightPixels;
	}
	
	/**
	 * 将dp转换成像素
	 * @param activity
	 * @param dp
	 * @return
	 */
	public static int dp2px(Activity activity, int dp){
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		return (int)(displayMetrics.density * dp);
	}
}
