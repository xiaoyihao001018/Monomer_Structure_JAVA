package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private RedisUtils redisUtils;
    
    private static final String USER_KEY_PREFIX = "user:";
    
    public User getUser(Long id) {
        String key = USER_KEY_PREFIX + id;
        
        // 先从Redis获取
        Object cached = redisUtils.get(key);
        if (cached != null) {
            log.debug("从Redis获取用户: {}, 数据是: {}", id, cached);
            return (User) cached;
        }
        
        // Redis没有，从数据库获取
        User user = userMapper.selectById(id);
        if (user != null) {
            // 放入Redis，设置1小时过期
            redisUtils.set(key, user, 3600);
            log.debug("用户数据放入Redis: {}, 数据是: {}", id, user);
        }
        
        return user;
    }
    
    public List<User> listUsers() {
        return userMapper.selectList(null);
    }
    
    public boolean saveUser(User user) {
        return userMapper.insert(user) > 0;
    }
} 