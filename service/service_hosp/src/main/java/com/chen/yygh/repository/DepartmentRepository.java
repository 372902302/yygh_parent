package com.chen.yygh.repository;

import com.chen.yygh.model.hosp.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends MongoRepository<Department,String> {

    Department findDepartmentByHoscodeAndDepcode(String hoscode,String depcode);


    Department getDepartmentByHoscodeAndDepcode(String hoscode, String depcode);
}
