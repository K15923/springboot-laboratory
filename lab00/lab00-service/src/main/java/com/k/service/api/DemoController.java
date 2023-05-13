package com.k.service.api;

import com.k.service.result.Result;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author k 2023/5/8 23:05
 */
@RestController
public class DemoController implements TestApi {
    @Override
    public Result<String> test() {
        return Result.ok();
    }
}
