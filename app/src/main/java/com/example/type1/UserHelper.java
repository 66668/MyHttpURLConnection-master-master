package com.example.type1;

import android.content.Context;

import com.example.R;
import com.example.type1.http.APIUtils;
import com.example.type1.http.HttpParameter;
import com.example.type1.http.HttpResult;
import com.example.type1.http.WebUrl;
import com.example.type1.model.StoreEmployeeModel;
import com.example.type1.model.TextModel;
import com.example.type1.model.VisitorBModel;
import com.example.type1.utils.LogUtils;
import com.example.type1.utils.MyException;
import com.example.type1.utils.NetworkManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户管理者帮助类，所有访问网络出现的可能都包含在一下方法中
 * <p/>
 * 处理 服务端数据
 * 处理方式为：（1）post：多个参数，jsonobject参数，图片上传，还包括泛型post
 * （2）get
 * <p>
 * <p>
 * 将数据 封装成 string（int等基本类型）,arraylist,jsonobject,list<entity>,entity实体 这五大返回类型
 *
 * @author JackSong
 */
public class UserHelper<T> {

    //********************************************************get的几种情况分析******************************************************************************

    /**
     * (1)get
     * <p>
     * 返回entity
     */

    public static StoreEmployeeModel getEmployeeListNameByStoreID(Context context, String storeId, String typeN) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        //        String newUrl = new String(WebUrl.UserManager.GET_RESPONDENTS + storeId + "/" + typeN);
        String newUrl = new String(WebUrl.GET_EMPLOYEELISTNAME + storeId + "/" + typeN);
        HttpResult httpResult = APIUtils.getForObject(newUrl);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonObject.toString(), StoreEmployeeModel.class);
    }

    /**
     * (2)get方式
     * <p>
     * 返回lsit<entity>
     */

    public static ArrayList<StoreEmployeeModel> getText(Context context) throws MyException {
        //
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        String newUrl = new String(WebUrl.GET_EMPLOYEELISTNAME + "" + "/" + "");
        HttpResult httpResult = APIUtils.getForObject(newUrl);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(),
                new TypeToken<ArrayList<StoreEmployeeModel>>() {
                }.getType());
    }


    /**
     * (3)get
     * <p>
     * 返回jsonarry
     */
    public static JSONArray getEmployeeListByStoreID(Context context, String storeId, String typeN) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        String newUrl = new String(WebUrl.GET_RESPONDENTS + storeId + "/" + typeN);
        HttpResult httpResult = APIUtils.getForObject(newUrl);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return httpResult.jsonArray;
    }

    //(4)get,返回jsonobject等情况不写也知道，都在httpResult中


    //********************************************************post的几种情况分析******************************************************************************


    /**
     * (1)post 1个参数
     * <p>
     * 获取 list<entity>数据
     */

    public static ArrayList<TextModel> postConferenceText(Context context) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context))
            throw new MyException(R.string.network_invalid);

        //        String newUrl = new String(WebUrl.UserManager.GET_RESPONDENTS + storeId + "/" + typeN);
        String newUrl = new String("http://59.110.26.83:8096/openapi/ConferenceInfo/GetConferenceInfo");
        HttpResult httpResult = APIUtils.postForObject(newUrl, new HttpParameter().create().add("storeID", "fa3df134-7fc7-4623-879e-24165d286568"));

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return (new Gson()).fromJson(httpResult.jsonArray.toString(),
                new TypeToken<ArrayList<TextModel>>() {
                }.getType());
    }

    /**
     * (2) post 多个参数
     * <p>
     * 获取 list<entity>数据
     *
     * @param context
     * @param pageIndex
     * @param pageSize
     * @param timespan
     * @param storeID
     * @return
     * @throws MyException
     */
    public static ArrayList<VisitorBModel> getVisitorRecordsByPage(Context context,
                                                                   int pageIndex,//List<VisitorBModel>
                                                                   int pageSize,
                                                                   int timespan,
                                                                   String storeID) throws MyException {

        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络
        }

        HttpResult httpResult = APIUtils.postForObject(WebUrl.GET_RRECORD_BYPAGE,
                HttpParameter.create().
                        add("pageIndex", pageIndex + "").
                        add("pageSize", pageSize + "").
                        add("timespan", timespan + "").
                        add("storeID", storeID));

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }
        //返回list形式
        return (new Gson()).fromJson(httpResult.jsonArray.toString(),
                new TypeToken<List<VisitorBModel>>() {
                }.getType());
    }


    /**
     * (3) post 多个参数
     * <p>
     * 返回 String
     * <p/>
     * HttpPost方式 返回值 {"code":"1","message":"保存成功！","result":""}
     */
    public static String changePassword(Context context, String oldUserPassword,
                                        String newUserPassword) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络
        }

        HttpResult httpResult = APIUtils.postForObject(WebUrl.CHANGE_PASSWORD,
                HttpParameter.create().add("storeUserID", "").// 登录时，返回的storeUserID ??
                        add("oldUserPassword", oldUserPassword).
                        add("newUserPassword", newUserPassword));
        if (httpResult.hasError()) {
            throw httpResult.getError();
        }
        return httpResult.Message;
    }

    /**
     * (4) post
     * <p>
     * jsonobject+一张图片
     * <p>
     * <p>
     * 后台接收key为obj，value为 parameters情况，
     * <p>
     * 返回 String
     *
     * @param context
     * @param parameters 所有参数都保存在 JSONObject中
     * @param fileName
     * @return
     * @throws MyException
     */

    public static String addOneVisitorRecord(Context context, String parameters, File fileName) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        HttpResult hr = APIUtils.postForObject(WebUrl.ADD_VISITORRECORD,
                HttpParameter.create().add("obj", parameters),
                fileName);

        if (hr.hasError()) {
            throw hr.getError();
        }

        return hr.Message;
    }

    /**
     * (5) post
     * <p>
     * 多个参数+一张图片
     * <p>
     * 返回 code值
     */

    public static int registerNew(Context context, HttpParameter params, File picPath) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }

        // 访问服务端：http://192.168.1.127:2016/api/Attend/RegAttFace
        HttpResult httpResult = APIUtils.postForObject(WebUrl.POST_NEW_EMPLOYEE, params, picPath);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }

        return httpResult.code;
    }

    /**
     * (6) post 多个参数
     * <p>
     * 返回 result的string格式的值
     * <p>
     * {"code":"1","message":"保存成功！","result":""}
     *
     * @throws MyException
     * @throws JSONException
     * @throws IOException
     */

    public static String registerOld(Context context, HttpParameter params, File picPath) throws MyException {
        // 判断否有网络连接，有网络连接，不抛异常，无连接，抛异常(logcat)
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);// 亲，您的网络不给力，请检查网络！
        }

        HttpResult httpResult = APIUtils.postForObject(WebUrl.POST_OLD_EMPLOYEE, params, picPath);

        if (httpResult.hasError()) {
            throw httpResult.getError();
        }
        return httpResult.resultJsonString;//解析result的数据-- 祖册成功
    }

    /**
     * (7)post
     * 返回各种 model
     * <p>
     * 注：使用泛型
     */
    Class<T> clz;

    public UserHelper(Class<T> clz) {
        this.clz = clz;
    }

    public T applicationDetailPost(Context context, String ApplicationID, String ApplicationType) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            HttpResult httpResult = APIUtils.postForObject(WebUrl.GET_OLD_EMPLOYEE_DETAILS,
                    HttpParameter.create()
                            .add("ApplicationID", ApplicationID)
                            .add("ApplicationType", ApplicationType)
                            .add("StoreID", "")
                            .add("EmployeeID", ""));
            if (httpResult == null) {
                throw new MyException("访问的url没有返回值或者url不正确");
            }
            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
            LogUtils.d("HTTP", httpResult.jsonObject.toString());

            return (new Gson()).fromJson(httpResult.jsonObject.toString(), clz);

        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }
    /**
     * (8) post （obj形式上传）
     *
     * 无返回
     * <p></>
     *
     * @param context
     * @param js
     * @throws MyException
     */
    public static void workoverTimePost(Context context, JSONObject js) throws MyException {
        if (!NetworkManager.isNetworkAvailable(context)) {
            throw new MyException(R.string.network_invalid);
        }
        try {
            /**
             * 参数保存成json 9参数
             */
            js.put("StoreID", "");
            js.put("EmployeeID", "");

            HttpResult httpResult = APIUtils.postForObject(WebUrl.GET_OLD_EMPLOYEE_DETAILS,
                    HttpParameter.create().add("obj", js.toString()));

            if (httpResult.hasError()) {
                throw httpResult.getError();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MyException e) {
            throw new MyException(e.getMessage());
        }
    }

}
