package com.zephyrover.vehicles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyrover.vehicles.entity.MaintenanceItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 维保项目Mapper接口
 */
@Mapper
public interface MaintenanceItemMapper extends BaseMapper<MaintenanceItem> {

}
