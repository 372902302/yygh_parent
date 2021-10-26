package com.chen.yygh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.chen.yygh.model.hosp.BookingRule;
import com.chen.yygh.model.hosp.Hospital;
import com.chen.yygh.repository.HospitalRepository;
import com.chen.yygh.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        //没有就添加，有的话就更新
        String jsonStr = JSONUtil.toJsonStr(paramMap);
        Hospital hospital = JSONUtil.toBean(jsonStr, Hospital.class);
        Hospital exists = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        String bookingRuleJson = (String) paramMap.get("bookingRule");
        BookingRule bookingRule = JSONUtil.toBean(bookingRuleJson, BookingRule.class);
        hospital.setBookingRule(bookingRule);
        if (ObjectUtil.isNotNull(exists)){
            hospital.setStatus(exists.getStatus());
            hospital.setId(exists.getId());
            hospital.setCreateTime(exists.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }else {
            hospital.setStatus(1);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        }

    }

    @Override
    public Hospital getByHoscode(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital;
    }
}
