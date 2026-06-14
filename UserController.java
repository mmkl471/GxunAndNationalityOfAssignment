package com.example.demo.controller;

import com.example.demo.common.Result;
import com.example.demo.config.JwtUtil;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    // 用户登录
    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody User loginUser) {
        User user = userService.getByUsername(loginUser.getUsername());
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!user.getPassword().equals(loginUser.getPassword())) {
            return Result.error("密码错误");
        }
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        return Result.success(data);
    }

    // 获取用户信息
    @GetMapping("/info")
    public Result<User> info(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        Long userId = jwtUtil.getUserIdFromToken(token);
        User user = userService.getById(userId);
        if (user != null) {
            user.setPassword(null);
        }
        return Result.success(user);
    }
}
