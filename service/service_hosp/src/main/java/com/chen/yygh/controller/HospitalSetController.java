package com.chen.yygh.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.model.hosp.HospitalSet;
import com.chen.yygh.service.HospitalSetService;
import com.chen.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("医院设置管理")
@RestController
@RequestMapping("/admin/hosp/hospitalSet")
public class HospitalSetController {
    @Autowired
    private HospitalSetService hospitalSetService;

    @ApiOperation("获取所有医院设置")
    @GetMapping("/findAll")
    public Result findAllHospitalSet(){
        List<HospitalSet> hospitalSets = hospitalSetService.list();
        return Result.ok(hospitalSets);
    }

    @ApiOperation("逻辑删除医院设置")
    @DeleteMapping("/{id}")
    public Result removeById(@PathVariable("id") Long id){
        boolean remove = hospitalSetService.removeById(id);
        if (remove){
            return Result.ok("删除成功");
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("分页查询医院设置")
    @PostMapping("/findPageHospSet/{current}/{pageSize}")
    public Result findPageHospSet(@PathVariable("current") Long current,
                                  @PathVariable("pageSize") Long pageSize,
                                  @RequestBody(required = false) HospitalSetQueryVo searchObject)
    {

        Result result = hospitalSetService.selectPage(current, pageSize, searchObject);
        return result;
    }

    @ApiOperation("添加医院设置")
    @PostMapping("/saveHospital")
    public Result saveHospital(@RequestBody(required = false) HospitalSet hospitalSet){
        return hospitalSetService.saveHospital(hospitalSet);
    }

    @ApiOperation("根据id查询医院设置")
    @GetMapping("/getHospSet/{id}")
    public Result findById(@PathVariable("id") Long id){
        HospitalSet hospitalSetServiceById = hospitalSetService.getById(id);
//        try {
//            int i = 1 / 0;
//        } catch (Exception e) {
//            throw new YyghException("失败",201);
//        }
        if (ObjectUtil.isNotNull(hospitalSetServiceById)){
            return Result.ok(hospitalSetServiceById);
        }else {
            return Result.fail(201,"查询失败，没有此id的医院信息");
        }
    }

    @ApiOperation("修改医院设置")
    @PutMapping("/updateHospitalSet")
    public Result updateHospitalSet(@RequestBody HospitalSet hospitalSet){
        if (ObjectUtil.isNull(hospitalSet)){
            return Result.fail(201,"医院信息为null");
        }
        boolean update = hospitalSetService.updateById(hospitalSet);
        if (update){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("批量删除医院设置")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList){
        if (ObjectUtil.isNull(idList)){
            return Result.fail(201,"id列表为null");
        }
        boolean remove = hospitalSetService.removeByIds(idList);
        if (remove){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public Result lockHospitalSet(@PathVariable Long id,@PathVariable Integer status){
        return hospitalSetService.lock(id, status);
    }

    @ApiOperation("发送签名密钥")
    @PutMapping("/sendKey/{id}")
    public Result sendKey(@PathVariable Long id){
        return hospitalSetService.sendKey(id);
    }




}
