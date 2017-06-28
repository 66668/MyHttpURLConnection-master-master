package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.type2.Constant;
import com.example.type2.entity.HttpDatas;
import com.example.type2.entity.UserInfo;
import com.example.type2.utils.JSONUtil;
import com.example.type2.utils.net.HttpAsyncTask;
import com.example.type2.utils.net.NetUtils;

import org.json.JSONException;
import org.json.JSONObject;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadType1();
            }
        });
    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn_type1);
        btn2 = (Button) findViewById(R.id.btn_type2);
        tv1 = (TextView) findViewById(R.id.tv_type1);
        tv2 = (TextView) findViewById(R.id.tv_type2);
    }

    /**
     *
     */

    private UserInfo user = null;
    private void loadType1() {
        /**
         * post 多参数
         */

        //保存的参数
        HttpDatas datas = new HttpDatas(Constant.USERAPI);

        datas.putParam("storeId", "linquan");
        datas.putParam("workId", "chenhuilin");
        datas.putParam("password", "123456");
        NetUtils.sendRequest(datas, TypeTwoActivity.this, getString(R.string.login_now), new HttpAsyncTask.TaskCallBack() {

            @Override
            public int excueHttpResponse(String strResponds) {
                System.out.println(strResponds);
                int code = 0;
                JSONObject jsonObject = JSONUtil.instaceJsonObject(strResponds);
                if (jsonObject != null) {
                    try {
                        code = jsonObject.getInt("code");
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
                if (code == 2000) {
                    user = JSONUtil.jsonObject2Bean(jsonObject, UserInfo.class);

                    return Constant.STATAS_OK;
                }
                return code;

            }

            @Override
            public void beforeTask() {
            }

            @Override
            public void afterTask(int result) {
                System.out.println(result);
                switch (result) {
                    case Constant.STATAS_OK:
                        Toast.makeText(getApplicationContext(), TypeTwoActivity.this.getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TypeTwoActivity.this, MainActivity.class);
                        intent.putExtra("User", user);
                        startActivity(intent);
                        break;
                    // case 5000:
                    // Toast.makeText(getApplicationContext(),
                    // getString(R.string.error_pw_or_user), 0).show();
                    // break;
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


}
