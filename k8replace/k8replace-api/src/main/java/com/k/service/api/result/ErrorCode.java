package com.k.service.api.result;

import lombok.Data;

/**
 * @author k 2023/4/27 22:30
 */
@Data
public class ErrorCode {
    /**
     * 错误code码
     */
    private Integer code;

    /**
     * 错误提示消息
     */
    private String message;

}
