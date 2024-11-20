package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "用户管理", description = "用户管理相关接口")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @GetMapping
    public List<User> listUsers() {
        log.debug("开始获取用户列表");
        List<User> users = userService.listUsers();
        log.debug("获取到 {} 个用户", users.size());
        return users;
    }

    @Operation(summary = "根据ID获取用户")
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        log.debug("获取用户信息, id: {}", id);
        return userService.getUser(id);
    }

    @Operation(summary = "创建用户")
    @PostMapping
    public boolean createUser(@RequestBody User user) {
        log.debug("创建用户: {}", user);
        return userService.saveUser(user);
    }

} 