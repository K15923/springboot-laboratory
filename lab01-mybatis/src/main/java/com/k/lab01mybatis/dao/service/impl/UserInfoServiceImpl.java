package com.k.lab01mybatis.dao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k.lab01mybatis.dao.domain.UserInfo;
import com.k.lab01mybatis.dao.service.UserInfoService;
import com.k.lab01mybatis.dao.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author admin
* @description 针对表【t_user_info】的数据库操作Service实现
* @createDate 2023-05-09 15:59:10
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




