package com.wms.backend.modules.repair.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.repair.dto.RepairProcessSaveRequest;
import com.wms.backend.modules.repair.entity.RepairProcess;
import com.wms.backend.modules.repair.repository.RepairProcessRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RepairProcessService {
    private final RepairProcessRepository repository;
    public RepairProcessService(RepairProcessRepository repository) { this.repository = repository; }
    public List<RepairProcess> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public RepairProcess detail(String id) {
        RepairProcess record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "报修流程不存在");
        return record;
    }
    public RepairProcess create(RepairProcessSaveRequest request) { return repository.insert(request); }
    public RepairProcess update(String id, RepairProcessSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
