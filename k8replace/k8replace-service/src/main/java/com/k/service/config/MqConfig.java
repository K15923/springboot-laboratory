package com.k.service.config;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties(prefix = "rocketmq")
@RefreshScope
public class MqConfig {

    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;
    private String consumeThreadNums;
    /**
     * @Description: 默认重试5次就记录入库，后续补偿
     * @Author: wuzhouwei
     * @Date: 2022/8/19
     * @param null:
     * @return:
     **/
    private Integer maxRetryNum = 5;

    private String autoCancelOrderTopic;

    private Integer autoCancelOrderSeconds;

    public Integer getAutoCancelOrderSeconds() {
        return autoCancelOrderSeconds;
    }

    public void setAutoCancelOrderSeconds(Integer autoCancelOrderSeconds) {
        this.autoCancelOrderSeconds = autoCancelOrderSeconds;
    }

    public String getAutoCancelOrderTopic() {
        return autoCancelOrderTopic;
    }

    public void setAutoCancelOrderTopic(String autoCancelOrderTopic) {
        this.autoCancelOrderTopic = autoCancelOrderTopic;
    }

    public Integer getMaxRetryNum() {
        return maxRetryNum;
    }

    public void setMaxRetryNum(Integer maxRetryNum) {
        this.maxRetryNum = maxRetryNum;
    }

    public Properties getMqProperties() {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, this.accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, this.secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, this.nameSrvAddr);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, this.consumeThreadNums);
        return properties;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getNameSrvAddr() {
        return nameSrvAddr;
    }

    public void setNameSrvAddr(String nameSrvAddr) {
        this.nameSrvAddr = nameSrvAddr;
    }

    public String getConsumeThreadNums() {
        return consumeThreadNums;
    }

    public void setConsumeThreadNums(String consumeThreadNums) {
        this.consumeThreadNums = consumeThreadNums;
    }
}
