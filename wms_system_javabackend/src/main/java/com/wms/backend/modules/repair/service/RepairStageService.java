package com.wms.backend.modules.repair.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.repair.dto.RepairStageSaveRequest;
import com.wms.backend.modules.repair.entity.RepairStage;
import com.wms.backend.modules.repair.repository.RepairStageRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RepairStageService {
    private final RepairStageRepository repository;
    public RepairStageService(RepairStageRepository repository) { this.repository = repository; }
    public List<RepairStage> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public RepairStage detail(String id) {
        RepairStage record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "报修阶段不存在");
        return record;
    }
    public RepairStage create(RepairStageSaveRequest request) { return repository.insert(request); }
    public RepairStage update(String id, RepairStageSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
