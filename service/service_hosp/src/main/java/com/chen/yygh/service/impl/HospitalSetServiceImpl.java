package com.chen.yygh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chen.yygh.common.result.Result;
import com.chen.yygh.mapper.HospitalSetMapper;
import com.chen.yygh.model.hosp.HospitalSet;
import com.chen.yygh.service.HospitalSetService;
import com.chen.yygh.vo.hosp.HospitalSetQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 *
 */
@Service
public class HospitalSetServiceImpl extends ServiceImpl<HospitalSetMapper, HospitalSet>
    implements HospitalSetService{

    @Autowired
    private HospitalSetMapper hospitalSetMapper;
    @Override
    public Result selectPage(Long current, Long pageSize, HospitalSetQueryVo query) {
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNull(query)){
            query = new HospitalSetQueryVo();
            query.setHoscode("");
            query.setHosname("");
        }
        Page<HospitalSet> hospitalSetPage = new Page<>(current, pageSize);
        if (StrUtil.isNotEmpty(query.getHosname())) {
            queryWrapper.like("hosname",query.getHosname());
        }
        if (StrUtil.isNotEmpty(query.getHoscode())) {
            queryWrapper.eq("hoscode",query.getHoscode());
        }
        Page<HospitalSet> page = hospitalSetMapper.selectPage(hospitalSetPage, queryWrapper);
        return Result.ok(page);
    }

    @Override
    public Result saveHospital(HospitalSet hospitalSet) {
        if (ObjectUtil.isNull(hospitalSet)){
            return Result.fail(201,"医院信息为null");
        }
        hospitalSet.setStatus(1);
        String md5 = SecureUtil.md5(System.currentTimeMillis() + "" + new Random().nextInt(1000));
        hospitalSet.setSignKey(md5);
        boolean save = this.saveOrUpdate(hospitalSet);
        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result lock(Long id, Integer status) {
        HospitalSet hospitalSet = hospitalSetMapper.selectById(id);
        hospitalSet.setStatus(status);
        boolean save = this.saveOrUpdate(hospitalSet);
        if (save){
            return Result.ok();
        }else {
            return Result.fail();
        }
    }

    @Override
    public Result sendKey(Long id) {
        HospitalSet hospitalSet = hospitalSetMapper.selectById(id);
        String signKey = hospitalSet.getSignKey();
        String hoscode = hospitalSet.getHoscode();
        //TODO: 发送短信
        return Result.ok();
    }
}




