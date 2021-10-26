package com.chen.yygh.repository;

import com.chen.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {
    //判断一下
    Hospital getHospitalByHoscode(String hoscode);
}
