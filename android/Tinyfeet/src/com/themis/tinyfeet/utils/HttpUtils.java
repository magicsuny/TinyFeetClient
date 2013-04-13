package com.themis.tinyfeet.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-10
 * Time: 下午9:38
 * To change this template use File | Settings | File Templates.
 */
public class HttpUtils {
    private static final String TAG = "HttpUtils";

    /**
     * 网络连接是否可用
     */
    public static boolean isConnnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivityManager) {
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();

            if (null != networkInfo) {
                for (NetworkInfo info : networkInfo) {
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        Log.e(TAG, "the net is ok");
                        return true;
                    }
                }
            }
        }
        Toast.makeText(context, "网络连接失败", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 网络可用状态下，通过get方式向server端发送请求，并返回响应数据
     *
     * @param url     请求网址
     * @param context 上下文
     * @return 响应数据
     */
    public static String getResponseForGet(String url, Map<String, String> reqHeaders, Context context) {
        if (isConnnected(context)) {
            return getResponseForGet(url, reqHeaders);
        }
        return null;
    }

    /**
     * 通过Get方式处理请求，并返回相应数据
     *
     * @param url 请求网址
     * @return 响应的JSON数据
     */
    public static String getResponseForGet(String url, Map<String, String> reqHeaders) {
        HttpGet httpRequest = new HttpGet(url);
        if (reqHeaders != null) {
            for (Map.Entry<String, String> header : reqHeaders.entrySet()) {
                httpRequest.addHeader(header.getKey(), header.getValue());
            }
        }
        return getRespose(httpRequest);
    }

    /**
     * 网络可用状态下，通过post方式向server端发送请求，并返回响应数据
     *
     * @param url       请求网址
     * @param reqParams 参数信息
     * @param context   上下文
     * @return 响应数据
     */
    public static String getResponseForPost(String url, JSONObject reqParams, Map<String, String> reqHeaders, Context context) {
        if (isConnnected(context)) {
            return getResponseForPost(url, reqParams, reqHeaders);
        }
        return null;
    }

    /**
     * 通过post方式向服务器发送请求，并返回响应数据
     *
     * @param url       请求网址
     * @param reqParams 参数信息
     * @return 响应数据
     */
    public static String getResponseForPost(String url, JSONObject reqParams, Map<String, String> reqHeaders) {
        if (null == url || "" == url) {
            return null;
        }
        HttpPost request = new HttpPost(url);
        if (reqHeaders != null) {
            for (Map.Entry<String, String> header : reqHeaders.entrySet()) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }
        try {

            request.setEntity(new StringEntity(reqParams.toString()));
            return getRespose(request);
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    /**
     * 响应客户端请求
     *
     * @param request 客户端请求get/post
     * @return 响应数据
     */
    public static String getRespose(HttpUriRequest request) {
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            switch (statusCode) {
                case HttpStatus.SC_OK://正常状态
                    String result = EntityUtils.toString(httpResponse.getEntity());
                    Log.i(TAG, "results=" + result);
                    return result;
                case HttpStatus.SC_NO_CONTENT://没查询到记录
                    Log.i(TAG, " 没有记录:" + request.getURI());
                    break;
                case HttpStatus.SC_FORBIDDEN://API验证失败
                    Log.e(TAG, " API验证失败:" + request.getURI());
                    break;
                case HttpStatus.SC_EXPECTATION_FAILED://操作失败
                    Log.e(TAG, " 操作失败:" + request.getURI());
                    break;
                case HttpStatus.SC_INTERNAL_SERVER_ERROR://其他错误
                    Log.e(TAG, " 其他错误:" + request.getURI());
                    break;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
