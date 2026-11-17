package com.wms.backend.modules.mdm.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.mdm.dto.WorkshopSaveRequest;
import com.wms.backend.modules.mdm.entity.Workshop;
import com.wms.backend.modules.mdm.repository.WorkshopRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WorkshopService {
    private final WorkshopRepository repository;
    public WorkshopService(WorkshopRepository repository) { this.repository = repository; }

    public List<Workshop> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public Workshop detail(String id) {
        Workshop record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Workshop not found");
        return record;
    }

    public Workshop create(WorkshopSaveRequest request) { return repository.insert(request); }
    public Workshop update(String id, WorkshopSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
