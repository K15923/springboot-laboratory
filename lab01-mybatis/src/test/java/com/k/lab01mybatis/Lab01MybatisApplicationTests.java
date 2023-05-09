package com.k.lab01mybatis;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.k.lab01mybatis.dao.domain.UserInfo;
import com.k.lab01mybatis.dao.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@SpringBootTest
class Lab01MybatisApplicationTests {

    @Resource
    private UserInfoService userInfoService;

    @Test
    public void save() {
        UserInfo userInfo = UserInfo.builder().username("zs").password("123456").phoneNumber("15923121234").build();
        boolean save = userInfoService.save(userInfo);
        log.info("保存结果：{}", save);
    }
    @Test
    public void queryOne() {
        LambdaQueryWrapper<UserInfo> queryWrapper = Wrappers.lambdaQuery(UserInfo.class);
        List<UserInfo> list = userInfoService.list(queryWrapper.eq(UserInfo::getUsername, "张三"));
    }

    @Test
    public void queryList() {
        List<UserInfo> list = userInfoService.list();
        log.info("保存结果：{}", list);
    }


    @Test
    public void queryPage() {
        //分页参数
        Page<UserInfo> page = Page.of(9, 10);
        //queryWrapper组装查询where条件
        LambdaQueryWrapper<UserInfo> queryWrapper = new LambdaQueryWrapper<>();
        // queryWrapper.eq(UserInfo::get,13);
        userInfoService.getBaseMapper().selectPage(page, queryWrapper);
        page.getRecords().forEach(System.out::println);
    }




}
