package com.wms.backend.modules.repair.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.repair.dto.RepairApplySaveRequest;
import com.wms.backend.modules.repair.entity.RepairApply;
import com.wms.backend.modules.repair.repository.RepairApplyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairApplyService {
    private final RepairApplyRepository repository;
    public RepairApplyService(RepairApplyRepository repository) { this.repository = repository; }
    public List<RepairApply> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public RepairApply detail(String id) {
        RepairApply record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "报修申请不存在");
        return record;
    }
    public RepairApply create(RepairApplySaveRequest request) { return repository.insert(request); }
    public RepairApply update(String id, RepairApplySaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
