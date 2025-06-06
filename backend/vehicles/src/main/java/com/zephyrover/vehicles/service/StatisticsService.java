package com.zephyrover.vehicles.service;

import java.util.List;
import java.util.Map;

/**
 * 数据统计服务接口
 */
public interface StatisticsService {

    /**
     * 获取每月费用趋势数据
     */
    Map<String, Object> getMonthlyCostTrend(Long vehicleId, Integer year);

    /**
     * 获取服务项目使用频率统计
     */
    List<Map<String, Object>> getPopularItems(int limit);

    /**
     * 获取车辆维保统计概览
     */
    Map<String, Object> getVehicleSummary(Long vehicleId);

    /**
     * 获取系统总体统计数据
     */
    Map<String, Object> getSystemOverview();

    /**
     * 获取最近维保记录
     */
    List<Map<String, Object>> getRecentRecords(int limit);
}
