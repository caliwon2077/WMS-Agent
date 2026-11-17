package com.wms.backend.modules.equipment.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.equipment.dto.EquipmentCategorySaveRequest;
import com.wms.backend.modules.equipment.entity.EquipmentCategory;
import com.wms.backend.modules.equipment.repository.EquipmentCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentCategoryService {
    private final EquipmentCategoryRepository repository;
    public EquipmentCategoryService(EquipmentCategoryRepository repository) { this.repository = repository; }
    public List<EquipmentCategory> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public EquipmentCategory detail(String id) {
        EquipmentCategory record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "设备分类不存在");
        return record;
    }
    public EquipmentCategory create(EquipmentCategorySaveRequest request) { return repository.insert(request); }
    public EquipmentCategory update(String id, EquipmentCategorySaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
