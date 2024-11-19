package org.example.service;

import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    public User getUser(Long id) {
        return userMapper.selectById(id);
    }
    
    public List<User> listUsers() {
        return userMapper.selectList(null);
    }
    
    public boolean saveUser(User user) {
        return userMapper.insert(user) > 0;
    }
} 