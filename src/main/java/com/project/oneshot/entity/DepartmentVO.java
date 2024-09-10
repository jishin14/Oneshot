package com.project.oneshot.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepartmentVO {

    @NotBlank
    private int departmentNo;

    @NotBlank
    private String departmentName;
}