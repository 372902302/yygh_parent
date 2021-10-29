package com.chen.yygh.controller.api;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.yygh.common.exception.YyghException;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.common.result.ResultCodeEnum;
import com.chen.yygh.common.util.HttpRequestHelper;
import com.chen.yygh.common.util.MD5;
import com.chen.yygh.model.hosp.Department;
import com.chen.yygh.model.hosp.Hospital;
import com.chen.yygh.model.hosp.HospitalSet;
import com.chen.yygh.model.hosp.Schedule;
import com.chen.yygh.service.DepartmentService;
import com.chen.yygh.service.HospitalService;
import com.chen.yygh.service.HospitalSetService;
import com.chen.yygh.service.ScheduleService;
import com.chen.yygh.vo.hosp.DepartmentQueryVo;
import com.chen.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/hosp")
public class ApiController {
    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private HospitalSetService hospitalSetService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/hospital/show")
    public Result showHosp(HttpServletRequest request){
        Map<String, Object> paramMap = vaildMap(request);
        String hoscode = request.getParameter("hoscode");
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    @ApiOperation("上传医院接口")
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request){
        Map<String, Object> paramMap = vaildMap(request);
        String logoData = request.getParameter("logoData");
        paramMap.put("logoData", logoData.replaceAll(" ","+"));
        hospitalService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation("上传科室")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = vaildMap(request);
        departmentService.save(paramMap);
        return Result.ok();
    }


    //抽取出来的方法 用来判断request里面的签名是否正确
    private Map<String, Object> vaildMap(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);
        //传过来的hoscode
        String hoscode = request.getParameter("hoscode");
        //传过来的key
        String signKey = request.getParameter("sign");
        HospitalSet hospitalSet = hospitalSetService.getOne(new QueryWrapper<HospitalSet>().eq("hoscode", hoscode));
        if (ObjectUtil.isNotNull(hospitalSet)){
            String hospitalSetSignKey = MD5.encrypt(hospitalSet.getSignKey());
            if (!hospitalSetSignKey.equals(signKey)){
                throw new YyghException(ResultCodeEnum.SIGN_ERROR);
            }
        }else {
            throw new YyghException("未识别的医院编码",201);
        }
        return paramMap;
    }

    @ApiOperation(value = "获取分页列表")
    @PostMapping("department/list")
    public Result department(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        //非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StrUtil.isEmpty((String)paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StrUtil.isEmpty((String)paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        if(StrUtil.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        vaildMap(request);
        DepartmentQueryVo departmentQueryVo = new DepartmentQueryVo();
        departmentQueryVo.setHoscode(hoscode);
        departmentQueryVo.setDepcode(depcode);
        Page<Department> pageModel = departmentService.selectPage(page, limit, departmentQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        //必填
        String depcode = (String)paramMap.get("depcode");
        if(StrUtil.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        vaildMap(request);
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        if(StrUtil.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        vaildMap(request);
        scheduleService.save(paramMap);
        return Result.ok();
    }


    @ApiOperation(value = "获取排班分页列表")
    @PostMapping("schedule/list")
    public Result schedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
//必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
//非必填
        String depcode = (String)paramMap.get("depcode");
        int page = StrUtil.isEmpty((String)paramMap.get("page")) ? 1 : Integer.parseInt((String)paramMap.get("page"));
        int limit = StrUtil.isEmpty((String)paramMap.get("limit")) ? 10 : Integer.parseInt((String)paramMap.get("limit"));

        if(StrUtil.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        vaildMap(request);
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page , limit, scheduleQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("schedule/remove")
    public Result removeSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //必须参数校验 略
        String hoscode = (String)paramMap.get("hoscode");
        //必填
        String hosScheduleId = (String)paramMap.get("hosScheduleId");
        if(StrUtil.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        //签名校验
        vaildMap(request);
        scheduleService.remove(hoscode, hosScheduleId);
        return Result.ok();
    }



}
