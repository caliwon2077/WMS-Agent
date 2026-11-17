package com.wms.backend.modules.inspection.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.inspection.dto.CheckRecordSaveRequest;
import com.wms.backend.modules.inspection.entity.CheckRecord;
import com.wms.backend.modules.inspection.repository.CheckRecordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckRecordService {
    private final CheckRecordRepository repository;
    public CheckRecordService(CheckRecordRepository repository) { this.repository = repository; }
    public List<CheckRecord> list(String keyword, String status) { return repository.findAll(keyword, status); }
    public CheckRecord detail(String id) {
        CheckRecord record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "点检记录不存在");
        return record;
    }
    public CheckRecord create(CheckRecordSaveRequest request) { return repository.insert(request); }
    public CheckRecord update(String id, CheckRecordSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
