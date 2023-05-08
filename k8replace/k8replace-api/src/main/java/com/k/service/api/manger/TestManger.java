package com.k.service.api.manger;


import com.k.service.api.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "k8replace")
@Api(value = "demo")
public interface TestManger {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "测试", notes = "feign测试")
    Result<String> test();

}
