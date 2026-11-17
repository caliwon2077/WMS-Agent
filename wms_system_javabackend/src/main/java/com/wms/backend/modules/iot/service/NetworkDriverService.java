package com.wms.backend.modules.iot.service;

import com.wms.backend.common.exception.BusinessException;
import com.wms.backend.modules.iot.dto.NetworkDriverSaveRequest;
import com.wms.backend.modules.iot.entity.NetworkDriver;
import com.wms.backend.modules.iot.repository.NetworkDriverRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class NetworkDriverService {
    private final NetworkDriverRepository repository;
    public NetworkDriverService(NetworkDriverRepository repository) { this.repository = repository; }

    public List<NetworkDriver> list(String keyword, String status) { return repository.findAll(keyword, status); }

    public NetworkDriver detail(String id) {
        NetworkDriver record = repository.findById(id);
        if (record == null) throw new BusinessException("NOT_FOUND", "网络驱动不存在");
        return record;
    }

    public NetworkDriver create(NetworkDriverSaveRequest request) { return repository.insert(request); }
    public NetworkDriver update(String id, NetworkDriverSaveRequest request) { detail(id); return repository.update(id, request); }
    public void delete(String id) { detail(id); repository.deleteById(id); }
}
