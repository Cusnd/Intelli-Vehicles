package com.zephyrover.vehicles.service;

import com.zephyrover.vehicles.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    boolean register(User user);

    /**
     * 用户登录
     */
    String login(String username, String password);

    /**
     * 根据用户名查找用户
     */
    User findByUsername(String username);

    /**
     * * 根据ID查找用户
     */
    User findById(Long id);

    /**
     * * 更新用户信息
     */
    boolean updateProfile(User user);
}
