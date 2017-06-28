package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * 方式一：
 * 个人封装网络框架：(公司的json返回格式：{code:"0/1",message:"",result:""})
 * 1.使用httpURLConnection底层
 * 2.gson jar包解析json，可封装成 entity实体类，list<entity>,String
 * 3.自定义异常，act处理
 * 4.有自定义progress显示和消失
 *
 * 有缺点：1.back键无法取消访问网络，正在改正
 * 2.在fragment/activity中使用时，一般处理数据的handler需要写成 静态内部类+弱引用，防止内存泄漏
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
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
                Intent intent = new Intent(MainActivity.this, TypeOneActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        btn1 = (Button) findViewById(R.id.btn_type1);
        btn2 = (Button) findViewById(R.id.btn_type2);
        tv1 = (TextView) findViewById(R.id.tv_type1);
        tv2 = (TextView) findViewById(R.id.tv_type2);
    }


}
