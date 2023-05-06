package com.k.service.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.CharEncoding;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Map.Entry;

/**
 * httpclient访问http接口的工具类
 *
 * @author navies
 */
public class HttpClientUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);
    private static Map<String, String> headers = new HashMap<>();

    static {
        headers.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
        headers.put("Accept-Language", "zh-cn,zh;q=0.5");
        headers.put("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
        headers.put("Accept", " image/gif, image/x-xbitmap, image/jpeg, " +
                              "image/pjpeg, application/x-silverlight, application/vnd.ms-excel, " +
                              "application/vnd.ms-powerpoint, application/msword, application/x-shockwave-flash, */*");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Accept-Encoding", "gzip, deflate");
    }

    private HttpClientUtils() {
    }

    public static String httpPost(String url, Map<String, Object> param, Map<String, String> headers) {

        HttpPost httpPost;
        HttpResponse response;
        HttpEntity entity;
        String result = "";
        RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(10000) // 读取超时
                                                          .setConnectTimeout(10000) // 连接超时
                                                          .setConnectionRequestTimeout(10000).build();

        try (CloseableHttpClient httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig)
                                                         .build()) {

            httpPost = new HttpPost(url);
            // 设置各种头信息
            for (Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            // 传入各种参数
            String postParams = JSONObject.toJSONString(param);
            LOGGER.info("参数为：{}----", postParams);
            HttpEntity httpEntity = new StringEntity(postParams);
            httpPost.setEntity(httpEntity);
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.info("HttpStatus ERROR" + "Method failed: " + response.getStatusLine());
                return "";
            } else {
                entity = response.getEntity();
                if (null != entity) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    result = new String(bytes, StandardCharsets.UTF_8);
                } else {
                    LOGGER.info("httpPost URL [{}],httpEntity is null.", url);
                }
                return result;
            }
        } catch (Exception e) {
            LOGGER.warn("httpPost URL [{}] warn", url, e);
            return "";
        }
    }

    /**
     * 异常或者没拿到返回结果的情况下,result为""
     *
     * @param url
     * @param param
     * @return
     */
    public static String httpPost(String url, Map<String, Object> param) {
        HttpPost httpPost;
        HttpResponse response;
        HttpEntity entity;
        String result = "";
        StringBuilder suf = new StringBuilder();
        try (DefaultHttpClient httpclient = new DefaultHttpClient()) {
            // 设置cookie的兼容性---考虑是否需要
            httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
            httpPost = new HttpPost(url);
            // 设置各种头信息
            for (Entry<String, String> entry : headers.entrySet()) {
                httpPost.setHeader(entry.getKey(), entry.getValue());
            }
            List<NameValuePair> nvps = new ArrayList<>();
            // 传入各种参数
            if (null != param) {
                for (Entry<String, Object> set : param.entrySet()) {
                    String key = set.getKey();
                    String value = set.getValue() == null ? "" : set.getValue().toString();
                    nvps.add(new BasicNameValuePair(key, value));
                    suf.append(" [" + key + "-" + value + "] ");
                }
            }
            LOGGER.debug("param ", suf.toString());
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            response = httpclient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                LOGGER.debug("HttpStatus ERROR Method failed: ", response.getStatusLine());
                return "";
            } else {
                entity = response.getEntity();
                if (null != entity) {
                    byte[] bytes = EntityUtils.toByteArray(entity);
                    result = new String(bytes, CharEncoding.UTF_8);
                } else {
                    LOGGER.debug("httpPost URL [{}],httpEntity is null.", url);
                }
                return result;
            }
        } catch (Exception e) {
            LOGGER.debug("httpPost URL [{}] error", url);
            return "";
        } finally {
            LOGGER.debug("RESULT:  [{}]", result);
            LOGGER.debug("httpPost URL [{}] end ", url);
        }
    }

    public static String sendPost(String url, String param, String method) {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestMethod(method);
            connection.setRequestProperty("content-type", "application/json");
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(10500);
            connection.setRequestProperty("Accept-Language", Locale.getDefault().toString());
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("accept", "*/*");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(connection.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("发送 POST 请求出现异常！", e);
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                LOGGER.error("异常：", ex);
            }
        }
        return result.toString();
    }
}
