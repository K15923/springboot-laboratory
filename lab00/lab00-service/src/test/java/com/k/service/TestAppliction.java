package com.k.service;

import com.k.service.enums.ErrorEnum;
import com.k.service.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author k 2023/5/16 15:14
 */
@Slf4j
@SpringBootTest
public class TestAppliction {

    @Test
    public void test01() {
        if (true) {
            throw new ApplicationException(ErrorEnum.NO_AUTH);
        }
        System.out.println("异常抛出");

    }
}
