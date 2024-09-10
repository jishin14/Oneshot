package com.project.oneshot.humanResource;

import com.project.oneshot.entity.EmployeeVO;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService{
    @Override
    public List<EmployeeVO> getAllEmployees() {
        return List.of();
    }

    @Override
    public EmployeeVO createOrUpdateEmployee(EmployeeVO employeeVo) {
        return null;
    }

    @Override
    public void deleteEmployee(Long id) {

    }
}
