/**
 *
 */
package com.k.service.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http 请求基于jdk的封装，不依赖其他lib
 *
 * @version: v1.0
 * @author: Haixiang.Dai project: copyright: createTime: 2016/5/9 14:50 modifyTime: modifyBy:
 */
public class HttpKit {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpKit.class);
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36";
    private static final SSLSocketFactory sslSocketFactory = initSSLSocketFactory();
    private static final TrustAnyHostnameVerifier trustAnyHostnameVerifier = new TrustAnyHostnameVerifier();
    public static String defaultContentType = "application/json; charset=UTF-8";
    private static String CHARSET = "utf-8";

    private HttpKit() {
    }

    private static SSLSocketFactory initSSLSocketFactory() {
        try {
            TrustManager[] tm = {new TrustAnyTrustManager()};
            SSLContext sslContext = SSLContext.getInstance("TLS"); // ("TLS", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param url
     * @param queryParas
     * @return
     */
    public static String get(String url, Map<String, String> queryParas, String contentType) {
        return get(url, queryParas, null, contentType);
    }

    /**
     * send get request
     *
     * @param url
     * @param queryParas
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers,
                             String contentType) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlEncodedWithQueryString(url, queryParas), GET, headers, contentType);
            conn.connect();
            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers,
                                                       String contentType) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        if (conn instanceof HttpsURLConnection) {
            ((HttpsURLConnection) conn).setSSLSocketFactory(sslSocketFactory);
            ((HttpsURLConnection) conn).setHostnameVerifier(trustAnyHostnameVerifier);
        }

        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setConnectTimeout(19000);
        conn.setReadTimeout(19000);

        if (StringUtils.isBlank(contentType)) {
            contentType = defaultContentType;
        }
        conn.setRequestProperty("Content-Type", contentType);
        conn.setRequestProperty("User-Agent",
                                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");

        if (headers != null && !headers.isEmpty()) {
            for (Entry<String, String> entry : headers.entrySet()) {
                conn.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        return conn;
    }

    /**
     * Build queryString of the url
     */
    private static String buildUrlEncodedWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas == null || queryParas.isEmpty()) {
            return url;
        }

        StringBuilder sb = new StringBuilder(url);
        boolean isFirst;
        if (url.indexOf("?") == -1) {
            isFirst = true;
            sb.append("?");
        } else {
            isFirst = false;
        }

        for (Entry<String, String> entry : queryParas.entrySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append("&");
            }

            String key = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNotBlank(value)) {
                try {
                    value = URLEncoder.encode(value, CHARSET);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
            sb.append(key).append("=").append(value);
        }
        return sb.toString();
    }

    private static String readResponseString(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, null, null, defaultContentType);
    }

    /**
     * @param url
     * @param contentType
     * @return
     */
    public static String get(String url, String contentType) {
        return get(url, null, null, contentType);
    }

    public static String post(String url, Map<String, String> queryParas) {
        return post(url, queryParas, null, null, defaultContentType);
    }

    /**
     * send post request
     *
     * @param url
     * @param queryParas
     * @param data
     * @param headers
     * @return
     */
    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers,
                              String contentType) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlEncodedWithQueryString(url, queryParas), POST, headers, contentType);
            conn.connect();

            // 传输数据
            if (StringUtils.isNotBlank(data)) {
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes(CHARSET));
                out.flush();
                out.close();
            }

            return readResponseString(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String post(String url, Map<String, String> queryParas, String contentType) {
        return post(url, queryParas, null, null, contentType);
    }

    public static String post(String url, Map<String, String> queryParas, String data, String contentType) {
        return post(url, queryParas, data, null, contentType);
    }

    public static String post(String url, String data, Map<String, String> headers, String contentType) {
        return post(url, null, data, headers, contentType);
    }

    public static String post(String url, String data) {
        return post(url, null, data, null, defaultContentType);
    }

    public static String post(String url, String data, String contentType) {
        return post(url, null, data, null, contentType);
    }

    public static Map<String, Object> postReturnMap(String url, Map<String, String> queryParas, String data,
                                                    String contentType) {
        return postReturnMap(url, queryParas, data, null, contentType);
    }

    public static Map<String, Object> postReturnMap(String url, Map<String, String> queryParas, String data,
                                                    Map<String, String> headers, String contentType) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlEncodedWithQueryString(url, queryParas), POST, headers, contentType);
            conn.connect();

            // 传输数据
            if (StringUtils.isNotBlank(data)) {
                OutputStream out = conn.getOutputStream();
                out.write(data.getBytes(CHARSET));
                out.flush();
                out.close();
            }

            return readResponseMap(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * @param conn
     * @return
     */
    private static Map<String, Object> readResponseMap(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        Map<String, Object> map = new HashMap<>();
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            Document document = XmlKit.parse(sb.toString());
            Element root = document.getDocumentElement();
            NodeList nodeList = root.getChildNodes();

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) {
                    map.put(node.getNodeName(), node.getNodeValue());
                }
            }

            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    public static Map<String, Object> postReturnMap(String url, Map<String, String> queryParas, String data,
                                                    Map<String, String> headers) {
        return postReturnMap(url, queryParas, data, headers, defaultContentType);
    }

    /**
     * 涉及https证书接口的请求
     *
     * @param url      请求的地址
     * @param data     xml数据
     * @param certPath 证书文件目录
     * @param certPass 证书密码
     * @return String 回调的xml信息
     */
    public static String postSSL(String url, String data, String certPath, String certPass) {
        HttpsURLConnection conn = null;
        OutputStream out = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(certPath), certPass.toCharArray());
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, certPass.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();
            SSLContext sslContext = SSLContext.getInstance("TLSv1");

            sslContext.init(kms, null, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            URL _url = new URL(url);
            conn = (HttpsURLConnection) _url.openConnection();

            conn.setConnectTimeout(25000);
            conn.setReadTimeout(25000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("User-Agent", DEFAULT_USER_AGENT);
            conn.connect();

            out = conn.getOutputStream();
            out.write(data.getBytes(CHARSET));
            out.flush();

            inputStream = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(inputStream);
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    /**
     * https 域名校验
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * https 证书管理
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }

}
