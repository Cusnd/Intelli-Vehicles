package com.zephyrover.vehicles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyrover.vehicles.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户
     */
    User findByUsername(String username);
}
