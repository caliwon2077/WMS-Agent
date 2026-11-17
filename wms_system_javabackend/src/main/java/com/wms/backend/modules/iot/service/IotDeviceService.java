package com.wms.backend.modules.iot.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.iot.dto.IotDeviceSaveRequest;
import com.wms.backend.modules.iot.entity.IotDevice;
import com.wms.backend.modules.iot.repository.IotDeviceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class IotDeviceService {
    private final IotDeviceRepository repository;
    public IotDeviceService(IotDeviceRepository repository) { this.repository = repository; }

    public List<IotDevice> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public IotDevice detail(String id) {
        IotDevice record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "设备实例不存在");
        return record;
    }

    public IotDevice create(IotDeviceSaveRequest request) { return repository.insert(request); }
    public IotDevice update(String id, IotDeviceSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
