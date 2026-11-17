package com.wms.backend.modules.equipment.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.equipment.dto.ManufacturerSaveRequest;
import com.wms.backend.modules.equipment.entity.Manufacturer;
import com.wms.backend.modules.equipment.repository.ManufacturerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManufacturerService {
    private final ManufacturerRepository repository;
    public ManufacturerService(ManufacturerRepository repository) { this.repository = repository; }
    public List<Manufacturer> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public Manufacturer detail(String id) {
        Manufacturer record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "厂商不存在");
        return record;
    }
    public Manufacturer create(ManufacturerSaveRequest request) { return repository.insert(request); }
    public Manufacturer update(String id, ManufacturerSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
