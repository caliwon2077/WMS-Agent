package com.wms.backend.modules.mdm.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.mdm.dto.WorkAreaSaveRequest;
import com.wms.backend.modules.mdm.entity.WorkArea;
import com.wms.backend.modules.mdm.repository.WorkAreaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class WorkAreaService {
    private final WorkAreaRepository repository;
    public WorkAreaService(WorkAreaRepository repository) { this.repository = repository; }

    public List<WorkArea> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public WorkArea detail(String id) {
        WorkArea record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "Work area not found");
        return record;
    }

    public WorkArea create(WorkAreaSaveRequest request) { return repository.insert(request); }
    public WorkArea update(String id, WorkAreaSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
