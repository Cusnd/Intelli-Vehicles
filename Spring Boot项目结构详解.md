# 🌱 Spring Boot项目结构详解

> 🎯 **学习目标**：理解Spring Boot项目的整体架构和各个组件的作用

## 📖 目录
1. [什么是Spring Boot？](#什么是spring-boot)
2. [MVC架构模式详解](#mvc架构模式详解)
3. [项目目录结构总览](#项目目录结构总览)
4. [各层详细讲解](#各层详细讲解)
5. [数据流向示例](#数据流向示例)
6. [配置文件说明](#配置文件说明)
7. [实际代码示例](#实际代码示例)

---

## 🤔 什么是Spring Boot？

### 简单理解
想象一下，Spring Boot就像是一个**智能助手**，帮你快速搭建一个网站的后台系统。

```
🏠 传统方式建房子：需要自己准备砖头、水泥、钢筋...
🏗️ Spring Boot建房子：提供预制模块，快速组装成房子
```

### 核心特点
- **开箱即用**：就像买电脑一样，系统已经装好了，插电就能用
- **约定大于配置**：有默认的规则，遵循规则就能正常工作
- **内嵌服务器**：不需要额外安装Tomcat等服务器

---

## 🏗️ MVC架构模式详解

MVC是一种软件设计模式，就像盖房子的蓝图一样：

### 📊 MVC = Model（模型）+ View（视图）+ Controller（控制器）

```
🎭 把开餐厅比作Web应用：

👨‍💼 Controller（控制器） = 服务员
   - 接待客人（接收请求）
   - 传达客人需求给厨房（调用业务逻辑）
   - 把菜品端给客人（返回响应）

🍳 Model（模型） = 厨房 + 食材仓库
   - 处理业务逻辑（做菜）
   - 管理数据（食材）

📱 View（视图） = 菜单 + 装盘
   - 展示给客人看的界面
   - 在我们项目中，前端Vue.js就是View
```

### 🔄 工作流程
```
客户端请求 → Controller → Service → Repository → Database
     ↑                                                ↓
    返回结果 ← Controller ← Service ← Repository ← 查询结果
```

---

## 📁 项目目录结构总览

```
backend/vehicles/
├── 📁 src/main/java/com/zephyrover/vehicles/
│   ├── 🎮 controller/          # 控制器层（服务员）
│   ├── 🏢 service/             # 业务逻辑层（厨房主管）
│   │   └── impl/               # 业务逻辑实现类
│   ├── 🗃️ entity/              # 实体类（数据模型）
│   ├── 🔍 mapper/              # 数据访问层（仓库管理员）
│   ├── ⚙️ config/              # 配置类（规章制度）
│   ├── 🛠️ util/                # 工具类（工具箱）
│   └── 📋 common/              # 通用类（公共设施）
├── 📁 src/main/resources/
│   ├── 📄 application.yml      # 主配置文件
│   └── 📁 mapper/              # SQL映射文件
└── 📁 src/test/                # 测试代码
```

---

## 🎯 各层详细讲解

### 1. 🎮 Controller层（控制器）
**作用**：像餐厅的服务员，负责接待客人

```java
@RestController                    // 告诉Spring这是个控制器
@RequestMapping("/api/vehicles")   // 这个控制器处理 /api/vehicles 路径的请求
public class VehicleController {
    
    // 当客户端发送 GET /api/vehicles 请求时，这个方法会处理
    @GetMapping
    public Result<Page<Vehicle>> getVehicleList() {
        // 1. 接收客户端请求
        // 2. 调用业务逻辑
        // 3. 返回结果
    }
}
```

**关键注解说明**：
- `@RestController`：告诉Spring这是个API控制器
- `@GetMapping`：处理GET请求（查询数据）
- `@PostMapping`：处理POST请求（创建数据）
- `@PutMapping`：处理PUT请求（更新数据）
- `@DeleteMapping`：处理DELETE请求（删除数据）

### 2. 🏢 Service层（业务逻辑）
**作用**：像餐厅的厨房主管，负责具体的业务处理

```java
@Service  // 告诉Spring这是个业务服务类
public class VehicleServiceImpl implements VehicleService {
    
    public Page<Vehicle> getVehicleList(VehicleQuery query) {
        // 这里写具体的业务逻辑
        // 比如：数据验证、业务规则处理、调用数据库等
    }
}
```

**为什么要分层？**
```
❌ 不分层：服务员既要接待客人，又要做菜，又要管理食材
   → 一团糟，难以维护

✅ 分层：各司其职，代码清晰
   → 服务员专门接待，厨师专门做菜，仓管专门管食材
```

### 3. 🗃️ Entity层（实体类）
**作用**：像数据库中表格的Java版本

```java
@Entity                    // 告诉Spring这是个数据库实体
@Table(name = "vehicle")   // 对应数据库中的 vehicle 表
public class Vehicle {
    
    @Id                    // 主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自动生成ID
    private Long id;
    
    @Column(name = "license_plate")  // 对应数据库字段 license_plate
    private String licensePlate;     // Java中用驼峰命名
    
    // 省略其他字段...
}
```

**数据库 vs Java对比**：
```sql
-- 数据库表
CREATE TABLE vehicle (
    id BIGINT PRIMARY KEY,
    license_plate VARCHAR(20),
    brand VARCHAR(50)
);
```

```java
// Java实体类
public class Vehicle {
    private Long id;           // 对应 id
    private String licensePlate; // 对应 license_plate  
    private String brand;      // 对应 brand
}
```

### 4. 🔍 Mapper层（数据访问）
**作用**：像仓库管理员，负责从数据库存取数据

```java
@Mapper  // 告诉MyBatis这是个数据访问接口
public interface VehicleMapper extends BaseMapper<Vehicle> {
    
    // 继承BaseMapper后，自动拥有基础的增删改查方法：
    // - insert(vehicle)     插入数据
    // - deleteById(id)      根据ID删除
    // - updateById(vehicle) 根据ID更新
    // - selectById(id)      根据ID查询
    // - selectList(query)   条件查询
}
```

### 5. ⚙️ Config层（配置类）
**作用**：像公司的规章制度，定义系统运行规则

```java
@Configuration  // 告诉Spring这是个配置类
public class SecurityConfig {
    
    @Bean  // 创建一个Bean（组件）
    public SecurityFilterChain filterChain(HttpSecurity http) {
        // 配置安全规则：哪些接口需要登录，哪些不需要
    }
}
```

### 6. 🛠️ Util层（工具类）
**作用**：像工具箱，存放各种通用的工具方法

```java
public class JwtUtil {
    
    // 生成JWT Token
    public static String generateToken(String username) {
        // 具体实现...
    }
    
    // 验证JWT Token
    public static boolean validateToken(String token) {
        // 具体实现...
    }
}
```

---

## 🔄 数据流向示例

让我们用"查询车辆列表"的例子来看整个流程：

### 步骤1：客户端发起请求
```javascript
// 前端Vue.js发送请求
axios.get('/api/vehicles?page=1&size=10')
```

### 步骤2：Controller接收请求
```java
@GetMapping
public Result<Page<Vehicle>> getVehicleList(
    @RequestParam int page,    // 接收page参数
    @RequestParam int size     // 接收size参数
) {
    // 调用业务层
    Page<Vehicle> vehicles = vehicleService.getVehicleList(page, size);
    return Result.success(vehicles);
}
```

### 步骤3：Service处理业务逻辑
```java
public Page<Vehicle> getVehicleList(int page, int size) {
    // 1. 验证参数
    if (page < 1) page = 1;
    if (size < 1) size = 10;
    
    // 2. 构建查询条件
    Page<Vehicle> pageInfo = new Page<>(page, size);
    
    // 3. 调用数据访问层
    return vehicleMapper.selectPage(pageInfo, null);
}
```

### 步骤4：Mapper查询数据库
```java
// MyBatis-Plus自动生成SQL并执行：
// SELECT * FROM vehicle LIMIT 0, 10
```

### 步骤5：返回结果
```java
// 数据层层返回：
Database → Mapper → Service → Controller → Frontend
```

---

## ⚙️ 配置文件说明

### application.yml 详解
```yaml
# 服务器配置
server:
  port: 8081              # 服务器端口号

# 数据库配置
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vehicle_maintenance
    username: root        # 数据库用户名
    password: 123456      # 数据库密码
    
  # JPA配置（数据库操作）
  jpa:
    hibernate:
      ddl-auto: update    # 自动更新表结构
    show-sql: true        # 显示SQL语句

# MyBatis-Plus配置
mybatis-plus:
  mapper-locations: classpath:mapper/*.xml  # SQL映射文件位置
  
# JWT配置
jwt:
  secret: mySecretKey     # JWT密钥
  expiration: 86400000    # Token过期时间（毫秒）
```

---

## 💻 实际代码示例

### 完整的车辆管理功能实现

#### 1. Entity（实体类）
```java
@Entity
@Table(name = "vehicle")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "license_plate", unique = true, nullable = false)
    private String licensePlate;  // 车牌号
    
    @Column(name = "brand")
    private String brand;         // 品牌
    
    @Column(name = "model")  
    private String model;         // 型号
    
    @Column(name = "owner_name")
    private String ownerName;     // 车主姓名
    
    @Column(name = "owner_phone")
    private String ownerPhone;    // 车主电话
    
    @Column(name = "create_time")
    private LocalDateTime createTime;  // 创建时间
    
    // 构造函数、getter、setter省略...
}
```

#### 2. Mapper（数据访问）
```java
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // BaseMapper提供了基础的CRUD方法
    // 如果需要复杂查询，可以自定义方法：
    
    // 根据车牌号查询
    Vehicle selectByLicensePlate(String licensePlate);
    
    // 根据车主姓名模糊查询
    List<Vehicle> selectByOwnerName(String ownerName);
}
```

#### 3. Service（业务逻辑）
```java
public interface VehicleService {
    Page<Vehicle> getVehicleList(int page, int size, String keyword);
    Vehicle getVehicleById(Long id);
    Vehicle saveVehicle(Vehicle vehicle);
    Vehicle updateVehicle(Vehicle vehicle);
    void deleteVehicle(Long id);
}

@Service
public class VehicleServiceImpl implements VehicleService {
    
    @Autowired
    private VehicleMapper vehicleMapper;
    
    @Override
    public Page<Vehicle> getVehicleList(int page, int size, String keyword) {
        Page<Vehicle> pageInfo = new Page<>(page, size);
        
        // 构建查询条件
        QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like("license_plate", keyword)
                   .or()
                   .like("owner_name", keyword);
        }
        
        return vehicleMapper.selectPage(pageInfo, wrapper);
    }
    
    @Override
    public Vehicle saveVehicle(Vehicle vehicle) {
        // 业务验证
        if (StringUtils.isEmpty(vehicle.getLicensePlate())) {
            throw new RuntimeException("车牌号不能为空");
        }
        
        // 检查车牌号是否已存在
        Vehicle existing = vehicleMapper.selectByLicensePlate(vehicle.getLicensePlate());
        if (existing != null) {
            throw new RuntimeException("车牌号已存在");
        }
        
        vehicle.setCreateTime(LocalDateTime.now());
        vehicleMapper.insert(vehicle);
        return vehicle;
    }
    
    // 其他方法省略...
}
```

#### 4. Controller（控制器）
```java
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    
    @Autowired
    private VehicleService vehicleService;
    
    @Autowired
    private LogService logService;
    
    // 获取车辆列表
    @GetMapping
    public Result<Page<Vehicle>> getVehicleList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword
    ) {
        try {
            Page<Vehicle> vehicles = vehicleService.getVehicleList(page, size, keyword);
            return Result.success(vehicles);
        } catch (Exception e) {
            return Result.error("查询失败：" + e.getMessage());
        }
    }
    
    // 添加车辆
    @PostMapping
    public Result<Vehicle> addVehicle(@RequestBody Vehicle vehicle, HttpServletRequest request) {
        try {
            Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);
            
            // 记录操作日志
            logService.recordUserAction(
                getCurrentUserId(request),
                "添加车辆：" + vehicle.getLicensePlate(),
                getClientIP(request)
            );
            
            return Result.success(savedVehicle);
        } catch (Exception e) {
            return Result.error("添加失败：" + e.getMessage());
        }
    }
    
    // 工具方法
    private Long getCurrentUserId(HttpServletRequest request) {
        // 从JWT Token中解析用户ID
        String token = request.getHeader("Authorization");
        return JwtUtil.getUserIdFromToken(token);
    }
    
    private String getClientIP(HttpServletRequest request) {
        // 获取客户端IP地址
        return request.getRemoteAddr();
    }
}
```

---

## 🔗 层与层之间的关系

### 依赖关系图
```
Controller  →  Service  →  Mapper  →  Database
    ↓           ↓          ↓
 接收请求    处理业务    操作数据
 返回响应    逻辑验证    SQL执行
```

### 依赖注入（@Autowired）
```java
// Spring会自动把需要的对象"注入"进来
@RestController
public class VehicleController {
    
    @Autowired  // Spring自动注入VehicleService实例
    private VehicleService vehicleService;
    
    // 使用时直接调用即可，不需要 new VehicleService()
    public Result getVehicles() {
        return vehicleService.getVehicleList();
    }
}
```

---

## 📝 总结

### 🎯 核心概念回顾
1. **MVC模式**：分层架构，各司其职
2. **依赖注入**：Spring自动管理对象关系
3. **注解驱动**：用注解告诉Spring怎么工作
4. **约定大于配置**：遵循命名规范，减少配置

### 🚀 学习建议
1. **先理解概念**：知道每一层是干什么的
2. **模仿代码**：照着例子写，慢慢理解
3. **多练习**：从简单的增删改查开始
4. **看日志**：出错时看控制台输出，了解执行流程

### 🔧 开发流程
```
1. 设计数据库表结构
2. 创建Entity实体类
3. 创建Mapper数据访问接口
4. 实现Service业务逻辑
5. 编写Controller接口
6. 测试API接口
7. 前后端联调
```

---

## 🎓 进阶学习方向

当你掌握了基础概念后，可以深入学习：

1. **Spring Security**：用户认证和权限控制
2. **MyBatis-Plus**：更高效的数据库操作
3. **Redis**：缓存技术
4. **Docker**：容器化部署
5. **微服务**：Spring Cloud

记住：**万丈高楼平地起**，先把基础打牢固！

---

*📚 这个文档会随着您的学习进度不断更新和完善。如果有任何疑问，欢迎随时提问！* 