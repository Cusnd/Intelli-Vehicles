# 🎓 车辆管理系统 - Spring Boot + MyBatis-Plus + Spring Security 知识点总结

> 📝 **项目概述**
> 
> 本项目是一个完整的车辆维护管理系统，采用前后端分离架构，涵盖了Spring Boot生态的核心技术栈。

## 📚 目录
1. [Spring Boot 知识点](#spring-boot-知识点)
2. [MyBatis-Plus 知识点](#mybatis-plus-知识点) 
3. [Spring Security 知识点](#spring-security-知识点)
4. [项目中的实际应用](#项目中的实际应用)
5. [技术难点与解决方案](#技术难点与解决方案)

---

## 🚀 Spring Boot 知识点

### 1. 核心概念与特性

#### 🔹 自动配置 (Auto Configuration)
```java
// 项目中的应用启动类
@SpringBootApplication  // 包含了 @Configuration, @EnableAutoConfiguration, @ComponentScan
public class VehiclesApplication {
    public static void main(String[] args) {
        SpringApplication.run(VehiclesApplication.class, args);
    }
}
```

**涉及的自动配置：**
- **DataSource自动配置**：根据application.yml中的数据库配置自动创建数据源
- **JPA/Hibernate自动配置**：自动配置JPA相关组件
- **Web MVC自动配置**：自动配置DispatcherServlet、视图解析器等
- **Security自动配置**：自动配置Spring Security（项目中有自定义配置）

#### 🔹 起步依赖 (Starter Dependencies)
```xml
<!-- 项目中使用的关键起步依赖 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>  <!-- Web开发起步依赖 -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>  <!-- 安全起步依赖 -->
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>  <!-- JPA起步依赖 -->
</dependency>
```

### 2. 配置管理

#### 🔹 外部化配置 (Externalized Configuration)
```yaml
# application.yml - 项目中的配置示例
spring:
  application:
    name: vehicles  # 应用名称
    
  datasource:  # 数据源配置
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/vehicle_maintenance
    username: root
    password: 521137Server@@
    
  jpa:  # JPA配置
    hibernate:
      ddl-auto: update  # 自动更新表结构
    show-sql: true      # 显示SQL语句
    
server:
  port: 8081           # 服务端口
  servlet:
    context-path: /    # 上下文路径
    
logging:  # 日志配置
  level:
    com.zephyrover.vehicles: debug  # 项目包日志级别
    root: info                      # 根日志级别
```

#### 🔹 配置属性绑定
```java
// 通过@Value注解获取配置值（项目中潜在用法）
@Value("${server.port}")
private int serverPort;

// 通过Environment获取配置（更推荐的方式）
@Autowired
private Environment environment;
```

### 3. Bean管理与依赖注入

#### 🔹 组件扫描与注册
```java
// 项目中的各种组件注解
@RestController  // 控制器组件
@Service        // 服务层组件
@Configuration  // 配置类组件
@Component      // 通用组件
@Mapper         // MyBatis映射器组件
```

#### 🔹 依赖注入
```java
// 项目中常见的依赖注入方式
@Autowired
private UserService userService;  // 字段注入

@Autowired
private LogService logService;    // 字段注入

// 构造器注入（更推荐的方式）
private final VehicleMapper vehicleMapper;

public VehicleServiceImpl(VehicleMapper vehicleMapper) {
    this.vehicleMapper = vehicleMapper;
}
```

### 4. Web开发

#### 🔹 RESTful API设计
```java
// 项目中的REST控制器示例
@RestController
@RequestMapping("/api/vehicles")  // 基础路径
@CrossOrigin(origins = "*")       // 跨域支持
public class VehicleController {

    @GetMapping("")  // GET /api/vehicles - 查询车辆列表
    public Result<IPage<Vehicle>> getVehicleList(/* 参数 */) { }

    @PostMapping("")  // POST /api/vehicles - 创建车辆
    public Result<Vehicle> createVehicle(@RequestBody Vehicle vehicle) { }

    @PutMapping("/{id}")  // PUT /api/vehicles/{id} - 更新车辆
    public Result<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicle) { }

    @DeleteMapping("/{id}")  // DELETE /api/vehicles/{id} - 删除车辆
    public Result<String> deleteVehicle(@PathVariable Long id) { }
}
```

#### 🔹 请求参数处理
```java
// 项目中的参数绑定示例
@GetMapping("")
public Result<IPage<Vehicle>> getVehicleList(
    @RequestParam(defaultValue = "1") int page,      // 查询参数
    @RequestParam(defaultValue = "10") int size,     // 查询参数
    @RequestParam(required = false) String keyword   // 可选参数
) { }

@PostMapping("/login")
public Result<Map<String, Object>> login(
    @RequestBody User loginUser,          // JSON请求体
    HttpServletRequest request            // 原生请求对象
) { }

@PutMapping("/{id}")
public Result<Vehicle> updateVehicle(
    @PathVariable Long id,                // 路径变量
    @RequestBody Vehicle vehicle          // JSON请求体
) { }
```

#### 🔹 响应处理
```java
// 项目中的统一响应格式
public class Result<T> {
    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "操作成功", data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
}
```

### 5. 异常处理

#### 🔹 全局异常处理（项目中的潜在实现）
```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return Result.error("服务器内部错误：" + e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<String> handleIllegalArgument(IllegalArgumentException e) {
        return Result.error("参数错误：" + e.getMessage());
    }
}
```

---

## 🗄️ MyBatis-Plus 知识点

### 1. 核心概念

#### 🔹 BaseMapper继承
```java
// 项目中的Mapper接口
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // 继承BaseMapper后自动拥有基础CRUD方法
    // selectById, selectList, insert, updateById, deleteById 等
}

@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 自定义方法
    User findByUsername(String username);
}
```

#### 🔹 实体类注解
```java
// 项目中的实体类示例
@TableName("user")  // 指定表名
public class User {

    @TableId(value = "id", type = IdType.AUTO)  // 主键策略
    private Long id;

    @TableField("username")  // 字段映射
    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)  // JSON序列化控制
    @TableField("password")
    private String password;

    @TableField(value = "create_time", fill = FieldFill.INSERT)  // 自动填充
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
```

### 2. 配置与插件

#### 🔹 MyBatis-Plus配置
```java
// 项目中的MyBatis-Plus配置类
@Configuration
public class MybatisPlusConfig {

    /**
     * 分页插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * 自动填充配置
     */
    @Component
    public static class MyMetaObjectHandler implements MetaObjectHandler {
        @Override
        public void insertFill(MetaObject metaObject) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}
```

#### 🔹 application.yml中的MyBatis-Plus配置
```yaml
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
  global-config:
    db-config:
      id-type: auto  # 主键自增策略
  mapper-locations: classpath*:/mapper/*.xml  # Mapper XML位置
```

### 3. CRUD操作

#### 🔹 基础CRUD（项目中的实际使用）
```java
// 项目中Service实现类的CRUD操作
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    // 查询单个
    @Override
    public Vehicle getById(Long id) {
        return vehicleMapper.selectById(id);
    }

    // 分页查询
    @Override
    public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
        Page<Vehicle> pageInfo = new Page<>(page, size);
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("license_plate", keyword)
                    .or()
                    .like("owner_name", keyword)
                    .or()
                    .like("brand", keyword);
        }

        queryWrapper.orderByDesc("create_time");
        return vehicleMapper.selectPage(pageInfo, queryWrapper);
    }

    // 新增
    @Override
    public boolean save(Vehicle vehicle) {
        return vehicleMapper.insert(vehicle) > 0;
    }

    // 更新
    @Override
    public boolean updateById(Vehicle vehicle) {
        return vehicleMapper.updateById(vehicle) > 0;
    }

    // 删除
    @Override
    public boolean removeById(Long id) {
        return vehicleMapper.deleteById(id) > 0;
    }
}
```

### 4. 条件构造器

#### 🔹 QueryWrapper使用
```java
// 项目中的复杂查询示例
public List<Vehicle> complexQuery() {
    QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
    
    // 等值查询
    wrapper.eq("brand", "丰田");
    
    // 模糊查询
    wrapper.like("license_plate", "京");
    
    // 范围查询
    wrapper.between("create_time", startDate, endDate);
    
    // 排序
    wrapper.orderByDesc("create_time");
    
    // 复杂条件：(品牌='丰田' OR 品牌='本田') AND 车主姓名不为空
    wrapper.and(w -> w.eq("brand", "丰田").or().eq("brand", "本田"))
           .isNotNull("owner_name");
    
    return vehicleMapper.selectList(wrapper);
}
```

#### 🔹 UpdateWrapper使用
```java
// 条件更新示例
public boolean updateByCondition() {
    UpdateWrapper<Vehicle> wrapper = new UpdateWrapper<>();
    wrapper.eq("owner_name", "张三")
           .set("brand", "更新后的品牌");
    
    return vehicleMapper.update(null, wrapper) > 0;
}
```

### 5. 分页插件

#### 🔹 分页查询实现
```java
// 项目中的分页查询
@Override
public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
    // 1. 创建分页对象
    Page<Vehicle> pageInfo = new Page<>(page, size);
    
    // 2. 构建查询条件
    QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
    if (StringUtils.hasText(keyword)) {
        queryWrapper.like("license_plate", keyword)
                .or()
                .like("owner_name", keyword);
    }
    
    // 3. 执行分页查询
    return vehicleMapper.selectPage(pageInfo, queryWrapper);
}
```

### 6. 自动填充

#### 🔹 时间字段自动填充
```java
// 实体类中的自动填充注解
@TableField(value = "create_time", fill = FieldFill.INSERT)
private LocalDateTime createTime;

@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
private LocalDateTime updateTime;

// 配置类中的填充处理器
@Override
public void insertFill(MetaObject metaObject) {
    this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
}
```

---

## 🔐 Spring Security 知识点

### 1. 安全配置

#### 🔹 SecurityConfig配置类
```java
// 项目中的Spring Security配置
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()  // 启用CORS，禁用CSRF
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 无状态会话
                .and()
                .authorizeHttpRequests(authz -> authz
                    .requestMatchers("/api/auth/**").permitAll()    // 认证接口放行
                    .requestMatchers("/api/test/**").permitAll()    // 测试接口放行
                    .anyRequest().hasRole("USER")                   // 其他接口需要USER角色
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // 添加JWT过滤器

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // BCrypt密码编码器
    }
}
```

### 2. 认证与授权

#### 🔹 用户认证
```java
// 项目中的用户登录实现
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public String login(String username, String password) {
        // 1. 查找用户
        User user = userMapper.findByUsername(username);
        if (user == null) {
            return null;
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // 3. 生成JWT Token
        return jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    public boolean register(User user) {
        // 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        return userMapper.insert(user) > 0;
    }
}
```

#### 🔹 角色与权限
```java
// 项目中的角色配置
.authorizeHttpRequests(authz -> authz
    .requestMatchers("/api/auth/**").permitAll()     // 无需认证
    .requestMatchers("/api/test/**").permitAll()     // 无需认证
    .anyRequest().hasRole("USER")                    // 需要USER角色
)

// 用户实体中的角色字段
@TableField("role")
private String role = "USER";  // 默认角色为USER
```

### 3. JWT认证

#### 🔹 JWT过滤器
```java
// 项目中的JWT认证过滤器
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String token = request.getHeader("Authorization");
        
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 移除"Bearer "前缀
            
            try {
                // 验证并解析JWT
                Claims claims = jwtUtil.parseToken(token);
                String username = claims.getSubject();
                String role = claims.get("role", String.class);
                
                // 创建认证对象
                Collection<SimpleGrantedAuthority> authorities = 
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role));
                
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                // Token无效
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

#### 🔹 JWT工具类
```java
// JWT令牌生成与解析
@Component
public class JwtUtil {
    
    private String secret = "mySecretKey";
    private int expiration = 3600000; // 1小时

    public String generateToken(Long userId, String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
```

### 4. 密码加密

#### 🔹 BCrypt密码编码
```java
// 密码编码器配置
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

// 密码加密与验证
public class UserServiceImpl {
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // 注册时加密密码
    public boolean register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userMapper.insert(user) > 0;
    }
    
    // 登录时验证密码
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

### 5. CORS跨域配置

#### 🔹 跨域资源共享配置
```java
// 项目中的CORS配置
@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));  // 允许所有源
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(Arrays.asList("*"));  // 允许所有请求头
    configuration.setAllowCredentials(true);  // 允许携带认证信息

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
```

---

## 🛠️ 项目中的实际应用

### 1. 完整的用户认证流程

```java
// 1. 用户注册
@PostMapping("/register")
public Result<String> register(@RequestBody User user) {
    // Spring Security: 密码加密
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    
    // MyBatis-Plus: 保存用户
    boolean success = userService.register(user);
    
    return success ? Result.success("注册成功") : Result.error("用户名已存在");
}

// 2. 用户登录
@PostMapping("/login")
public Result<Map<String, Object>> login(@RequestBody User loginUser, HttpServletRequest request) {
    // Spring Security: 密码验证 + JWT生成
    String token = userService.login(loginUser.getUsername(), loginUser.getPassword());
    
    if (token != null) {
        // MyBatis-Plus: 查询用户信息
        User user = userService.findByUsername(loginUser.getUsername());
        
        Map<String, Object> data = new HashMap<>();
        data.put("token", "Bearer " + token);
        data.put("role", user.getRole());
        data.put("username", user.getUsername());
        
        return Result.success(data);
    }
    
    return Result.error("用户名或密码错误");
}
```

### 2. 受保护的资源访问

```java
// 所有车辆相关接口都需要认证
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    // Spring Security会自动验证JWT Token
    // MyBatis-Plus执行分页查询
    @GetMapping("")
    public Result<IPage<Vehicle>> getVehicleList(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String keyword
    ) {
        IPage<Vehicle> vehiclePage = vehicleService.getVehiclePage(page, size, keyword);
        return Result.success(vehiclePage);
    }
}
```

### 3. 数据持久化与业务逻辑

```java
// Service层：业务逻辑处理
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;  // MyBatis-Plus Mapper

    @Override
    public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
        // MyBatis-Plus 分页插件
        Page<Vehicle> pageInfo = new Page<>(page, size);
        
        // MyBatis-Plus 条件构造器
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("license_plate", keyword)
                    .or().like("owner_name", keyword)
                    .or().like("brand", keyword);
        }
        queryWrapper.orderByDesc("create_time");

        // MyBatis-Plus 分页查询
        return vehicleMapper.selectPage(pageInfo, queryWrapper);
    }
}
```

---

## 🔧 技术难点与解决方案

### 1. JWT Token管理

**难点**：Token的生成、验证、刷新机制

**解决方案**：
```java
// 统一的JWT处理
public class JwtUtil {
    // Token生成：包含用户ID、用户名、角色信息
    // Token验证：解析并验证签名
    // Token刷新：重新生成新的Token（可选实现）
}

// 在过滤器中统一处理
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // 从请求头提取Token
    // 验证Token有效性
    // 设置Spring Security上下文
}
```

### 2. 数据库连接与事务管理

**难点**：数据库连接池配置、事务边界控制

**解决方案**：
```yaml
# 数据源配置优化
spring:
  datasource:
    type: com.zaxxer.hikari.HikariDataSource  # 使用HikariCP连接池
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

```java
// 事务管理
@Service
@Transactional  // 类级别事务
public class VehicleServiceImpl {
    
    @Transactional(rollbackFor = Exception.class)  // 方法级别事务
    public boolean createVehicleWithRecord(Vehicle vehicle, MaintenanceRecord record) {
        // 多表操作，保证数据一致性
    }
}
```

### 3. 安全防护

**难点**：SQL注入、XSS攻击、CSRF攻击防护

**解决方案**：
```java
// MyBatis-Plus自动防止SQL注入
QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
wrapper.like("license_plate", userInput);  // 参数化查询，防止SQL注入

// Spring Security CSRF防护
http.csrf().disable()  // API项目禁用CSRF（使用JWT）

// 输入验证
@PostMapping("/vehicles")
public Result<Vehicle> createVehicle(@Valid @RequestBody Vehicle vehicle) {
    // @Valid触发Bean Validation验证
}
```

### 4. 分页性能优化

**难点**：大数据量下的分页查询性能

**解决方案**：
```java
// 优化分页查询
@Override
public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
    Page<Vehicle> pageInfo = new Page<>(page, size);
    
    // 避免count查询（当不需要总数时）
    pageInfo.setSearchCount(false);
    
    // 添加索引优化的查询条件
    QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
    if (StringUtils.hasText(keyword)) {
        wrapper.and(w -> w
            .like("license_plate", keyword)  // license_plate字段应有索引
            .or().like("owner_name", keyword)
        );
    }
    
    return vehicleMapper.selectPage(pageInfo, wrapper);
}
```

---

## 📈 技术栈总结

### Spring Boot 核心知识点
1. **自动配置**：@SpringBootApplication、条件注解
2. **起步依赖**：starter机制简化依赖管理
3. **外部化配置**：application.yml、@Value、Environment
4. **Web开发**：@RestController、@RequestMapping、参数绑定
5. **依赖注入**：@Autowired、@Service、@Component
6. **异常处理**：@ControllerAdvice、统一异常处理

### MyBatis-Plus 核心知识点
1. **BaseMapper**：继承获得基础CRUD方法
2. **实体注解**：@TableName、@TableId、@TableField
3. **条件构造器**：QueryWrapper、UpdateWrapper
4. **分页插件**：PaginationInnerInterceptor
5. **自动填充**：MetaObjectHandler、FieldFill
6. **代码生成器**：快速生成实体、Mapper、Service

### Spring Security 核心知识点
1. **安全配置**：SecurityFilterChain、@EnableWebSecurity
2. **认证机制**：JWT、UsernamePasswordAuthenticationToken
3. **授权控制**：hasRole、权限表达式
4. **密码加密**：BCryptPasswordEncoder
5. **CORS配置**：跨域资源共享
6. **过滤器链**：自定义认证过滤器

### 项目集成特色
1. **前后端分离**：RESTful API + JWT认证
2. **统一响应格式**：Result封装类
3. **分层架构**：Controller → Service → Mapper
4. **自动化配置**：最小配置实现最大功能
5. **安全防护**：多层次安全保障机制

---

*💡 **学习建议**：从基础的CRUD操作开始，逐步掌握高级特性如分页、条件查询、事务管理等，最后深入理解安全机制和性能优化。* 