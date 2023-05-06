package com.k.service.interceptor;

import com.alibaba.fastjson.JSON;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;

import java.io.*;
import java.util.*;

import static feign.Util.decodeOrDefault;
import static java.nio.charset.StandardCharsets.UTF_8;


/**
 * @Author: wangqing
 * @DATE: 2022/12/6
 */
@Slf4j
public class FeignLogger extends feign.Logger {

    private static final ThreadLocal<Map<String, String>> logContext = new ThreadLocal<>();

    private static final String METHOD_NAME = "methodName";

    private static final String PATH = "path";

    private static final String METHOD = "method";

    private static final String REQUEST_BODY = "body";

    private static final String HEADER = "header";

    private static final String TIME = "time";

    /**
     * 请求拦截
     */
    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        Map<String, String> map = new HashMap<>(3);
        map.put(METHOD_NAME, configKey);
        map.put(PATH, request.url());
        map.put(TIME, System.currentTimeMillis() + "");
        map.put(METHOD, request.httpMethod().name());
        map.put(HEADER, builderHeaders(request.headers()));
        String body = request.charset() != null ? new String(request.body(), request.charset()) : "Binary data";
        // 文件上传不打印请求日志
        if (StringUtils.contains(request.url(), "/file/upload")) {
            body = null;
        }
        map.put(REQUEST_BODY, body);
        logContext.set(map);
    }

    /**
     * 构建headers字符串
     */
    private String builderHeaders(Map<String, Collection<String>> headersMap) {
        StringBuilder headers = new StringBuilder();
        Iterator<Map.Entry<String, Collection<String>>> iterator = headersMap.entrySet().stream().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Collection<String>> next = iterator.next();
            ArrayList<String> values = new ArrayList<>(next.getValue());
            headers.append(next.getKey()).append(":").append(
                    values.size() == 1 ? values.get(0) : JSON.toJSONString(values)).append(
                    iterator.hasNext() ? "|" : "");
        }
        return headers.toString();
    }

    /**
     * 响应拦截
     */
    @Override
    protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response,
                                              long elapsedTime) throws IOException {
        Map<String, String> request = logContext.get();
        logContext.remove();
        // 返回参数
        if (response.body() != null) {
            byte[] bodyData = streamToByteArray(response.body().asInputStream());
            if (null != bodyData && bodyData.length > 0) {
                String responseBody = decodeOrDefault(bodyData, UTF_8, "Binary data");
                log(request, response.status(), responseBody.replaceAll("\\s*|\t|\r|\n", ""));
                return response.toBuilder().body(bodyData).build();
            }
        }

        log(request, response.status(), null);
        return response;
    }

    /**
     * 输入流转byte[]
     *
     * @param inStream 文件流内容
     * @return
     */
    public static byte[] streamToByteArray(InputStream inStream) {
        if (inStream == null) {
            return null;
        }
        byte[] in2b = null;
        BufferedInputStream in = new BufferedInputStream(inStream);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        int rc = 0;
        try {
            while ((rc = in.read()) != -1) {
                swapStream.write(rc);
            }
            in2b = swapStream.toByteArray();
        } catch (IOException e) {
            log.warn("streamToByteArray exception inStream:[{}]", inStream, e);
        } finally {
            closeIo(inStream, in, swapStream);
        }
        return in2b;
    }

    /**
     * 日志打印
     *
     * @param request
     * @param responseStatus
     * @param responseBody
     */
    private void log(Map<String, String> request, Integer responseStatus, String responseBody) {
        log.info("\n<FeignRequest>" + "\npath       -> {}" + "\nmethodName -> {}" + "\ntime       -> {}" +
                 "\nmethod     -> {}" + "\nstatus     -> {}" + "\nrequest    -> {}" + "\nresponse   -> {}",
                 request.get(PATH), request.get(METHOD_NAME),
                 (System.currentTimeMillis() - Long.parseLong(request.get(TIME))) + "ms", request.get(METHOD),
                 responseStatus, request.get(REQUEST_BODY), responseBody);
    }

    /**
     * 关闭流
     */
    public static void closeIo(Closeable... closeable) {
        if (null == closeable || closeable.length <= 0) {
            return;
        }
        for (Closeable cb : closeable) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                throw new RuntimeException(FileUtil.class.getName(), e);
            }
        }
    }

    /**
     * 异常拦截
     */
    @Override
    protected IOException logIOException(String configKey, Level logLevel, IOException ioe, long elapsedTime) {
        Map<String, String> request = logContext.get();
        logContext.remove();
        log.error("\n<FeignRequest-Error> -> \npath -> {}\nmethod -> {}\nheaders -> {}\nrequest -> {}\nexception -> ",
                  request.get(PATH), request.get(METHOD), request.get(HEADER), request.get(REQUEST_BODY), ioe);
        return ioe;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        if (log.isInfoEnabled()) {
            log.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
