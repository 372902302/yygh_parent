package com.chen.yygh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.model.hosp.HospitalSet;
import com.chen.yygh.vo.hosp.HospitalSetQueryVo;

/**
 *
 */
public interface HospitalSetService extends IService<HospitalSet> {

    Result selectPage(Long current, Long pageSize, HospitalSetQueryVo query);

    Result saveHospital(HospitalSet hospitalSet);

    Result lock(Long id, Integer status);

    Result sendKey(Long id);
}
