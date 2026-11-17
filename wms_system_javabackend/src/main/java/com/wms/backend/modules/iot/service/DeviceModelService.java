package com.wms.backend.modules.iot.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.iot.dto.DeviceModelSaveRequest;
import com.wms.backend.modules.iot.entity.DeviceModel;
import com.wms.backend.modules.iot.repository.DeviceModelRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DeviceModelService {
    private final DeviceModelRepository repository;
    public DeviceModelService(DeviceModelRepository repository) { this.repository = repository; }

    public List<DeviceModel> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public DeviceModel detail(String id) {
        DeviceModel record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "设备模型不存在");
        return record;
    }

    public DeviceModel create(DeviceModelSaveRequest request) { return repository.insert(request); }
    public DeviceModel update(String id, DeviceModelSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
