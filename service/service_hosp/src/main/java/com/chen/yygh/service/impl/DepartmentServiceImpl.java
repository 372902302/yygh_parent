package com.chen.yygh.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.chen.yygh.model.hosp.Department;
import com.chen.yygh.repository.DepartmentRepository;
import com.chen.yygh.service.DepartmentService;
import com.chen.yygh.vo.hosp.DepartmentQueryVo;
import com.chen.yygh.vo.hosp.DepartmentVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void save(Map<String, Object> paramMap) {
        String jsonStr = JSONUtil.toJsonStr(paramMap);
        Department department = JSONUtil.toBean(jsonStr, Department.class);
        Department exist = departmentRepository.findDepartmentByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        if (ObjectUtil.isNotNull(exist)) {
            BeanUtil.copyProperties(department, exist, "id", "createTime");
            //更新就保存查出来的就行 用参数覆盖掉
            exist.setUpdateTime(new Date());
            departmentRepository.save(exist);
        } else {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(0);
            departmentRepository.save(department);
        }
    }

    @Override
    public Page<Department> selectPage(Integer page, Integer limit, DepartmentQueryVo departmentQueryVo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //0为第一页
        Pageable pageable = PageRequest.of(page - 1, limit, sort);

        Department department = new Department();
        BeanUtils.copyProperties(departmentQueryVo, department);
        department.setIsDeleted(0);

        //创建匹配器，即如何使用查询条件 //构建对象
        ExampleMatcher matcher = ExampleMatcher.matching()
                //改变默认字符串匹配方式：模糊查询
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                //改变默认大小写忽略方式：忽略大小写
                .withIgnoreCase(true);

        //创建实例
        Example<Department> example = Example.of(department, matcher);
        Page<Department> pages = departmentRepository.findAll(example, pageable);
        return pages;
    }

    @Override
    public void remove(String hoscode, String depcode) {
        Department department = departmentRepository.getDepartmentByHoscodeAndDepcode(hoscode, depcode);
        if (null != department) {
            departmentRepository.deleteById(department.getId());
        }

    }

    @Override
    public List<DepartmentVo> findDeptTree(String hoscode) {
        List<DepartmentVo> result = new ArrayList<>();
        //根据医院编号，查询医院所有科室信息
        Department departmentQuery = new Department();
        departmentQuery.setHoscode(hoscode);
        Example example = Example.of(departmentQuery);
        List<Department> all = departmentRepository.findAll(example);
        Map<String, List<Department>> collect = all.stream().collect(Collectors.groupingBy(Department::getBigcode));
        for (Map.Entry<String, List<Department>> deptList : collect.entrySet()) {
            String bigcode = deptList.getKey();
            List<Department> value = deptList.getValue();
            String bigname = value.get(0).getBigname();
            DepartmentVo bigDepartment = new DepartmentVo();
            bigDepartment.setDepcode(bigcode);
            bigDepartment.setDepname(bigname);
            List<DepartmentVo> children = new ArrayList<>();
            for (Department department : value) {
                DepartmentVo child = new DepartmentVo();
                child.setDepname(department.getDepname());
                child.setDepcode(department.getDepcode());
                children.add(child);
            }
            bigDepartment.setChildren(children);
            result.add(bigDepartment);
        }


        return result;
    }
}
