package com.zephyrover.vehicles.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyrover.vehicles.entity.Vehicle;
import com.zephyrover.vehicles.mapper.VehicleMapper;
import com.zephyrover.vehicles.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 车辆服务实现类
 */
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
        Page<Vehicle> pageInfo = new Page<>(page, size);
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("license_plate", keyword)
                    .or()
                    .like("owner_name", keyword)
                    .or()
                    .like("brand", keyword);
        }

        return vehicleMapper.selectPage(pageInfo, queryWrapper);
    }

    @Override
    public Vehicle getById(Long id) {
        return vehicleMapper.selectById(id);
    }

    @Override
    public boolean save(Vehicle vehicle) {
        return vehicleMapper.insert(vehicle) > 0;
    }

    @Override
    public boolean updateById(Vehicle vehicle) {
        return vehicleMapper.updateById(vehicle) > 0;
    }

    @Override
    public boolean removeById(Long id) {
        return vehicleMapper.deleteById(id) > 0;
    }
}
