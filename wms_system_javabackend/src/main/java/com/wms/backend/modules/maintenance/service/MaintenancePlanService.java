package com.wms.backend.modules.maintenance.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.maintenance.dto.MaintenancePlanSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenancePlan;
import com.wms.backend.modules.maintenance.repository.MaintenancePlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenancePlanService {
    private final MaintenancePlanRepository repository;
    public MaintenancePlanService(MaintenancePlanRepository repository) { this.repository = repository; }
    public List<MaintenancePlan> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public MaintenancePlan detail(String id) {
        MaintenancePlan record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "保养计划不存在");
        return record;
    }
    public MaintenancePlan create(MaintenancePlanSaveRequest request) { return repository.insert(request); }
    public MaintenancePlan update(String id, MaintenancePlanSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
