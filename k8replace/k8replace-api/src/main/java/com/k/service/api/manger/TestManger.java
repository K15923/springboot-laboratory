package com.k.service.api.manger;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "k8replace")
@Api(value = "油价信息查询与导出")
public interface TestManger {

    @RequestMapping(value = "/oil/price/test", method = RequestMethod.GET)
    @ApiOperation(value = "测试", notes = "feign测试")
    String test();

}
