package com.project.oneshot.controller;


import com.project.oneshot.entity.DepartmentVO;
import com.project.oneshot.humanResource.HumanResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.FieldError;


import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hrm")
public class humanResourceRestController {

    @Autowired
    @Qualifier("humanResourceService")
    private HumanResourceService humanResourceService;

    @PostMapping("/registDepartment")
    public ResponseEntity<Map<String, String>> registDepartment(
            @Valid @RequestBody DepartmentVO vo, BindingResult result) {

        if (result.hasErrors()) {
            // 검증 오류가 있을 경우, 필드별 오류 메시지를 JSON 형태로 반환
            Map<String, String> errors = new HashMap<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        int insertResult = humanResourceService.departmentInsert(vo);
        return ResponseEntity.ok(Collections.singletonMap("result", "Insert result: " + insertResult));
    }

}
