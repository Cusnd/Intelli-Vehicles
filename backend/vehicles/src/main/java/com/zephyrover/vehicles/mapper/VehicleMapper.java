package com.zephyrover.vehicles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyrover.vehicles.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆Mapper接口
 */
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {

}
