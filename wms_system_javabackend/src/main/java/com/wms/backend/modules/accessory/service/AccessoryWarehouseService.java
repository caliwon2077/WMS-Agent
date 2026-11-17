package com.wms.backend.modules.accessory.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.accessory.dto.AccessoryWarehouseSaveRequest;
import com.wms.backend.modules.accessory.entity.AccessoryWarehouse;
import com.wms.backend.modules.accessory.repository.AccessoryWarehouseRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AccessoryWarehouseService {
    private final AccessoryWarehouseRepository repository;
    public AccessoryWarehouseService(AccessoryWarehouseRepository repository) { this.repository = repository; }
    public List<AccessoryWarehouse> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public AccessoryWarehouse detail(String id) {
        AccessoryWarehouse record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Accessory warehouse not found");
        return record;
    }
    public AccessoryWarehouse create(AccessoryWarehouseSaveRequest request) { return repository.insert(request); }
    public AccessoryWarehouse update(String id, AccessoryWarehouseSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
