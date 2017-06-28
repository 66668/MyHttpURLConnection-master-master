package com.example;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.type1.UserHelper;
import com.example.type1.load.Loading;
import com.example.type1.model.TextModel;
import com.example.type1.utils.MyException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 该类讲解 所有的关于访问http的类型,和处理数据与UI的handler两种写法
 * <p>
 * Created by sjy on 2017/6/27.
 */

public class TypeOneActivity extends AppCompatActivity {
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
     * 方式1获取数据，handler处理数据与UI的交互
     * 这里只举例了获取list<entity>和泛型使用，具体其他情况都类似，想要看到结果，就用你们公司的url测试，我这小公司没有阿里云测试的url
     */
    private void loadType1() {

        /**
         * (1)获取list<entity>方式
         *
         * 处理数据用的内部类handler
         *
         */

        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                try {
                    ArrayList<TextModel> model = UserHelper.postConferenceText(TypeOneActivity.this);
                    if (model != null) {
                        Log.d(TAG, "run: model.getPhotourl()=" + model.get(1).getPhotourl());
                    }
                    handler.sendMessage(handler.obtainMessage(1, model));

                } catch (MyException e) {
                    handler.sendMessage(handler.obtainMessage(2, e.getMessage()));
                }
            }
        });

        /**
         *  (2)使用泛型
         *  处理数据用的静态内部类+弱引用的handler,之所有有这种handler，是因为优化时，测试出内存泄漏的情况，这种写法就可以避免泄漏
         *
         */
        final Message msg = new Message();
        Loading.run(this, new Runnable() {
            @Override
            public void run() {
                //泛型
                try {
                    TextModel model2 = new UserHelper<>(TextModel.class)
                            .applicationDetailPost(TypeOneActivity.this,
                                    "",
                                    "");
                    //方式1：
                    mHandler.sendMessage(mHandler.obtainMessage(3, model2));

                    //方式2：
                    //                    msg.obj = list;
                    //                    msg.what = 3;// 1001
                    //                    mHandler.sendMessage(msg);
                } catch (MyException e) {

                    mHandler.sendMessage(mHandler.obtainMessage(4, e.getMessage()));
                }
            }
        });

    }

    /**
     * 内部类
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv1.setText("获取" + ((ArrayList<TextModel>) msg.obj).size() + "条数据");
                    break;
            }
        }
    };

    /**
     * act中 静态+弱引用 避免内存泄漏
     * <p>
     * frament的写法类似，就不写了
     */
    private final MyHandler mHandler = new MyHandler(TypeOneActivity.this);

    private static class MyHandler extends Handler {
        private final WeakReference<TypeOneActivity> mActivity;

        public MyHandler(TypeOneActivity activity) {
            mActivity = new WeakReference<TypeOneActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            TypeOneActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 3://服务端数据处理
                        //
                        break;
                    case 4:
                        break;
                }
            }

        }
    }


}
