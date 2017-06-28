package com.example;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextPaint;
import android.widget.TextView;
import android.widget.Toast;

import com.example.type2.Constant;
import com.example.type2.MyCrashHandler;
import com.example.type2.dialog.CustomDialog;
import com.example.type2.utils.ThreadPoolService;

import org.apache.http.client.CookieStore;

import java.util.ArrayList;

/**
 * 主要是type2访问需要使用
 * Created by sjy on 2017/6/28.
 */

public class MyApplication extends Application {

    public static MyApplication instance;// 实例化一个app
    private static ArrayList<Activity> activitystack;// activity启动栈，记录栈中的activity实例
    public static ThreadPoolService service;
    public static CookieStore cookieStore;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        instance = this;

        activitystack = new ArrayList<Activity>();
        service = new ThreadPoolService();
        MyCrashHandler handler = MyCrashHandler.getInstance();
        handler.init(getApplicationContext());
        //把异常处理的handler设置到主线程里面
        Thread.setDefaultUncaughtExceptionHandler(handler);
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        // 程序安全退出
        for (Activity activity : activitystack) {
            activity.finish();
        }
        activitystack.clear();
        super.onTerminate();
    }

    /**
     * 将应用程序的任务栈中的一activity实例添加到activitystack中
     *
     * @param activity 一个activity实例
     */
    public static void addActivity2Stack(Activity activity) {
        instance.activitystack.add(activity);
    }

    /**
     * 经activity实例从activitystack中移除
     *
     * @param activity 一个activity实例
     */
    public static void removeActivityFromStack(Activity activity) {
        instance.activitystack.remove(activity);
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 将中文字体设置为粗体字 android默认不支持中文
     *
     * @param tv
     */
    public static void setBoldText(TextView tv) {
        TextPaint tp2 = tv.getPaint();
        tp2.setFakeBoldText(true);
    }

    public static SharedPreferences getOrSharedPrefences(Context context) {
        return context.getSharedPreferences(Constant.ORPREFERENCES, Context.MODE_PRIVATE);

    }

    public static void showResultToast(int result, Context context) {
        switch (result) {
            case Constant.NO_RESPONSE:
                Toast.makeText(context, R.string.no_response, Toast.LENGTH_SHORT).show();
                break;
            case Constant.S_EXCEPTION:
                Toast.makeText(context, R.string.server_exception, Toast.LENGTH_SHORT).show();
                break;
            case Constant.RESPONESE_EXCEPTION:
                Toast.makeText(context, R.string.responese_exception, Toast.LENGTH_SHORT).show();
                break;
            case Constant.TIMEOUT:
                Toast.makeText(context, R.string.timeout, Toast.LENGTH_SHORT).show();
                break;
            case Constant.NO_NETWORK:
                Toast.makeText(context, R.string.no_network, Toast.LENGTH_SHORT).show();
                break;
            case Constant.NULLPARAMEXCEPTION:
                Toast.makeText(context, R.string.nullparamexception, Toast.LENGTH_SHORT).show();
                break;
            case Constant.SERVER_EXCEPTION:
                Toast.makeText(context, R.string.server_exception, Toast.LENGTH_SHORT).show();
                break;
            case Constant.RELOGIN:
                CustomDialog dialog = new CustomDialog(context, new CustomDialog.ButtonRespond() {

                    @Override
                    public void buttonRightRespond() {
                        Activity activity = activitystack.get(0);
                        activitystack.remove(0);// 把登录界面提出来
                        MyApplication.instance.onTerminate();
                        activitystack.add(activity);// 重新放到栈中
                    }

                    @Override
                    public void buttonLeftRespond() {
                        MyApplication.instance.onTerminate();
                    }
                });
                dialog.setDialogTitle(R.string.relogin);
                dialog.setDialogMassage(R.string.relogin_message);
                dialog.setLeftButtonText(R.string.exit_app);
                dialog.setRightButtonText(R.string.relogin);
                dialog.setCancelable(false);
                dialog.show();
                break;
            case 4005:
                Toast.makeText(context, "缺少参数", Toast.LENGTH_SHORT).show();
                break;
            case 4006:
                Toast.makeText(context, "参数值不能为空", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(context, "请求响应失败，错误号" + result, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}