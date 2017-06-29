package com.example.type2.utils.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.R;
import com.example.type1.utils.LogUtils;
import com.example.type2.Constant;
import com.example.type2.entity.ParamsDatas;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

/**
 * 静态内部类 http异步请求类
 */
public class HttpAsyncTask extends AsyncTask<ParamsDatas, Void, Integer> {
    private TaskCallBack callback;
    private Context context;
    private ProgressDialog dialog;
    private String dialogStr;

    /**
     * 构造函数
     *
     * @param callback 回调函数
     */
    public HttpAsyncTask(TaskCallBack callback, Context context) {
        super();
        this.callback = callback;
        this.context = context;
    }

    /**
     * 构造函数
     *
     * @param callback 回调函数
     */
    public HttpAsyncTask(TaskCallBack callback, Context context, String dialogStr) {
        super();
        LogUtils.d("HttpAsyncTask", "自定义AsyncTask构造");
        this.callback = callback;
        this.context = context;
        this.dialogStr = dialogStr;

    }

    @Override
    protected Integer doInBackground(ParamsDatas... params) {
        LogUtils.d("HttpAsyncTask", "02doInBackground,回调excueHttpResponse,return 的值由act的onPostExecute处理");

        //变量
        DataOutputStream outStream = null;
        InputStreamReader in = null;
        HttpURLConnection conn = null;
        ParamsDatas datas = null;

        //get
        InputStream is = null;
        ByteArrayOutputStream baos = null;

        //常量
        String BOUNDARY = java.util.UUID.randomUUID().toString();// 边界标识 随机生成
        String PREFIX = "--";
        String LINEND = "\r\n";
        String MULTIPART_FORM_DATA = "multipart/form-data";// 内容类型
        String CHARSET = "UTF-8";

        //参数判断
        if (params != null) {
            datas = params[0];
        } else {
            return Constant.NULLPARAMEXCEPTION;//空参数
        }

        try {
            //(1)创建url对象
            URL url = new URL(datas.getUrl());
            LogUtils.d("HttpAsyncTask", "url=" + datas.getUrl());
            //(2)利用HttpURLConnectioncont从网络中获取数据
            conn = (HttpURLConnection) url.openConnection();
            //(3)设置连接要求
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);// 缓存最长时间

            if (datas.isPost()) {//post连接要求
                LogUtils.d("HttpAsyncTask", "post设置");

                conn.setDoInput(true);// 允许输入
                conn.setDoOutput(true);// 允许输出
                conn.setUseCaches(false);// 不允许缓存，避免多余问题
                conn.setRequestMethod("POST");// 请求方式
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("Charset", "UTF-8");// 设置编码
                conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + ";boundary=" + BOUNDARY);
            } else {//get链接要求
                LogUtils.d("HttpAsyncTask", "get设置");
                conn.setRequestMethod("GET");// get
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
            }
            conn.connect();

            int responeseCode = 0;

            if (datas.isPost()) {//处理post
                LogUtils.d("HttpAsyncTask", "post");

                /**
                 * 首先组拼文本类型参数
                 */
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, String> entry : datas.entrySet()) {
                    sb.append(PREFIX);//--
                    sb.append(BOUNDARY);
                    sb.append(LINEND);//\r\n
                    sb.append("Content-Disposition:form-data;name=\"" + entry.getKey() + "\"" + LINEND);
                    sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
                    sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
                    sb.append(LINEND);
                    sb.append(entry.getValue());
                    sb.append(LINEND);
                    LogUtils.d("参数", entry.getValue());
                }
                // 获取输出流
                outStream = new DataOutputStream(conn.getOutputStream());
                outStream.write(sb.toString().getBytes());

                /**
                 * 拼接文件数据
                 */
                int filesLen = datas.getFiles().length;
                for (int i = 0; i < filesLen; i++) {
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
                    InputStream ist = new FileInputStream(datas.getFiles()[i]);
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = ist.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    ist.close();
                    outStream.write(LINEND.getBytes());
                }
                // 请求结束标志
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
                outStream.write(end_data);
                outStream.flush();

