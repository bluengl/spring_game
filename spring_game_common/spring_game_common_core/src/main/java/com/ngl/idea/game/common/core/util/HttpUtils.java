package com.ngl.idea.game.common.core.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * HTTP工具类
 */
@Slf4j
public class HttpUtils {

    /**
     * 执行GET请求
     *
     * @param url    请求URL
     * @param params 请求参数
     * @return 响应结果
     */
    public static String doGet(String url, Map<String, String> params) {
        StringBuilder urlBuilder = new StringBuilder(url);
        
        if (params != null && !params.isEmpty()) {
            urlBuilder.append("?");
            params.forEach((key, value) -> {
                try {
                    urlBuilder.append(key).append("=")
                            .append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()))
                            .append("&");
                } catch (Exception e) {
                    log.error("URL参数编码失败", e);
                }
            });
            // 移除最后一个&
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }
        
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String result = null;
        
        try {
            URL requestUrl = new URL(urlBuilder.toString());
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            connection.connect();
            
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                result = response.toString();
            } else {
                log.error("HTTP请求失败: {}, 响应码: {}", url, connection.getResponseCode());
            }
        } catch (Exception e) {
            log.error("执行HTTP GET请求失败: {}", url, e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    log.error("关闭BufferedReader失败", e);
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        
        return result;
    }
    
    /**
     * 执行GET请求并将结果解析为指定类型
     *
     * @param url         请求URL
     * @param params      请求参数
     * @param resultClass 结果类型
     * @param <T>         泛型
     * @return 解析后的结果对象
     */
    public static <T> T doGet(String url, Map<String, String> params, Class<T> resultClass) {
        String result = doGet(url, params);
        if (StringUtils.hasText(result)) {
            try {
                return JSON.parseObject(result, resultClass);
            } catch (Exception e) {
                log.error("解析HTTP响应失败: {}", result, e);
            }
        }
        return null;
    }
} 