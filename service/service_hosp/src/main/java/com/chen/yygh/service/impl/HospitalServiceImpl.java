package com.chen.yygh.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.chen.yygh.cmn.client.DictFeignClient;
import com.chen.yygh.model.hosp.BookingRule;
import com.chen.yygh.model.hosp.Hospital;
import com.chen.yygh.repository.HospitalRepository;
import com.chen.yygh.service.HospitalService;
import com.chen.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private DictFeignClient dictFeignClient;

    @Override
    public void save(Map<String, Object> paramMap) {
        //没有就添加，有的话就更新
        String jsonStr = JSONUtil.toJsonStr(paramMap);
        Hospital hospital = JSONUtil.toBean(jsonStr, Hospital.class);
        Hospital exists = hospitalRepository.getHospitalByHoscode(hospital.getHoscode());
        String bookingRuleJson = (String) paramMap.get("bookingRule");
        BookingRule bookingRule = JSONUtil.toBean(bookingRuleJson, BookingRule.class);
        hospital.setBookingRule(bookingRule);
        if (ObjectUtil.isNotNull(exists)) {
            hospital.setStatus(exists.getStatus());
            hospital.setId(exists.getId());
            hospital.setCreateTime(exists.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(0);
            hospitalRepository.save(hospital);
        } else {
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

    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Hospital hospital = new Hospital();
        BeanUtils.copyProperties(hospitalQueryVo, hospital);

        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式：模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写

        //创建实例
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> pages = hospitalRepository.findAll(example, pageable);
        pages.getContent().forEach(item -> {
            setHospitalParam(item);
        });
        return pages;

    }

    private void setHospitalParam(Hospital item){
        String hostypeString = dictFeignClient.getName("Hostype", item.getHostype());
        String provinceString = dictFeignClient.getName(item.getProvinceCode());
        String cityString = dictFeignClient.getName(item.getCityCode());
        String districtString = dictFeignClient.getName(item.getDistrictCode());

        item.getParam().put("hostypeString", hostypeString);
        item.getParam().put("fullAddress", provinceString + cityString + districtString + item.getAddress());
    }

    @Override
    public void updateStatus(String id, Integer status) {
        Optional<Hospital> result = hospitalRepository.findById(id);
        Hospital hospital = result.get();
        hospital.setUpdateTime(new Date());
        hospital.setStatus(status);
        hospitalRepository.save(hospital);
    }

    @Override
    public Map<String, Object> show(String id) {
        Map<String, Object> result = new HashMap<>();
        Hospital hospital = hospitalRepository.findById(id).get();
        setHospitalParam(hospital);

        result.put("hospital", hospital);
        //单独处理更直观
        result.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return result;
    }

    @Override
    public String getHospName(String hoscode) {
        Hospital hospital = hospitalRepository.getHospitalByHoscode(hoscode);
        return hospital.getHosname();
    }

}
