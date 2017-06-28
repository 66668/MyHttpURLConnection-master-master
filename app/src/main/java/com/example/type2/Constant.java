package com.example.type2;

/**
 * @Title: Constant.java
 * @Description: 常量集合类 封装一些常量
 * @author lanhaizhong
 * @date 2013年7月22日 上午10:24:45
 * @version V1.0 Copyright (c) 2013 Company,Inc. All Rights Reserved.
 * 
 */
public class Constant {
	// http请求状态码标志
	public final static int STATAS_OK = 200;// 请求OK
	public final static int NO_RESPONSE = 400;// 请求无响应 找不到响应资源
	public final static int S_EXCEPTION = 500;// 服务器出错
	public final static int RESPONESE_EXCEPTION = 160;// 响应异常
	public final static int TIMEOUT = 101;// 请求超时
	public final static int NO_NETWORK = 102;// 没用可用网络
	public final static int NULLPARAMEXCEPTION = 103;// 参数为空异常
	public final static int RESPONSE_OK = 2000;
	public final static int RELOGIN = 4001;
	public final static int SERVER_EXCEPTION = 5001;
	public final static int NULLPARAM=4006;//参数值为空
	public final static int LOSEPARAM=4005;//缺少参数
	public final static String SAVAVERSIONCODE="savaVersionCode";//保存最后上一次运行用的版本
	// 访问的主机
	public final static String HOST = "http://api.qcbis.com";
	public final static String USERAPI = HOST + "/User.aspx";
	public final static String PROJECTAPI = HOST + "/Project.aspx";
	public final static String LOANAPI = HOST + "/Loan.aspx";

	public final static String DOWNLOADURL="http://api.qcbis.com/Download.aspx/";
	// 保存图片的文件夹
	public final static String ROOT = "OrongImages";
	// 保存头像
	public final static String USERICFODER = ROOT + "/user";
	// 保存项目图片
	public final static String PROJECTIMG = ROOT + "/project";

	// sharedPeferences
	public final static String ORPREFERENCES = "orongConfig";
	public final static String ISSAVEPW = "isSavepassword";
	public final static String USERNAME = "userName";
	public final static String PASSWORD = "passWord";
	public final static String USERICONPATH = "usericonpath";// 用户图像保存的url  =qrcode+path;//用户的url+存储路径
	// end sharedPeferences

	// 加密密码
	public final static String ENCODEPASSWORD = "AAAcom.orongaaa";
	//
	public final static int PAGESIZE = 12;
	
	public final static Long MAXFIESZISE=1048576L*6L; //最多可保存6M
	public final static int MAXFIESNUM=80;//最多允许保存80张图片
	public final static int DELFIESNUM=30;//每次删除的图片数量

}
