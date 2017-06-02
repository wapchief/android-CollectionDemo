package wapchief.com.collectiondemo.framework.system;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

public class X_SystemBarUI {
	/** 
     * 改变系统标题栏颜色 
     * @param activity 
     * @param color   color xml文件下的颜色 
     */  
    public static void initSystemBar(Activity activity, int color) {  
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {  
            setTranslucentStatus(activity, true);  
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);  
        tintManager.setStatusBarTintEnabled(true);  
        // 使用颜色资源  
        tintManager.setStatusBarTintResource(color);  
  
    }  
    /** 
     * 设置系统标题栏的透明度 
     * @param activity 
     * @param on 
     */  
    @TargetApi(19)  
    private static void setTranslucentStatus(Activity activity, boolean on) {  
        Window win = activity.getWindow();  
        WindowManager.LayoutParams winParams = win.getAttributes();  
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;  
        if (on) {  
            winParams.flags |= bits;  
        } else {  
            winParams.flags &= ~bits;  
        }  
        win.setAttributes(winParams);  
    }  
}
