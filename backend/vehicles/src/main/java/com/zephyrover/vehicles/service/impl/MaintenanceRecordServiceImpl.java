package com.zephyrover.vehicles.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zephyrover.vehicles.common.PageResult;
import com.zephyrover.vehicles.entity.MaintenanceItem;
import com.zephyrover.vehicles.entity.MaintenanceRecord;
import com.zephyrover.vehicles.mapper.MaintenanceItemMapper;
import com.zephyrover.vehicles.mapper.MaintenanceRecordMapper;
import com.zephyrover.vehicles.service.MaintenanceRecordService;

/**
 * 维保记录服务实现类
 */
@Service
public class MaintenanceRecordServiceImpl implements MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordMapper recordMapper;

    @Autowired
    private MaintenanceItemMapper itemMapper;

    @Override
    public PageResult<MaintenanceRecord> getRecordPage(int page, int size, Long vehicleId) {
        Page<MaintenanceRecord> pageInfo = new Page<>(page, size);
        QueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<>();

        if (vehicleId != null) {
            queryWrapper.eq("vehicle_id", vehicleId);
        }

        queryWrapper.orderByDesc("service_date");

        IPage<MaintenanceRecord> pageResult = recordMapper.selectPage(pageInfo, queryWrapper);

        // 为每个记录查询对应的维保项目
        for (MaintenanceRecord record : pageResult.getRecords()) {
            List<MaintenanceItem> items = getItemsByRecordId(record.getId());
            record.setItems(items);
        }

        return new PageResult<>(
                pageResult.getTotal(),
                pageResult.getRecords(),
                pageResult.getCurrent(),
                pageResult.getSize()
        );
    }

    @Override
    public MaintenanceRecord getById(Long id) {
        MaintenanceRecord record = recordMapper.selectById(id);
        if (record != null) {
            List<MaintenanceItem> items = getItemsByRecordId(id);
            record.setItems(items);
        }
        return record;
    }

    @Override
    @Transactional
    public boolean save(MaintenanceRecord record) {
        // 计算总费用
        BigDecimal totalCost = BigDecimal.ZERO;
        if (record.getItems() != null && !record.getItems().isEmpty()) {
            for (MaintenanceItem item : record.getItems()) {
                if (item.getItemCost() != null) {
                    totalCost = totalCost.add(item.getItemCost());
                }
            }
        }
        record.setCost(totalCost);

        // 保存维保记录
        int result = recordMapper.insert(record);
        if (result > 0) {
            // 保存维保项目
            if (record.getItems() != null && !record.getItems().isEmpty()) {
                for (MaintenanceItem item : record.getItems()) {
                    item.setRecordId(record.getId());
                    itemMapper.insert(item);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean updateById(MaintenanceRecord record) {
        // 删除原有的维保项目
        QueryWrapper<MaintenanceItem> wrapper = new QueryWrapper<>();
        wrapper.eq("record_id", record.getId());
        itemMapper.delete(wrapper);

        // 重新计算总费用
        BigDecimal totalCost = BigDecimal.ZERO;
        if (record.getItems() != null && !record.getItems().isEmpty()) {
            for (MaintenanceItem item : record.getItems()) {
                if (item.getItemCost() != null) {
                    totalCost = totalCost.add(item.getItemCost());
                }
            }
        }
        record.setCost(totalCost);

        // 更新维保记录
        int result = recordMapper.updateById(record);
        if (result > 0) {
            // 保存新的维保项目
            if (record.getItems() != null && !record.getItems().isEmpty()) {
                for (MaintenanceItem item : record.getItems()) {
                    item.setRecordId(record.getId());
                    itemMapper.insert(item);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean removeById(Long id) {
        // 删除相关的维保项目
        QueryWrapper<MaintenanceItem> wrapper = new QueryWrapper<>();
        wrapper.eq("record_id", id);
        itemMapper.delete(wrapper);

        // 删除维保记录
        return recordMapper.deleteById(id) > 0;
    }

    @Override
    public List<MaintenanceRecord> getByVehicleId(Long vehicleId) {
        QueryWrapper<MaintenanceRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("vehicle_id", vehicleId);
        queryWrapper.orderByDesc("service_date");

        List<MaintenanceRecord> records = recordMapper.selectList(queryWrapper);

        // 为每个记录查询对应的维保项目
        for (MaintenanceRecord record : records) {
            List<MaintenanceItem> items = getItemsByRecordId(record.getId());
            record.setItems(items);
        }

        return records;
    }

    /**
     * 根据记录ID查询维保项目
     */
    private List<MaintenanceItem> getItemsByRecordId(Long recordId) {
        QueryWrapper<MaintenanceItem> wrapper = new QueryWrapper<>();
        wrapper.eq("record_id", recordId);
        return itemMapper.selectList(wrapper);
    }
}
