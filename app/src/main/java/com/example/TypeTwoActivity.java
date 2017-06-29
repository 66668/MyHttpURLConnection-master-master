package com.example;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.type1.utils.LogUtils;
import com.example.type2.Constant;
import com.example.type2.entity.ParamsDatas;
import com.example.type2.entity.UserInfo;
import com.example.type2.utils.CustomRunnable;
import com.example.type2.utils.FileUitls;
import com.example.type2.utils.JSONUtil;
import com.example.type2.utils.ThreadPoolService;
import com.example.type2.utils.net.HttpAsyncTask;
import com.example.type2.utils.net.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * 该类讲解 所有的关于访问http的类型,和处理数据与UI的handler两种写法
 * <p>
 * Created by sjy on 2017/6/27.
 */

public class TypeTwoActivity extends AppCompatActivity {
    private static final String TAG = "TypeOneActivity";
    Button btn1;
    Button btn2;
    TextView tv1;
    TextView tv2;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type2);
        initView();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadType1();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadType2();
            }
        });
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn_type1);
        btn2 = (Button) findViewById(R.id.btn_type2);
        tv1 = (TextView) findViewById(R.id.tv_type1);
        tv2 = (TextView) findViewById(R.id.tv_type2);
        img = (ImageView) findViewById(R.id.img);
    }

    /**
     * post 多参数 演示登录情况
     */

    private UserInfo user = null;

    private void loadType1() {
        /**
         * post 多参数
         */

        //保存的参数,及url
        ParamsDatas datas = ParamsDatas.create(this,"http://59.110.26.83:8083/openapi/User/LoginByPassword")
                .add("storeId", "linquan")
                .add("workId", "chenhuilin")
                .add("password", "123456");
        NetUtils.sendRequest(TypeTwoActivity.this, datas, getString(R.string.login_now), new HttpAsyncTask.TaskCallBack() {

            @Override
            public int excueHttpResponse(String strResponds) {
                LogUtils.d("act响应", strResponds);
                int code = 0;
                JSONObject jsonObject = JSONUtil.instaceJsonObject(strResponds);
                LogUtils.d("act响应", "解析json=" + jsonObject.toString());
                if (jsonObject != null) {
                    try {
                        code = jsonObject.getInt("code");
                        LogUtils.d("act响应", "code=" + code);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (code == 1) {
                    user = JSONUtil.jsonObject2Bean(jsonObject, UserInfo.class);

                    return Constant.STATAS_CODEONE;
                }
                return code;

            }

            @Override
            public void beforeTask() {

            }

            @Override
            public void afterTask(int result) {
                LogUtils.d("act响应后", "result=" + result + "由HttpAsyncTask的onPostExecute方法传递过来");
                switch (result) {

                    case Constant.STATAS_CODEONE:
                        Toast.makeText(getApplicationContext(), TypeTwoActivity.this.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        break;

                    case 4004:
                        Toast.makeText(getApplicationContext(), getString(R.string.error_pw_or_user), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        MyApplication.showResultToast(result, TypeTwoActivity.this);
                        break;
                }
            }
        });


    }

    /**
     * 演示 参数+图片数组上传
     */
    private void loadType2() {
        final File[] file = null;//这里是图片路径
        //保存参数,文件及url
        ParamsDatas datas = ParamsDatas.create(this,"http://59.110.26.83:8083/openapi/User/LoginByPassword", file)
                .add("storeId", "linquan")
                .add("workId", "chenhuilin")
                .add("password", "123456");

        NetUtils.sendRequest(TypeTwoActivity.this, datas, getString(R.string.login_now), new HttpAsyncTask.TaskCallBack() {

            @Override
            public int excueHttpResponse(String strResponds) {
                LogUtils.d("act响应", strResponds);
                int code = 0;
                JSONObject jsonObject = JSONUtil.instaceJsonObject(strResponds);
                LogUtils.d("act响应", "解析json=" + jsonObject.toString());
                if (jsonObject != null) {
                    try {
                        code = jsonObject.getInt("code");
                        LogUtils.d("act响应", "code=" + code);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (code == 1) {
                    user = JSONUtil.jsonObject2Bean(jsonObject, UserInfo.class);

                    return Constant.STATAS_CODEONE;
                }
                return code;

            }

            @Override
            public void beforeTask() {

            }

            @Override
            public void afterTask(int result) {
                LogUtils.d("act响应后", "result=" + result + "由HttpAsyncTask的onPostExecute方法传递过来");
                switch (result) {

                    case Constant.STATAS_CODEONE:
                        Toast.makeText(getApplicationContext(), TypeTwoActivity.this.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        break;

                    case 4004:
                        Toast.makeText(getApplicationContext(), getString(R.string.error_pw_or_user), Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        MyApplication.showResultToast(result, TypeTwoActivity.this);
                        break;
                }
            }
        });


    }

    /**
     * 图片上传
     */
    private ProgressDialog progressDialog;

    private void type3() {

        String url = Constant.USERAPI;
        final File[] file = null;//这里是图片路径
        ParamsDatas datas = ParamsDatas.create(this,"url",file).add("method", "EditPicture");
        NetUtils.imageUpload(datas, new NetUtils.UploadCallback() {

            @Override
            public void beforeUpload() {
                progressDialog = new ProgressDialog(TypeTwoActivity.this);
                progressDialog.setMessage("图片上传中……");
                progressDialog.show();
            }

            @Override
            public void afterUpload(String response) {
                int code = 0;
                String picture = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    code = jsonObject.getInt("code");
                    if (code == Constant.STATAS_CODEONE) {
                        picture = jsonObject.getString("picture");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (code == Constant.STATAS_CODEONE) {
                    Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
                    // 保存用户图像新图片

                    // 拷贝文件，并显示

                    // 拷贝成功 在SharedPreferences保存拷贝路径

                } else {
                    Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
                ;
            }
        });
    }

    //演示 用线程池
    private void type4() {
        // 通过线程方式管理Sd卡下面的图片缓存
        ThreadPoolService.execute(new CustomRunnable<Void, Void>() {
            @Override
            public Void executeTask(Void... param) {
                File projectdirec = FileUitls.createDirectory(TypeTwoActivity.this, Constant.PROJECTIMG);
                FileUitls.deleteOldfiles(projectdirec, Constant.MAXFIESNUM, Constant.MAXFIESZISE, 1);
                return null;
            }
        });

        // 清除除开本用户外的其它用户图像
        if (user.getPicture() != null) {
            ThreadPoolService.execute(new CustomRunnable<Void, Void>() {
                @Override
                public Void executeTask(Void... param) {
                    File projectdirec = FileUitls.createDirectory(TypeTwoActivity.this, Constant.USERICFODER);
                    File safe = new File(projectdirec.getPath() + "/" + user.getPicture().substring(user.getPicture().lastIndexOf('/')));
                    FileUitls.deleteOtherfiles(projectdirec, safe);
                    return null;
                }

                @Override
                public void afterTask(Void aVoid) {
                    super.afterTask(aVoid);

                }
            });

        }
    }

}
