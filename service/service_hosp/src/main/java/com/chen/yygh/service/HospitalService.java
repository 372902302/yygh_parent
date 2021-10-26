package com.chen.yygh.service;

import com.chen.yygh.model.hosp.Hospital;

import java.util.Map;

public interface HospitalService{
    void save(Map<String, Object> paramMap);

    Hospital getByHoscode(String hoscode);
}
