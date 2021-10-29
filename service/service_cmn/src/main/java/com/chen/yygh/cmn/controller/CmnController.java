package com.chen.yygh.cmn.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chen.yygh.cmn.service.DictService;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.model.cmn.Dict;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(
            @ApiParam(name = "parentDictCode", value = "上级编码", required = true)
            @PathVariable("parentDictCode") String parentDictCode,

            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation(value = "获取数据字典名称")
    @ApiImplicitParam(name = "value", value = "值", required = true, dataType = "Long", paramType = "path")
    @GetMapping(value = "/getName/{value}")
    public String getName(
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue("", value);
    }

    @ApiOperation(value = "根据dictcode查出所有子节点")
    @GetMapping("findChildrenByDictcode/{dictCode}")
    public Result findChildrenByDictcode(@PathVariable String dictCode){
        List<Dict> list = dictService.findChildrenByDictcode(dictCode);
        if (ObjectUtil.isNotNull(list)){
            return Result.ok(list);
        }else {
            return Result.fail(201,"查询dict列表失败");
        }
    }
}
