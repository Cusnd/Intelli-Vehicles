package com.zephyrover.vehicles.service;

import com.zephyrover.vehicles.common.PageResult;
import com.zephyrover.vehicles.entity.MaintenanceRecord;

import java.util.List;

/**
 * 维保记录服务接口
 */
public interface MaintenanceRecordService {

    /**
     * 分页查询维保记录
     */
    PageResult<MaintenanceRecord> getRecordPage(int page, int size, Long vehicleId);

    /**
     * 根据ID查询维保记录
     */
    MaintenanceRecord getById(Long id);

    /**
     * 保存维保记录（包含维保项目）
     */
    boolean save(MaintenanceRecord record);

    /**
     * 更新维保记录
     */
    boolean updateById(MaintenanceRecord record);

    /**
     * 删除维保记录
     */
    boolean removeById(Long id);

    /**
     * 根据车辆ID查询维保记录
     */
    List<MaintenanceRecord> getByVehicleId(Long vehicleId);
}
