package com.project.oneshot.inventory.supplier;

import com.project.oneshot.entity.jpa.SupplierVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SupplierMapper {
    public void registerSupplier(SupplierVO supplier);
    public List<SupplierVO> getAllSuppliers();
}