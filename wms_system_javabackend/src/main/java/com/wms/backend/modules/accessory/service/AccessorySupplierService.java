package com.wms.backend.modules.accessory.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.accessory.dto.AccessorySupplierSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessorySupplier;
import com.wms.backend.modules.accessory.repository.AccessorySupplierRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessorySupplierService {
    private final AccessorySupplierRepository repository;
    public AccessorySupplierService(AccessorySupplierRepository repository) { this.repository = repository; }
    public List<AccessorySupplier> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public AccessorySupplier detail(String id) {
        AccessorySupplier record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Accessory supplier not found");
        return record;
    }
    public AccessorySupplier create(AccessorySupplierSaveRequest request) { return repository.insert(request); }
    public AccessorySupplier update(String id, AccessorySupplierSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
