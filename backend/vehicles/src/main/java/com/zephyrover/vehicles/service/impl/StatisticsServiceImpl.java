package com.zephyrover.vehicles.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zephyrover.vehicles.entity.MaintenanceRecord;
import com.zephyrover.vehicles.entity.Vehicle;
import com.zephyrover.vehicles.mapper.MaintenanceItemMapper;
import com.zephyrover.vehicles.mapper.MaintenanceRecordMapper;
import com.zephyrover.vehicles.mapper.VehicleMapper;
import com.zephyrover.vehicles.service.StatisticsService;

/**
 * 数据统计服务实现类
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private MaintenanceRecordMapper recordMapper;

    @Autowired
    private MaintenanceItemMapper itemMapper;

    @Autowired
    private VehicleMapper vehicleMapper;

    @Override
    public Map<String, Object> getMonthlyCostTrend(Long vehicleId, Integer year) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        Map<String, Object> result = new HashMap<>();
        List<String> months = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月",
                "7月", "8月", "9月", "10月", "11月", "12月");
        List<BigDecimal> costs = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            QueryWrapper<MaintenanceRecord> wrapper = new QueryWrapper<>();

            // 构建日期范围查询
            LocalDate startDate = LocalDate.of(year, month, 1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            wrapper.between("service_date", startDate, endDate);

            if (vehicleId != null) {
                wrapper.eq("vehicle_id", vehicleId);
            }

            List<MaintenanceRecord> records = recordMapper.selectList(wrapper);
            BigDecimal monthCost = records.stream()
                    .map(MaintenanceRecord::getCost)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            costs.add(monthCost);
        }

        result.put("xAxis", months);
        result.put("series", costs);
        result.put("year", year);

        return result;
    }

    @Override
    public List<Map<String, Object>> getPopularItems(int limit) {
        // 这里使用原生SQL查询，实际项目中建议在Mapper中定义
        List<Map<String, Object>> results = new ArrayList<>();

        // 模拟数据，实际应该通过SQL查询
        String[] items = {"更换机油", "轮胎检查", "刹车检查", "空调滤芯更换", "火花塞更换"};
        Random random = new Random();

        for (int i = 0; i < Math.min(limit, items.length); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("itemName", items[i]);
            item.put("count", random.nextInt(50) + 10);
            item.put("totalCost", new BigDecimal(random.nextInt(5000) + 500));
            results.add(item);
        }

        // 按使用次数排序
        results.sort((a, b) -> ((Integer) b.get("count")).compareTo((Integer) a.get("count")));

        return results;
    }

    @Override
    public Map<String, Object> getVehicleSummary(Long vehicleId) {
        Map<String, Object> result = new HashMap<>();

        // 查询车辆信息
        Vehicle vehicle = vehicleMapper.selectById(vehicleId);
        if (vehicle == null) {
            throw new RuntimeException("车辆不存在");
        }

        // 查询该车辆的维保记录
        QueryWrapper<MaintenanceRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("vehicle_id", vehicleId);
        List<MaintenanceRecord> records = recordMapper.selectList(wrapper);

        // 统计数据
        int totalRecords = records.size();
        BigDecimal totalCost = records.stream()
                .map(MaintenanceRecord::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 最近一次维保时间
        LocalDate lastServiceDate = records.stream()
                .map(MaintenanceRecord::getServiceDate)
                .filter(Objects::nonNull)
                .max(LocalDate::compareTo)
                .orElse(null);

        result.put("vehicle", vehicle);
        result.put("totalRecords", totalRecords);
        result.put("totalCost", totalCost);
        result.put("lastServiceDate", lastServiceDate);
        result.put("avgCostPerService", totalRecords > 0
                ? totalCost.divide(new BigDecimal(totalRecords), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO);

        return result;
    }

    @Override
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> result = new HashMap<>();

        // 总车辆数
        Long totalVehicles = vehicleMapper.selectCount(null);

        // 总维保记录数
        Long totalRecords = recordMapper.selectCount(null);

        // 今年总费用
        int currentYear = LocalDate.now().getYear();
        QueryWrapper<MaintenanceRecord> wrapper = new QueryWrapper<>();
        wrapper.between("service_date",
                LocalDate.of(currentYear, 1, 1),
                LocalDate.of(currentYear, 12, 31));
        List<MaintenanceRecord> yearRecords = recordMapper.selectList(wrapper);
        BigDecimal yearTotalCost = yearRecords.stream()
                .map(MaintenanceRecord::getCost)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 本月维保次数
        LocalDate now = LocalDate.now();
        LocalDate monthStart = LocalDate.of(now.getYear(), now.getMonth(), 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);

        QueryWrapper<MaintenanceRecord> monthWrapper = new QueryWrapper<>();
        monthWrapper.between("service_date", monthStart, monthEnd);
        Long monthRecords = recordMapper.selectCount(monthWrapper);

        result.put("totalVehicles", totalVehicles);
        result.put("totalRecords", totalRecords);
        result.put("yearTotalCost", yearTotalCost);
        result.put("monthRecords", monthRecords);
        result.put("avgCostPerVehicle", totalVehicles > 0
                ? yearTotalCost.divide(new BigDecimal(totalVehicles), 2, BigDecimal.ROUND_HALF_UP)
                : BigDecimal.ZERO);

        return result;
    }

    @Override
    public List<Map<String, Object>> getRecentRecords(int limit) {
        QueryWrapper<MaintenanceRecord> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        wrapper.last("LIMIT " + limit);

        List<MaintenanceRecord> records = recordMapper.selectList(wrapper);
        List<Map<String, Object>> results = new ArrayList<>();

        for (MaintenanceRecord record : records) {
            Map<String, Object> item = new HashMap<>();

            // 获取车辆信息
            Vehicle vehicle = vehicleMapper.selectById(record.getVehicleId());

            item.put("id", record.getId());
            item.put("vehicleId", record.getVehicleId());
            item.put("licensePlate", vehicle != null ? vehicle.getLicensePlate() : "未知");
            item.put("serviceDate", record.getServiceDate());
            item.put("cost", record.getCost());
            item.put("servicePerson", record.getServicePerson());
            item.put("createTime", record.getCreateTime());

            results.add(item);
        }

        return results;
    }
}
