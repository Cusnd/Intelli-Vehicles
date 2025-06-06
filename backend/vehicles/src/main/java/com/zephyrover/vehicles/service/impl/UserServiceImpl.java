package com.zephyrover.vehicles.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.zephyrover.vehicles.entity.User;
import com.zephyrover.vehicles.mapper.UserMapper;
import com.zephyrover.vehicles.service.UserService;
import com.zephyrover.vehicles.util.JwtUtil;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean register(User user) {
        // 检查用户名是否已存在
        if (findByUsername(user.getUsername()) != null) {
            return false;
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 保存用户
        return userMapper.insert(user) > 0;
    }

    @Override
    public String login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername(), user.getRole());
        }
        return null;
    }

    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public boolean updateProfile(User user) {
        // 不允许更新密码和用户名
        User existingUser = userMapper.selectById(user.getId());
        if (existingUser == null) {
            return false;
        }

        // 只更新允许修改的字段
        existingUser.setEmail(user.getEmail());
        existingUser.setPhone(user.getPhone());

        return userMapper.updateById(existingUser) > 0;
    }
}
