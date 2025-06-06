package com.zephyrover.vehicles.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zephyrover.vehicles.common.PageResult;
import com.zephyrover.vehicles.common.Result;
import com.zephyrover.vehicles.entity.MaintenanceRecord;
import com.zephyrover.vehicles.service.MaintenanceRecordService;

/**
 * 维保记录控制器
 */
@RestController
@RequestMapping("/api/records")
@CrossOrigin(origins = "*")
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService recordService;

    /**
     * 分页查询维保记录
     */
    @GetMapping
    public Result<PageResult<MaintenanceRecord>> getRecordPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long vehicleId) {
        try {
            PageResult<MaintenanceRecord> pageResult = recordService.getRecordPage(page, size, vehicleId);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询维保记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询维保记录详情
     */
    @GetMapping("/{id}")
    public Result<MaintenanceRecord> getById(@PathVariable Long id) {
        try {
            MaintenanceRecord record = recordService.getById(id);
            if (record != null) {
                return Result.success(record);
            } else {
                return Result.error("维保记录不存在");
            }
        } catch (Exception e) {
            return Result.error("查询维保记录失败：" + e.getMessage());
        }
    }

    /**
     * 添加维保记录
     */
    @PostMapping
    public Result<String> save(@RequestBody MaintenanceRecord record) {
        try {
            boolean success = recordService.save(record);
            if (success) {
                return Result.success("添加维保记录成功");
            } else {
                return Result.error("添加维保记录失败");
            }
        } catch (Exception e) {
            return Result.error("添加维保记录失败：" + e.getMessage());
        }
    }

    /**
     * 更新维保记录
     */
    @PutMapping("/{id}")
    public Result<String> updateById(@PathVariable Long id, @RequestBody MaintenanceRecord record) {
        try {
            record.setId(id);
            boolean success = recordService.updateById(record);
            if (success) {
                return Result.success("更新维保记录成功");
            } else {
                return Result.error("更新维保记录失败");
            }
        } catch (Exception e) {
            return Result.error("更新维保记录失败：" + e.getMessage());
        }
    }

    /**
     * 删除维保记录
     */
    @DeleteMapping("/{id}")
    public Result<String> removeById(@PathVariable Long id) {
        try {
            boolean success = recordService.removeById(id);
            if (success) {
                return Result.success("删除维保记录成功");
            } else {
                return Result.error("删除维保记录失败");
            }
        } catch (Exception e) {
            return Result.error("删除维保记录失败：" + e.getMessage());
        }
    }

    /**
     * 根据车辆ID查询维保记录
     */
    @GetMapping("/vehicle/{vehicleId}")
    public Result<List<MaintenanceRecord>> getByVehicleId(@PathVariable Long vehicleId) {
        try {
            List<MaintenanceRecord> records = recordService.getByVehicleId(vehicleId);
            return Result.success(records);
        } catch (Exception e) {
            return Result.error("查询车辆维保记录失败：" + e.getMessage());
        }
    }
}
