package com.wms.backend.modules.maintenance.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.maintenance.dto.MaintenanceRecordSaveRequest;
import com.wms.backend.modules.maintenance.entity.MaintenanceRecord;
import com.wms.backend.modules.maintenance.repository.MaintenanceRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaintenanceRecordService {
    private final MaintenanceRecordRepository repository;
    public MaintenanceRecordService(MaintenanceRecordRepository repository) { this.repository = repository; }
    public List<MaintenanceRecord> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public MaintenanceRecord detail(String id) {
        MaintenanceRecord record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "保养记录不存在");
        return record;
    }
    public MaintenanceRecord create(MaintenanceRecordSaveRequest request) { return repository.insert(request); }
    public MaintenanceRecord update(String id, MaintenanceRecordSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
