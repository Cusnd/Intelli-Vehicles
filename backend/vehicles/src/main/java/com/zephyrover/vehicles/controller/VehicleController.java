package com.zephyrover.vehicles.controller;

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

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zephyrover.vehicles.common.Result;
import com.zephyrover.vehicles.entity.Vehicle;
import com.zephyrover.vehicles.service.LogService;
import com.zephyrover.vehicles.service.VehicleService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 车辆控制器
 */
@RestController
@RequestMapping("/api/vehicles")
@CrossOrigin(origins = "*")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private LogService logService;

    /**
     * 分页查询车辆列表
     */
    @GetMapping
    public Result<IPage<Vehicle>> getVehiclePage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        try {
            IPage<Vehicle> pageResult = vehicleService.getVehiclePage(page, size, keyword);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("查询车辆列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询车辆
     */
    @GetMapping("/{id}")
    public Result<Vehicle> getById(@PathVariable Long id) {
        try {
            Vehicle vehicle = vehicleService.getById(id);
            if (vehicle != null) {
                return Result.success(vehicle);
            } else {
                return Result.error("车辆不存在");
            }
        } catch (Exception e) {
            return Result.error("查询车辆失败：" + e.getMessage());
        }
    }

    /**
     * 添加车辆
     */
    @PostMapping
    public Result<String> save(@RequestBody Vehicle vehicle, HttpServletRequest request) {
        try {
            boolean success = vehicleService.save(vehicle);
            if (success) {
                // 记录操作日志
                String ipAddress = getClientIpAddress(request);
                logService.recordUserOperation(vehicle.getUserId(),
                        "添加车辆：" + vehicle.getLicensePlate(), ipAddress);

                return Result.success("添加车辆成功");
            } else {
                return Result.error("添加车辆失败");
            }
        } catch (Exception e) {
            return Result.error("添加车辆失败：" + e.getMessage());
        }
    }

    /**
     * 更新车辆
     */
    @PutMapping("/{id}")
    public Result<String> updateById(@PathVariable Long id, @RequestBody Vehicle vehicle, HttpServletRequest request) {
        try {
            Vehicle oldVehicle = vehicleService.getById(id);
            vehicle.setId(id);
            boolean success = vehicleService.updateById(vehicle);
            if (success) {
                // 记录操作日志
                String ipAddress = getClientIpAddress(request);
                logService.recordUserOperation(vehicle.getUserId(),
                        "更新车辆：" + (oldVehicle != null ? oldVehicle.getLicensePlate() : ""), ipAddress);

                return Result.success("更新车辆成功");
            } else {
                return Result.error("更新车辆失败");
            }
        } catch (Exception e) {
            return Result.error("更新车辆失败：" + e.getMessage());
        }
    }

    /**
     * 删除车辆
     */
    @DeleteMapping("/{id}")
    public Result<String> removeById(@PathVariable Long id, HttpServletRequest request) {
        try {
            Vehicle vehicle = vehicleService.getById(id);
            boolean success = vehicleService.removeById(id);
            if (success) {
                // 记录操作日志
                String ipAddress = getClientIpAddress(request);
                logService.recordUserOperation(vehicle != null ? vehicle.getUserId() : null,
                        "删除车辆：" + (vehicle != null ? vehicle.getLicensePlate() : ""), ipAddress);

                return Result.success("删除车辆成功");
            } else {
                return Result.error("删除车辆失败");
            }
        } catch (Exception e) {
            return Result.error("删除车辆失败：" + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        // 处理多个IP的情况，取第一个IP
        if (ipAddress != null && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0].trim();
        }
        return ipAddress;
    }
}
