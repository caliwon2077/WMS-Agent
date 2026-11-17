package com.wms.backend.modules.mdm.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.mdm.dto.MaterialSaveRequest;
import com.wms.backend.modules.mdm.entity.Material;
import com.wms.backend.modules.mdm.repository.MaterialRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {
    private final MaterialRepository repository;
    public MaterialService(MaterialRepository repository) { this.repository = repository; }

    public List<Material> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public Material detail(String id) {
        Material record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Material not found");
        return record;
    }

    public Material create(MaterialSaveRequest request) { return repository.insert(request); }
    public Material update(String id, MaterialSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
