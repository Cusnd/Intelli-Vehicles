package com.zephyrover.vehicles.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyrover.vehicles.entity.Vehicle;

/**
 * 车辆服务接口
 */
public interface VehicleService {

    /**
     * 分页查询车辆列表
     */
    IPage<Vehicle> getVehiclePage(int page, int size, String keyword);

    /**
     * 根据ID查询车辆
     */
    Vehicle getById(Long id);

    /**
     * 保存车辆
     */
    boolean save(Vehicle vehicle);

    /**
     * 更新车辆
     */
    boolean updateById(Vehicle vehicle);

    /**
     * 删除车辆
     */
    boolean removeById(Long id);
}
