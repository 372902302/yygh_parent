package com.chen.yygh;

import cn.hutool.json.JSONUtil;

import java.util.ArrayList;

public class TestMain {
    public static void main(String[] args) {
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(6l);
        longs.add(8l);
        System.out.println(JSONUtil.toJsonStr(longs));
    }
}
