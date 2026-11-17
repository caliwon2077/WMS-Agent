package com.wms.backend.modules.accessory.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.accessory.dto.AccessoryCategorySaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryCategory;
import com.wms.backend.modules.accessory.repository.AccessoryCategoryRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessoryCategoryService {
    private final AccessoryCategoryRepository repository;
    public AccessoryCategoryService(AccessoryCategoryRepository repository) { this.repository = repository; }
    public List<AccessoryCategory> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public AccessoryCategory detail(String id) {
        AccessoryCategory record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Accessory category not found");
        return record;
    }
    public AccessoryCategory create(AccessoryCategorySaveRequest request) { return repository.insert(request); }
    public AccessoryCategory update(String id, AccessoryCategorySaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
