package com.chen.yygh.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/admin/user")
public class UserController {

    @PostMapping("/login")
    public Object loginUser(String username,String password){
        HashMap<String, String> map = new HashMap<>();
        map.put("token","admin");
        return map;
    }

}
