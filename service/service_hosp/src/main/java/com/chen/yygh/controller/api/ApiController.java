package com.chen.yygh.controller.api;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chen.yygh.common.exception.YyghException;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.common.result.ResultCodeEnum;
import com.chen.yygh.common.util.HttpRequestHelper;
import com.chen.yygh.common.util.MD5;
import com.chen.yygh.model.hosp.Hospital;
import com.chen.yygh.model.hosp.HospitalSet;
import com.chen.yygh.service.HospitalService;
import com.chen.yygh.service.HospitalSetService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
}
