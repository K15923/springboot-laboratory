package com.k.service.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k.service.api.dto.QueryMerchandiseDto;
import com.k.service.utils.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 接收站点 油站配置信息变更消息
 *
 * @author: wangqing
 * @date: 2022/10/24
 **/
@Slf4j
@Component
public class StationCacheUpdateListener implements MessageListener {


    @Autowired
    private ObjectMapper om;

    public void send(QueryMerchandiseDto queryMerchandiseDto) {
        try {
            Message message = new Message("scs_station_office", "stationState",
                                          om.writeValueAsBytes(queryMerchandiseDto));
            consume(message, null);
        } catch (Exception e) {
            log.error("send cancelOrderMsg message failed. : {}", e);
        }
    }

    @SneakyThrows
    @Override
    public Action consume(Message message, ConsumeContext context) {

        log.info("接收ssc油站变更消息 : scsStationMessageDTO : {} ", "");
        try {
            return Action.CommitMessage;
        } catch (Exception e) {
            log.info("ssc油站变更消息消费失败,消息体{}, 重试 {},异常信息：{}", JsonUtils.toJson(""),
                     message.getReconsumeTimes(), e);
            return Action.ReconsumeLater;
        }
    }
}
