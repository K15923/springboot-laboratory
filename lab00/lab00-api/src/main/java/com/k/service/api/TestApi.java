package com.k.service.api;


import com.k.service.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "lab00")
@Api(value = "demo")
public interface TestApi {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "测试", notes = "demo测试")
    Result<String> test();

}
