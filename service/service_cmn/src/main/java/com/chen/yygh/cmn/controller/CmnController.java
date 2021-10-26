package com.chen.yygh.cmn.controller;

import com.chen.yygh.cmn.service.DictService;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/cmn/dict")
public class CmnController {
    @Autowired
    private DictService dictService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据数据id获取子数据列表")
    @GetMapping("findChildData/{id}")
    public Result findChildData(@PathVariable Long id){
        List<Dict> list = dictService.findChildData(id);
        return Result.ok(list);
    }

    @ApiOperation("导出数据字典")
    @GetMapping("exportData")
    public void exportData(HttpServletResponse response){
        dictService.exportData(response);
    }

    @ApiOperation("导入数据字典")
    @PostMapping("/importData")
    public void importData(MultipartFile file){
        dictService.importData(file);
    }

    @GetMapping("testRedis")
    public void testRedis(){
        redisTemplate.opsForValue().set("test","chen");
    }

    @GetMapping("testRedis2")
    public Object testRedis2(){
        Object dict = redisTemplate.opsForValue().get("dict::com.chen.yygh.cmn.service.impl.DictServiceImplfindChildData1");
        return dict;
    }
}
