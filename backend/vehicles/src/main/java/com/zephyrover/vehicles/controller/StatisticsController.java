package com.zephyrover.vehicles.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zephyrover.vehicles.common.Result;
import com.zephyrover.vehicles.service.StatisticsService;

/**
 * 数据统计控制器
 */
@RestController
@RequestMapping("/api/statistics")
@CrossOrigin(origins = "*")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    /**
     * 获取每月费用趋势数据
     */
    @GetMapping("/monthly-cost")
    public Result<Map<String, Object>> getMonthlyCost(
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Integer year) {
        try {
            Map<String, Object> data = statisticsService.getMonthlyCostTrend(vehicleId, year);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取月度费用趋势失败：" + e.getMessage());
        }
    }

    /**
     * 获取服务项目使用频率统计
     */
    @GetMapping("/popular-items")
    public Result<List<Map<String, Object>>> getPopularItems(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<Map<String, Object>> data = statisticsService.getPopularItems(limit);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取热门项目统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取车辆维保统计概览
     */
    @GetMapping("/vehicle-summary/{vehicleId}")
    public Result<Map<String, Object>> getVehicleSummary(@PathVariable Long vehicleId) {
        try {
            Map<String, Object> data = statisticsService.getVehicleSummary(vehicleId);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取车辆统计概览失败：" + e.getMessage());
        }
    }

    /**
     * 获取系统总体统计数据
     */
    @GetMapping("/overview")
    public Result<Map<String, Object>> getSystemOverview() {
        try {
            Map<String, Object> data = statisticsService.getSystemOverview();
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取系统概览失败：" + e.getMessage());
        }
    }

    /**
     * 获取最近维保记录
     */
    @GetMapping("/recent-records")
    public Result<List<Map<String, Object>>> getRecentRecords(
            @RequestParam(defaultValue = "5") int limit) {
        try {
            List<Map<String, Object>> data = statisticsService.getRecentRecords(limit);
            return Result.success(data);
        } catch (Exception e) {
            return Result.error("获取最近维保记录失败：" + e.getMessage());
        }
    }
}
