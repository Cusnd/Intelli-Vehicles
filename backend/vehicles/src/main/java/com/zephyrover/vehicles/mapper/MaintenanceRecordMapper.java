package com.zephyrover.vehicles.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyrover.vehicles.entity.MaintenanceRecord;

/**
 * 维保记录Mapper接口
 */
@Mapper
public interface MaintenanceRecordMapper extends BaseMapper<MaintenanceRecord> {

}
