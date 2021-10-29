package com.chen.yygh.controller;

import com.chen.yygh.common.result.Result;
import com.chen.yygh.service.HospitalService;
import com.chen.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/hosp/hospital")
public class HospitalController {
    @Autowired
    private HospitalService hospitalService;

    @ApiOperation(value = "获取分页列表")
    @PostMapping("{page}/{limit}")
    public Result index(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Integer page,

            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Integer limit,

            @ApiParam(name = "hospitalQueryVo", value = "查询对象", required = false)
                    @RequestBody HospitalQueryVo hospitalQueryVo) {
        return Result.ok(hospitalService.selectPage(page, limit, hospitalQueryVo));
    }

    @ApiOperation("更新医院上下线状态")
    @PutMapping("/{id}/{status}")
    public Result updateStatus(@PathVariable("id") String id , @PathVariable("status") Integer status){
        hospitalService.updateStatus(id,status);
        return Result.ok();
    }

    @ApiOperation(value = "获取医院详情")
    @GetMapping("show/{id}")
    public Result show(
            @ApiParam(name = "id", value = "医院id", required = true)
            @PathVariable String id) {
        return Result.ok(hospitalService.show(id));
    }


}
