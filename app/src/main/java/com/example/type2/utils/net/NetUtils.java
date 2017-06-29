package com.example.type2.utils.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.type1.utils.LogUtils;
import com.example.type2.Constant;
import com.example.type2.entity.ParamsDatas;
import com.example.type2.utils.CustomRunnable;
import com.example.type2.utils.ThreadPoolService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Copyright (c) 2013 Company,Inc. All Rights Reserved.
 *
 * @author lanhaizhong
 * @version V1.0
 * @Title: NetUtils.java
 * @Description: 一些网络应用的工具类
 * @date 2013年7月3日 上午11:29:42
 */
public class NetUtils {
    /**
     * 检查是否有可用网络
     *
     * @param context 上下文环境
     * @return 有可用网络返回true 否则返回false
     */
    public static boolean isHasNet(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conn.getActiveNetworkInfo();// 获取联网状态网络
        if (info == null || !info.isAvailable()) {
            return false;
        } else {
            return true;
        }
    }


    /**
     * 图片上传 只有路径+图片(无参数)
     *
     * @param datas
     * @param callback
     * @return
     */

    public static CustomRunnable<?, ?> imageUpload(final ParamsDatas datas, final UploadCallback callback) {


        //        public static String uploadFile(String[] picPaths, String requestURL) {


        CustomRunnable<ParamsDatas, String> customRunnable = new CustomRunnable<ParamsDatas, String>(datas) {
            @Override
            public String executeTask(ParamsDatas... param) {
                HttpURLConnection conn = null;
                DataOutputStream outStream = null;
                InputStreamReader in = null;
                //常量
                String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
                String PREFIX = "--";
                String LINEND = "\r\n";
                String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
                String CHARSET = "UTF-8";
                try {
                    URL url = new URL(datas.getUrl());
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(20000);//conn-->HttpURLConnection
                    conn.setReadTimeout(20000);// 缓存最长时间
                    conn.setDoInput(true);// 允许输入
                    conn.setDoOutput(true);// 允许输出
                    conn.setUseCaches(false);// 不允许缓存
                    conn.setRequestMethod("POST");// 请求方式
                    conn.setRequestProperty("connection", "keep-alive");
                    conn.setRequestProperty("Charset", "UTF-8");// 设置编码
                    conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
                    conn.connect();

           /*
             * 拼接文件数据
			 */
                    int fileLen = datas.getFiles().length;
                    for (int i = 0; i < fileLen; i++) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(PREFIX);
                        sb2.append(BOUNDARY);
                        sb2.append(LINEND);
                        /**
                         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                         * filename是文件的名字，包含后缀名的 比如:abc.png
                         */
                        sb2.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + datas.getFiles()[i].getName() + "\"" + LINEND);
                        sb2.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                        sb2.append(LINEND);

                        outStream.write(sb2.toString().getBytes());
                        // ?
                        InputStream is = new FileInputStream(datas.getFiles()[i]);
                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while ((len = is.read(buffer)) != -1) {
                            outStream.write(buffer, 0, len);
                        }
                        is.close();
                        outStream.write(LINEND.getBytes());
                    }

                    // 请求结束标志
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                    outStream.write(end_data);
                    outStream.flush();
                    /**
                     * 获取响应码 200=成功 当响应成功，获取响应的流
                     */
                    int responeseCode = conn.getResponseCode();
                    if (responeseCode == 200) {
                        JSONObject js = null;
                        //(5)得到数据流
                        in = new InputStreamReader(conn.getInputStream(), "utf-8");
                        int ch;
                        StringBuilder sb3 = new StringBuilder();
                        while ((ch = in.read()) != -1) {
                            sb3.append((char) ch);
                        }
                        js = new JSONObject(sb3.toString());
                        return js.toString();
                    } else if (399 < responeseCode && responeseCode < 500) {// 请求无响应拒绝等
                        return String.valueOf(Constant.NO_RESPONSE);
                    } else if (500 <= responeseCode && responeseCode < 600) {// 服务器出错出现异常
                        return String.valueOf(Constant.S_EXCEPTION);
                    } else {// 其它异常
                        return String.valueOf(Constant.RESPONESE_EXCEPTION);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    return String.valueOf(Constant.RESPONESE_EXCEPTION);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // 网络状态是否可用
                    if (!NetUtils.isHasNet(datas.getContext())) {
                        return String.valueOf(Constant.NO_NETWORK);// 无可用网络
                    } else {
                        e.printStackTrace();
                        return String.valueOf(Constant.RESPONESE_EXCEPTION);
                    }

                }
                return null;
            }

            @Override
            public void beforTask() {
                callback.beforeUpload();
                super.beforTask();
            }

            @Override
            public void afterTask(String result) {
                callback.afterUpload(result);
                super.afterTask(result);

            }
        };
        ThreadPoolService.execute(customRunnable);
        return customRunnable;
    }

    /**
     * 发送网络请求，普通参数上传
     *
     * @param datas    请求参数集合
     * @param context  上下文
     * @param callback 回调
     * @return
     */
    public static HttpAsyncTask sendRequest(ParamsDatas datas, Context context, HttpAsyncTask.TaskCallBack callback) {
        HttpAsyncTask task = new HttpAsyncTask(callback, context);
        task.execute(datas);
        return task;
    }

    /**
     * 发送网络请求,只能传参数，同上
     *
     * @param datas     请求参数集合
     * @param context   上下文
     * @param dialogStr 进度文字
     * @param callback  回调
     * @return
     */
    public static HttpAsyncTask sendRequest(Context context, ParamsDatas datas, String dialogStr, HttpAsyncTask.TaskCallBack callback) {
        LogUtils.d("NetUtils-", "sendRequest");
        HttpAsyncTask task = new HttpAsyncTask(callback, context, dialogStr);
        task.execute(datas);
        return task;
    }

    public interface UploadCallback {
        public void beforeUpload();

        public void afterUpload(String response);
    }


}
