# 🧪 服务测试说明

## 测试前准备
1. 确保MySQL服务已启动
2. 确保数据库`vehicle_maintenance`已创建
3. 确保已导入数据库表结构

## 1. 后端服务测试

### 启动后端
```bash
cd backend/vehicles
mvn spring-boot:run
```

### 测试后端API
当后端启动成功后（通常显示 "Started VehiclesApplication"），可以进行以下测试：

#### 1.1 测试用户注册API
```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456",
    "email": "test@example.com",
    "phone": "13800138000"
  }'
```

#### 1.2 测试用户登录API
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

成功登录后会返回JWT token，形如：
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "username": "testuser",
    "role": "USER",
    "userId": 1
  }
}
```

#### 1.3 测试车辆API（需要token）
```bash
# 获取车辆列表
curl -X GET "http://localhost:8081/api/vehicles?page=1&size=10" \
  -H "Authorization: Bearer [YOUR_TOKEN_HERE]"

# 添加车辆
curl -X POST http://localhost:8081/api/vehicles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer [YOUR_TOKEN_HERE]" \
  -d '{
    "licensePlate": "京A12345",
    "vin": "WVWBB71K59W000001",
    "brand": "大众",
    "model": "帕萨特",
    "ownerName": "张三",
    "ownerPhone": "13800138000"
  }'
```

## 2. 前端服务测试

### 启动前端
```bash
cd frontend
npm run dev
```

### 访问前端应用
前端启动成功后，在浏览器中访问：http://localhost:3000

### 测试流程
1. **登录测试**
   - 访问 http://localhost:3000
   - 自动跳转到登录页面
   - 使用预填的测试账号登录：
     - 用户名：`testuser`
     - 密码：`123456`

2. **仪表盘测试**
   - 登录成功后查看仪表盘页面
   - 检查统计数据是否正常显示
   - 测试快捷操作按钮

3. **车辆管理测试**
   - 点击左侧菜单"车辆管理"
   - 测试添加车辆功能
   - 测试车辆列表显示
   - 测试搜索功能
   - 测试编辑和删除功能

4. **操作日志测试**
   - 点击左侧菜单"操作日志"
   - 查看系统操作记录
   - 测试日志搜索功能

## 3. 端到端测试场景

### 场景1：新用户注册和首次使用
1. 在登录页面点击"立即注册"
2. 填写注册信息并提交
3. 注册成功后使用新账号登录
4. 添加第一辆车辆
5. 查看操作日志中的记录

### 场景2：车辆管理完整流程
1. 登录系统
2. 进入车辆管理页面
3. 添加多辆车辆
4. 使用搜索功能查找特定车辆
5. 编辑车辆信息
6. 删除不需要的车辆记录
7. 查看操作日志确认所有操作已记录

### 场景3：数据统计验证
1. 添加若干车辆记录
2. 进行多次操作（增删改查）
3. 在仪表盘查看统计数据
4. 点击刷新数据按钮验证数据更新

## 4. 错误处理测试

### 4.1 网络错误测试
- 关闭后端服务，测试前端错误提示
- 检查网络请求失败时的用户体验

### 4.2 权限测试
- 删除localStorage中的token，测试未登录访问
- 测试token过期后的自动跳转

### 4.3 表单验证测试
- 提交空表单测试验证规则
- 输入格式错误的数据测试验证

## 5. 性能测试

### 5.1 大数据量测试
- 添加大量车辆记录（建议100+条）
- 测试分页功能性能
- 测试搜索功能响应速度

### 5.2 并发测试
- 多个浏览器标签页同时操作
- 测试数据一致性

## 6. 浏览器兼容性测试
- Chrome（推荐）
- Firefox
- Edge
- Safari（如有Mac环境）

## 预期结果
- 所有API请求返回正确的JSON格式响应
- 前端页面正常渲染，无控制台错误
- 用户操作流畅，体验良好
- 数据一致性保证，操作日志完整记录

## 常见问题排查
1. **后端启动失败**：检查MySQL连接、端口占用
2. **前端白屏**：检查控制台错误、网络请求
3. **登录失败**：确认用户已注册、密码正确
4. **数据不显示**：检查API响应、权限设置

---
**测试建议**：按照上述流程逐步测试，发现问题及时记录并修复。 