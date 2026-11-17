package com.wms.backend.modules.inspection.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.inspection.dto.CheckPlanSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckPlan;
import com.wms.backend.modules.inspection.repository.CheckPlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckPlanService {
    private final CheckPlanRepository repository;
    public CheckPlanService(CheckPlanRepository repository) { this.repository = repository; }
    public List<CheckPlan> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public CheckPlan detail(String id) {
        CheckPlan record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "点检计划不存在");
        return record;
    }
    public CheckPlan create(CheckPlanSaveRequest request) { return repository.insert(request); }
    public CheckPlan update(String id, CheckPlanSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
