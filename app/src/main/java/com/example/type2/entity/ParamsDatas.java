package com.example.type2.entity;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

/**
 * 封装的http请求参数
 * <p>
 * 一般参数
 *
 * @author lanhaizhong
 */
public class ParamsDatas extends HashMap<String, String> {
    private static final long serialVersionUID = 1L;
    private Context context;
    private File[] files;
    private String url;// 请求URL
    private boolean isPost;// 是否是POST请求

    //链式创建
    public static ParamsDatas create(Context context, String url) {
        ParamsDatas p = new ParamsDatas(context, url);
        return p;
    }

    public static ParamsDatas create(Context context, String url, boolean isPost) {
        ParamsDatas p = new ParamsDatas(context, url, isPost);
        return p;
    }

    public static ParamsDatas create(Context context, String url, File[] fileName) {
        ParamsDatas p = new ParamsDatas(context, url, true, fileName);
        return p;
    }


    //构造

    //默认post
    public ParamsDatas(Context context, String url) {
        this(context, url, true);
    }

    public ParamsDatas(Context context, String url, boolean isPost) {
        this.context = context;
        this.url = url;
        this.isPost = isPost;
        this.files = null;
    }

    public ParamsDatas(Context context, String url, boolean isPost, File[] fileName) {
        this.context = context;
        this.files = fileName;
        this.url = url;
        this.isPost = isPost;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public File[] getFiles() {
        return files;
    }

    public void setFiles(File[] files) {
        this.files = files;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean isPost) {
        this.isPost = isPost;
    }


    //map集合的put方法
    public ParamsDatas add(String key, String value) {
        this.put(key, value);
        return this;
    }

    //方法重载
    public ParamsDatas add(String key, int value) {
        this.put(key, "" + value);
        return this;
    }

    //	public ArrayList<BasicNameValuePair> getParamList() {
    //		return paramList;
    //	}
    //
    //	public void putParam(String name, String value) {
    //		if (paramList == null) {
    //			paramList = new ArrayList<BasicNameValuePair>();
    //		}
    //		paramList.add(new BasicNameValuePair(name, value));
    //	}
}
