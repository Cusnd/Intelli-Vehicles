# 智能车辆维保记录管理系统 - 后端

## 项目简介

基于Spring Boot 3.x开发的智能车辆维保记录管理系统，提供完整的车辆信息管理、维保记录管理、数据统计分析等功能。

## 技术栈

- **Spring Boot 3.0.2** - 主框架
- **Spring Security** - 安全框架  
- **MyBatis-Plus 3.5.3.1** - ORM框架
- **MySQL 8.0** - 数据库
- **JWT** - 认证方案
- **Maven** - 构建工具

## 项目结构

```
src/main/java/com/zephyrover/vehicles/
├── VehiclesApplication.java          # Spring Boot启动类
├── entity/                           # 实体类 (5个)
│   ├── User.java                     # 用户实体
│   ├── Vehicle.java                  # 车辆实体
│   ├── MaintenanceRecord.java        # 维保记录实体
│   ├── MaintenanceItem.java         # 维保项目实体
│   └── Log.java                      # 操作日志实体
├── mapper/                           # Mapper接口层 (5个)
│   ├── UserMapper.java               # 用户Mapper
│   ├── VehicleMapper.java            # 车辆Mapper
│   ├── MaintenanceRecordMapper.java  # 维保记录Mapper
│   ├── MaintenanceItemMapper.java    # 维保项目Mapper
│   └── LogMapper.java                # 日志Mapper
├── service/                          # 服务层 (5个接口 + 5个实现)
│   ├── UserService.java              # 用户服务接口
│   ├── VehicleService.java           # 车辆服务接口
│   ├── MaintenanceRecordService.java # 维保记录服务接口
│   ├── StatisticsService.java        # 统计服务接口
│   ├── LogService.java               # 日志服务接口
│   └── impl/                         # 服务实现类
│       ├── UserServiceImpl.java      # 用户服务实现
│       ├── VehicleServiceImpl.java   # 车辆服务实现
│       ├── MaintenanceRecordServiceImpl.java # 维保记录服务实现
│       ├── StatisticsServiceImpl.java # 统计服务实现
│       └── LogServiceImpl.java       # 日志服务实现
├── controller/                       # 控制器层 (6个)
│   ├── AuthController.java          # 认证控制器
│   ├── VehicleController.java       # 车辆控制器
│   ├── MaintenanceRecordController.java # 维保记录控制器
│   ├── StatisticsController.java    # 统计控制器
│   ├── LogController.java           # 日志控制器
│   └── TestController.java          # 测试控制器
├── config/                           # 配置类 (3个)
│   ├── MybatisPlusConfig.java        # MyBatis-Plus配置
│   ├── SecurityConfig.java          # Spring Security配置
│   └── JwtAuthenticationFilter.java # JWT认证过滤器
├── common/                           # 通用类 (2个)
│   ├── Result.java                   # 统一响应结果
│   └── PageResult.java               # 分页结果包装
└── util/                             # 工具类 (1个)
    └── JwtUtil.java                  # JWT工具类
```

## 运行项目

### 环境要求
- Java 17+
- MySQL 8.0+
- Maven 3.6+ (或使用项目内置Maven Wrapper)

### 启动步骤

1. **配置数据库**
   ```sql
   CREATE DATABASE vehicle_maintenance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. **修改配置文件**
   ```yaml
   # src/main/resources/application.yml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/vehicle_maintenance
       username: your_username  
       password: your_password
   ```

3. **启动项目**
   ```bash
   # 方式一：使用Maven Wrapper (推荐)
   .\mvnw.cmd spring-boot:run
   
   # 方式二：使用IDE导入运行
   # 方式三：双击 start.bat
   ```

4. **访问测试**
   - 服务地址：http://localhost:8081
   - 测试接口：http://localhost:8081/api/test/hello

## API接口 (共33个)

### 认证模块 (4个接口)
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/login` - 用户登录
- `GET /api/auth/profile` - 获取用户信息
- `PUT /api/auth/profile` - 更新用户信息

### 车辆管理 (5个接口)
- `GET /api/vehicles` - 分页查询车辆列表
- `GET /api/vehicles/{id}` - 根据ID查询车辆
- `POST /api/vehicles` - 添加车辆
- `PUT /api/vehicles/{id}` - 更新车辆
- `DELETE /api/vehicles/{id}` - 删除车辆

### 维保记录管理 (6个接口)
- `GET /api/records` - 分页查询维保记录
- `GET /api/records/{id}` - 查询维保记录详情
- `POST /api/records` - 添加维保记录
- `PUT /api/records/{id}` - 更新维保记录
- `DELETE /api/records/{id}` - 删除维保记录
- `GET /api/records/vehicle/{vehicleId}` - 按车辆查询记录

### 数据统计分析 (5个接口)
- `GET /api/statistics/monthly-cost` - 月度费用趋势
- `GET /api/statistics/popular-items` - 热门服务项目
- `GET /api/statistics/vehicle-summary/{id}` - 车辆维保概览
- `GET /api/statistics/overview` - 系统总体概览
- `GET /api/statistics/recent-records` - 最近维保记录

### 操作日志管理 (3个接口)
- `GET /api/logs` - 分页查询操作日志
- `POST /api/logs` - 记录操作日志
- `DELETE /api/logs/cleanup` - 清理过期日志

### 测试接口 (1个接口)
- `GET /api/test/hello` - 系统测试接口

**详细API文档请查看**: `API_DOCUMENTATION_DETAILED.md`

## 功能特点

✅ **完整实现的功能**
- 用户认证系统（注册/登录/JWT）
- 车辆信息管理（CRUD + 搜索）
- 维保记录管理（CRUD + 项目明细）
- 数据统计报表（月度趋势、热门项目、系统概览）
- 操作日志记录（分页查询、自动记录、清理）
- JWT认证拦截器（Token验证、权限控制）
- 统一响应格式
- MyBatis-Plus集成
- 数据库表设计

## 权限控制

- **公开接口**: `/api/auth/**`, `/api/test/**`
- **用户接口**: `/api/vehicles/**`, `/api/records/**`, `/api/statistics/**`
- **管理员接口**: `/api/logs/**`

## 安全配置

- 密码BCrypt加密
- JWT Token认证（24小时有效期）
- 跨域CORS支持
- SQL注入防护（MyBatis-Plus）

## 数据库设计

- **user** - 用户表
- **vehicle** - 车辆表
- **maintenance_record** - 维保记录表
- **maintenance_item** - 维保项目表
- **log** - 操作日志表

## 快速测试

```bash
# 1. 用户注册
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456","email":"test@example.com"}'

# 2. 用户登录
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"test","password":"123456"}'

# 3. 测试接口
curl http://localhost:8081/api/test/hello
```

## 注意事项

1. 首次启动会自动下载Maven和依赖，请保持网络畅通
2. 确保MySQL服务已启动且数据库配置正确
3. JWT Token在请求头中格式：`Authorization: Bearer {token}`


## 开发工具推荐

- **IDE**: IntelliJ IDEA、VS Code
- **API测试**: Postman、Apifox
- **数据库**: MySQL Workbench、Navicat