                //响应
                responeseCode = conn.getResponseCode();
                if (responeseCode == 200) {

                    //(5)得到数据流
                    in = new InputStreamReader(conn.getInputStream(), "utf-8");
                    int ch;
                    StringBuilder sb3 = new StringBuilder();

                    while ((ch = in.read()) != -1) {
                        sb3.append((char) ch);
                    }
                    JSONObject jsonObject = new JSONObject(sb3.toString());

                    return callback.excueHttpResponse(jsonObject.toString());
                } else if (399 < responeseCode && responeseCode < 500) {// 请求无响应拒绝等
                    return Constant.NO_RESPONSE;
                } else if (500 <= responeseCode && responeseCode < 600) {// 服务器出错出现异常
                    return Constant.S_EXCEPTION;
                } else {// 其它异常
                    LogUtils.d("HttpAsyncTask", "doInBackground--其他异常--responeseCode=" + responeseCode);
                    return Constant.RESPONESE_EXCEPTION;
                }
            } else {//处理 get请求
                LogUtils.d("HttpAsyncTask", "get");
                responeseCode = conn.getResponseCode();
                //响应
                if (responeseCode == 200) {
                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    int len = -1;
                    byte[] buf = new byte[128];

                    while ((len = is.read(buf)) != -1) {
                        baos.write(buf, 0, len);
                    }
                    baos.flush();
                    LogUtils.d("HTTP", baos.toString());
                    JSONObject jsonObject = new JSONObject(baos.toString());
                    return callback.excueHttpResponse(jsonObject.toString());

                } else if (399 < responeseCode && responeseCode < 500) {// 请求无响应拒绝等
                    return Constant.NO_RESPONSE;
                } else if (500 <= responeseCode && responeseCode < 600) {// 服务器出错出现异常
                    return Constant.S_EXCEPTION;
                } else {// 其它异常
                    LogUtils.d("HttpAsyncTask", "doInBackground--其他异常--responeseCode=" + responeseCode);
                    return Constant.RESPONESE_EXCEPTION;
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            e.printStackTrace();
            return Constant.TIMEOUT;
        } catch (UnknownHostException e) {
            // 网络状态是否可用
            if (!NetUtils.isHasNet(context)) {
                return Constant.NO_NETWORK;// 无可用网络
            } else {
                e.printStackTrace();
                return Constant.RESPONESE_EXCEPTION;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return Constant.RESPONESE_EXCEPTION;
        } finally {
            //get
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (baos != null)
                    baos.close();
            } catch (IOException e) {
            }
            //post
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
            }
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
            // 关闭连接
            conn.disconnect();
        }
        return 0;
    }

    protected void onPreExecute() {
        LogUtils.d("HttpAsyncTask", "01onPreExecute，dialog显示，中断访问操作");
        if (dialogStr != null && !"".equals(dialogStr)) {
            dialog = new ProgressDialog(context) {
                @Override
                public void cancel() {
                    if (!HttpAsyncTask.this.isCancelled()) {
                        HttpAsyncTask.this.cancel(true);
                    }
                    Toast.makeText(context, context.getString(R.string.cancelrequest), Toast.LENGTH_SHORT).show();
                    super.cancel();
                }
            };
            dialog.setMessage(dialogStr);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        callback.beforeTask();
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Integer result) {
        LogUtils.d("HttpAsyncTask", "03onPostExecute，回调afterTask");
        LogUtils.d("HttpAsyncTask", "03onPostExecute，result=" + result);
        callback.afterTask(result);
        if (dialog != null) {
            dialog.dismiss();
        }
        super.onPostExecute(result);
    }

    /**
     * http一步请求的回调函数 接口
     *
     * @author lanhaizhong
     */
    public interface TaskCallBack {

        /**
         * 任务启动前的操作
         */
        public void beforeTask();

        /**
         * 后台执行http 请求得到相应后的处理
         *
         * @param respondsStr 相应实体的字符串
         * @return 状态码
         */
        public int excueHttpResponse(String respondsStr);

        /**
         * 任务结束会的操作
         *
         * @param result
         */
        public void afterTask(int result);

    }


}
