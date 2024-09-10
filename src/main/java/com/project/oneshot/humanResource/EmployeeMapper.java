package com.project.oneshot.humanResource;

import com.project.oneshot.entity.EmployeeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    // 모든 사원 조회
    List<EmployeeVO> getAllEmployees();

    // 사원 생성 및 업데이트
    void insertOrUpdateEmployee(EmployeeVO employeeVo);

    // 사원 삭제
    void deleteEmployee(Long id);

}
