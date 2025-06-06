# 🗃️ BaseMapper详细使用说明

> 📚 **BaseMapper是什么？**
> 
> BaseMapper是MyBatis-Plus提供的基础Mapper接口，它就像一个**万能钥匙**，为我们自动提供了常用的数据库操作方法，不需要写SQL语句！

## 📖 目录
1. [BaseMapper基础概念](#basemapper基础概念)
2. [项目中的实际使用](#项目中的实际使用)
3. [BaseMapper提供的方法详解](#basemapper提供的方法详解)
4. [实战示例](#实战示例)
5. [高级用法](#高级用法)

---

## 🤔 BaseMapper基础概念

### 类比理解
```
🏪 把BaseMapper比作超市的自助收银机：

传统方式：需要人工一个个扫码、计算价格、找零钱
BaseMapper：自动扫码、自动计算、自动支付

传统写SQL：SELECT * FROM vehicle WHERE id = ?
BaseMapper：vehicleMapper.selectById(id) 
```

### 在项目中的声明
```java
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // 继承BaseMapper后，自动拥有所有基础CRUD方法
    // 不需要写任何代码！
}
```

---

## 🛠️ 项目中的实际使用

### 1. VehicleMapper的定义
```java
package com.zephyrover.vehicles.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zephyrover.vehicles.entity.Vehicle;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // 空接口！但是继承了BaseMapper<Vehicle>
    // 这意味着它自动拥有了针对Vehicle表的所有基础操作
}
```

### 2. 在Service中的使用
```java
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;  // 注入Mapper

    // 使用BaseMapper的selectById方法
    @Override
    public Vehicle getById(Long id) {
        return vehicleMapper.selectById(id);  // 自动生成：SELECT * FROM vehicle WHERE id = ?
    }

    // 使用BaseMapper的insert方法
    @Override
    public boolean save(Vehicle vehicle) {
        return vehicleMapper.insert(vehicle) > 0;  // 自动生成INSERT语句
    }

    // 使用BaseMapper的updateById方法
    @Override
    public boolean updateById(Vehicle vehicle) {
        return vehicleMapper.updateById(vehicle) > 0;  // 自动生成UPDATE语句
    }

    // 使用BaseMapper的deleteById方法
    @Override
    public boolean removeById(Long id) {
        return vehicleMapper.deleteById(id) > 0;  // 自动生成DELETE语句
    }

    // 使用BaseMapper的selectPage方法（分页查询）
    @Override
    public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
        Page<Vehicle> pageInfo = new Page<>(page, size);
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("license_plate", keyword)
                    .or()
                    .like("owner_name", keyword);
        }

        return vehicleMapper.selectPage(pageInfo, queryWrapper);  // 自动生成分页SQL
    }
}
```

---

## 📋 BaseMapper提供的方法详解

### 🔍 查询方法（SELECT）

#### 1. selectById - 根据ID查询
```java
// 方法签名
T selectById(Serializable id)

// 项目中使用
Vehicle vehicle = vehicleMapper.selectById(1L);

// 生成的SQL
SELECT id, license_plate, brand, model, owner_name, owner_phone, create_time 
FROM vehicle WHERE id = 1
```

#### 2. selectList - 条件查询
```java
// 方法签名
List<T> selectList(Wrapper<T> queryWrapper)

// 项目中使用
QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
wrapper.eq("brand", "丰田");  // 查询品牌为丰田的车辆
List<Vehicle> vehicles = vehicleMapper.selectList(wrapper);

// 生成的SQL
SELECT * FROM vehicle WHERE brand = '丰田'
```

#### 3. selectPage - 分页查询
```java
// 方法签名  
IPage<T> selectPage(IPage<T> page, Wrapper<T> queryWrapper)

// 项目中使用
Page<Vehicle> page = new Page<>(1, 10);  // 第1页，每页10条
QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
wrapper.like("license_plate", "京");  // 车牌号包含"京"
IPage<Vehicle> result = vehicleMapper.selectPage(page, wrapper);

// 生成的SQL
SELECT * FROM vehicle WHERE license_plate LIKE '%京%' LIMIT 0, 10
```

### 📝 新增方法（INSERT）

#### insert - 插入数据
```java
// 方法签名
int insert(T entity)

// 项目中使用
Vehicle vehicle = new Vehicle();
vehicle.setLicensePlate("京A12345");
vehicle.setBrand("丰田");
vehicle.setModel("卡罗拉");
vehicle.setOwnerName("张三");
vehicle.setOwnerPhone("13800138000");

int result = vehicleMapper.insert(vehicle);  // 返回影响行数
// result = 1 表示插入成功

// 生成的SQL
INSERT INTO vehicle (license_plate, brand, model, owner_name, owner_phone, create_time) 
VALUES ('京A12345', '丰田', '卡罗拉', '张三', '13800138000', NOW())
```

### 🔄 更新方法（UPDATE）

#### 1. updateById - 根据ID更新
```java
// 方法签名
int updateById(T entity)

// 项目中使用
Vehicle vehicle = new Vehicle();
vehicle.setId(1L);
vehicle.setBrand("本田");  // 只更新品牌
vehicle.setModel("雅阁");  // 只更新型号

int result = vehicleMapper.updateById(vehicle);

// 生成的SQL（只更新非null字段）
UPDATE vehicle SET brand = '本田', model = '雅阁' WHERE id = 1
```

#### 2. update - 条件更新
```java
// 方法签名
int update(T entity, Wrapper<T> updateWrapper)

// 项目中使用
Vehicle vehicle = new Vehicle();
vehicle.setBrand("大众");

UpdateWrapper<Vehicle> wrapper = new UpdateWrapper<>();
wrapper.eq("owner_name", "张三");  // 更新车主是张三的所有车辆

int result = vehicleMapper.update(vehicle, wrapper);

// 生成的SQL
UPDATE vehicle SET brand = '大众' WHERE owner_name = '张三'
```

### 🗑️ 删除方法（DELETE）

#### 1. deleteById - 根据ID删除
```java
// 方法签名
int deleteById(Serializable id)

// 项目中使用
int result = vehicleMapper.deleteById(1L);

// 生成的SQL
DELETE FROM vehicle WHERE id = 1
```

#### 2. delete - 条件删除
```java
// 方法签名
int delete(Wrapper<T> wrapper)

// 项目中使用
QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
wrapper.eq("brand", "报废车");
int result = vehicleMapper.delete(wrapper);

// 生成的SQL
DELETE FROM vehicle WHERE brand = '报废车'
```

---

## 💻 实战示例

### 场景1：用户注册（插入新用户）
```java
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserMapper userMapper;

    public User register(User user) {
        // 1. 检查用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername());
        User existingUser = userMapper.selectOne(wrapper);
        
        if (existingUser != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 2. 密码加密
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreateTime(LocalDateTime.now());
        user.setRole("USER");

        // 3. 插入新用户
        userMapper.insert(user);  // BaseMapper的insert方法
        return user;
    }
}
```

### 场景2：车辆列表查询（分页+条件查询）
```java
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    public IPage<Vehicle> getVehiclePage(int page, int size, String keyword) {
        // 1. 创建分页对象
        Page<Vehicle> pageInfo = new Page<>(page, size);
        
        // 2. 构建查询条件
        QueryWrapper<Vehicle> queryWrapper = new QueryWrapper<>();
        
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like("license_plate", keyword)  // 车牌号模糊查询
                    .or()
                    .like("owner_name", keyword)         // 或者车主姓名模糊查询
                    .or()
                    .like("brand", keyword);             // 或者品牌模糊查询
        }

        // 3. 按创建时间降序排列
        queryWrapper.orderByDesc("create_time");

        // 4. 执行分页查询
        return vehicleMapper.selectPage(pageInfo, queryWrapper);  // BaseMapper的selectPage方法
    }
}
```

### 场景3：车辆信息更新
```java
@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleMapper vehicleMapper;

    public Vehicle updateVehicle(Long id, Vehicle vehicle) {
        // 1. 检查车辆是否存在
        Vehicle existingVehicle = vehicleMapper.selectById(id);  // BaseMapper的selectById方法
        if (existingVehicle == null) {
            throw new RuntimeException("车辆不存在");
        }

        // 2. 更新车辆信息
        vehicle.setId(id);
        vehicle.setUpdateTime(LocalDateTime.now());
        
        int result = vehicleMapper.updateById(vehicle);  // BaseMapper的updateById方法
        
        if (result > 0) {
            return vehicleMapper.selectById(id);  // 返回更新后的车辆信息
        } else {
            throw new RuntimeException("更新失败");
        }
    }
}
```

---

## 🚀 高级用法

### 1. 自定义查询方法
虽然BaseMapper提供了基础方法，但有时需要复杂查询：

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {

    // 自定义方法：根据用户名查询（在项目中实际使用）
    User findByUsername(String username);
    
    // 如果需要，可以添加更多自定义方法
    @Select("SELECT * FROM user WHERE role = #{role} AND create_time > #{date}")
    List<User> findUsersByRoleAndDate(@Param("role") String role, @Param("date") LocalDateTime date);
}
```

### 2. 条件构造器的高级用法
```java
public List<Vehicle> complexQuery() {
    QueryWrapper<Vehicle> wrapper = new QueryWrapper<>();
    
    // 复杂条件：(品牌='丰田' OR 品牌='本田') AND 车主姓名不为空 AND 创建时间在最近30天
    wrapper.and(w -> w.eq("brand", "丰田").or().eq("brand", "本田"))
           .isNotNull("owner_name")
           .ge("create_time", LocalDateTime.now().minusDays(30))
           .orderByDesc("create_time");
    
    return vehicleMapper.selectList(wrapper);
}
```

### 3. 批量操作
```java
public boolean batchInsert(List<Vehicle> vehicles) {
    // MyBatis-Plus提供的批量插入
    return saveBatch(vehicles);  // 这是ServiceImpl提供的方法
}

public boolean batchDelete(List<Long> ids) {
    return vehicleMapper.deleteBatchIds(ids) > 0;  // BaseMapper的批量删除方法
}
```

---

## ✨ BaseMapper的优势

### 🎯 对比传统方式

#### 传统MyBatis写法：
```xml
<!-- 需要写XML映射文件 -->
<select id="selectById" resultType="Vehicle">
    SELECT * FROM vehicle WHERE id = #{id}
</select>

<insert id="insert">
    INSERT INTO vehicle (license_plate, brand, model, owner_name, owner_phone, create_time)
    VALUES (#{licensePlate}, #{brand}, #{model}, #{ownerName}, #{ownerPhone}, #{createTime})
</insert>
```

```java
// 还需要在Mapper接口中声明方法
public interface VehicleMapper {
    Vehicle selectById(Long id);
    int insert(Vehicle vehicle);
    // ... 更多方法
}
```

#### BaseMapper写法：
```java
// 只需要继承BaseMapper，什么都不用写！
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // 自动拥有所有基础CRUD方法
}
```

### 📈 优势总结
1. **代码简洁**：不需要写SQL和XML
2. **类型安全**：编译期检查，减少错误
3. **自动生成**：根据实体类自动生成SQL
4. **功能强大**：支持分页、条件查询、批量操作
5. **易于维护**：修改实体类，SQL自动更新

---

## 🤔 常见问题解答

### Q1: BaseMapper和传统SQL哪个更好？
```
🎯 选择建议：
- 简单CRUD操作：用BaseMapper（95%的场景）
- 复杂业务查询：写自定义SQL（5%的场景）
- 性能要求极高：优化后的自定义SQL
```

### Q2: 如何同时使用BaseMapper和自定义SQL？
```java
@Mapper
public interface VehicleMapper extends BaseMapper<Vehicle> {
    // 继承BaseMapper的所有方法
    
    // 同时可以添加自定义方法
    @Select("SELECT * FROM vehicle WHERE DATE(create_time) = CURDATE()")
    List<Vehicle> getTodayVehicles();
    
    // 复杂统计查询
    @Select("SELECT brand, COUNT(*) as count FROM vehicle GROUP BY brand")
    List<BrandStatistics> getBrandStatistics();
}
```

### Q3: BaseMapper生成的SQL性能如何？
```
✅ 优势：
- SQL结构标准，数据库优化器友好
- 自动处理字段映射，减少错误
- 支持动态SQL，只更新非null字段

⚠️ 注意：
- 复杂查询可能不如手写SQL高效
- 大批量操作建议使用专门的批量方法
```

---

## 📝 总结

BaseMapper就像是一个**智能助手**，为我们提供了：

1. **开箱即用**的基础CRUD操作
2. **类型安全**的方法调用
3. **灵活强大**的条件构造器
4. **高效便捷**的分页查询

在我们的车辆管理项目中，99%的数据库操作都可以通过BaseMapper完成，只有特殊需求才需要自定义SQL。

**记住**：掌握BaseMapper的使用，就掌握了Spring Boot项目中数据访问层的核心技能！

---

*💡 **学习建议**：先熟练使用BaseMapper的基础方法，再学习条件构造器的高级用法，最后掌握自定义SQL的场景。* 