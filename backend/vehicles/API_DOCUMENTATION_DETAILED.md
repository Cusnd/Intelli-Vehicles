# 智能车辆维保记录管理系统 - 详细API接口文档

## 🌐 API 基础信息

- **服务地址**: `http://localhost:8081`
- **接口格式**: RESTful API
- **数据格式**: JSON (application/json)
- **字符编码**: UTF-8
- **认证方式**: JWT Bearer Token
- **Token有效期**: 24小时

## 📋 目录

1. [通用说明](#通用说明)
2. [认证模块](#认证模块) (4个接口)
3. [车辆管理模块](#车辆管理模块) (5个接口)
4. [维保记录模块](#维保记录模块) (6个接口)
5. [数据统计模块](#数据统计模块) (5个接口)
6. [操作日志模块](#操作日志模块) (3个接口)
7. [测试接口](#测试接口) (1个接口)
8. [测试工具使用](#测试工具使用)
9. [常见问题](#常见问题)

---

## 🔧 通用说明

### 请求头 (Headers)

#### 基础请求头
```http
Content-Type: application/json
Accept: application/json
```

#### 需要认证的接口
```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer {JWT_TOKEN}
```

### 通用响应格式

#### 成功响应
```json
{
  "code": 200,
  "message": "success",
  "data": {具体数据}
}
```

#### 错误响应
```json
{
  "code": 400,
  "message": "具体错误信息",
  "data": null
}
```

### 状态码说明
- **200**: 操作成功
- **400**: 请求参数错误
- **401**: 未授权 (Token无效或过期)
- **403**: 权限不足
- **404**: 资源不存在
- **500**: 服务器内部错误

---

## 🔐 认证模块

### 1. 用户注册

#### 基本信息
- **接口地址**: `POST /api/auth/register`
- **认证要求**: 无需认证
- **权限级别**: public

#### 请求示例
```http
POST /api/auth/register HTTP/1.1
Host: localhost:8081
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456",
  "email": "test@example.com",
  "phone": "13800138000"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 | 约束 |
|--------|------|------|------|------|
| username | String | 是 | 用户名 | 3-50字符，字母数字下划线 |
| password | String | 是 | 密码 | 6-20位 |
| email | String | 否 | 邮箱 | 标准邮箱格式 |
| phone | String | 否 | 电话 | 11位手机号 |

#### 响应示例
```json
{
  "code": 200,
  "message": "注册成功",
  "data": null
}
```

#### curl测试命令
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

#### Postman测试步骤
1. 创建新请求，选择 `POST` 方法
2. 输入URL: `http://localhost:8081/api/auth/register`
3. Headers设置:
   - `Content-Type`: `application/json`
4. Body选择 `raw` -> `JSON`，输入请求参数
5. 点击 `Send` 发送请求

---

### 2. 用户登录

#### 基本信息
- **接口地址**: `POST /api/auth/login`
- **认证要求**: 无需认证
- **权限级别**: public

#### 请求示例
```http
POST /api/auth/login HTTP/1.1
Host: localhost:8081
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | String | 是 | 用户名 |
| password | String | 是 | 密码 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNzA2NzY4NDAwLCJleHAiOjE3MDY4NTQ4MDB9.abc123",
    "role": "user",
    "username": "testuser",
    "userId": 1
  }
}
```

#### curl测试命令
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "123456"
  }'
```

#### 重要说明
- **Token使用**: 登录成功后保存 `data.token` 字段值
- **Token格式**: 已包含 "Bearer " 前缀，直接使用
- **权限识别**: 通过 `data.role` 区分用户权限

---

### 3. 获取用户信息

#### 基本信息
- **接口地址**: `GET /api/auth/profile`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/auth/profile?userId=1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "username": "testuser",
    "email": "test@example.com",
    "phone": "13800138000",
    "role": "user",
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00"
  }
}
```

#### curl测试命令
```bash
# 注意：替换 {TOKEN} 为实际的JWT Token
curl -X GET "http://localhost:8081/api/auth/profile?userId=1" \
  -H "Authorization: Bearer {TOKEN}"
```

---

### 4. 更新用户信息

#### 基本信息
- **接口地址**: `PUT /api/auth/profile`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
PUT /api/auth/profile HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "id": 1,
  "email": "newemail@example.com",
  "phone": "13900139000"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 用户ID |
| email | String | 否 | 新邮箱 |
| phone | String | 否 | 新电话 |

---

## 🚗 车辆管理模块

### 1. 分页查询车辆列表

#### 基本信息
- **接口地址**: `GET /api/vehicles`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/vehicles?page=1&size=10&keyword=京A HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
| size | int | 否 | 10 | 每页数量 |
| keyword | String | 否 | - | 搜索关键词(车牌号/品牌/型号) |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "licensePlate": "京A12345",
        "vin": "LHGCM82633A004352",
        "brand": "本田",
        "model": "雅阁",
        "ownerName": "张三",
        "ownerPhone": "13800138000",
        "userId": 1,
        "createTime": "2024-01-01T10:00:00",
        "updateTime": "2024-01-01T10:00:00"
      }
    ],
    "total": 25,
    "size": 10,
    "current": 1,
    "pages": 3
  }
}
```

#### curl测试命令
```bash
curl -X GET "http://localhost:8081/api/vehicles?page=1&size=10&keyword=京A" \
  -H "Authorization: Bearer {TOKEN}"
```

---

### 2. 查询车辆详情

#### 基本信息
- **接口地址**: `GET /api/vehicles/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/vehicles/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Path参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | Long | 是 | 车辆ID |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "licensePlate": "京A12345",
    "vin": "LHGCM82633A004352",
    "brand": "本田",
    "model": "雅阁",
    "ownerName": "张三",
    "ownerPhone": "13800138000",
    "userId": 1,
    "createTime": "2024-01-01T10:00:00",
    "updateTime": "2024-01-01T10:00:00"
  }
}
```

---

### 3. 添加车辆

#### 基本信息
- **接口地址**: `POST /api/vehicles`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
POST /api/vehicles HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "licensePlate": "京A12345",
  "vin": "LHGCM82633A004352",
  "brand": "本田",
  "model": "雅阁",
  "ownerName": "张三",
  "ownerPhone": "13800138000"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 | 约束 |
|--------|------|------|------|------|
| licensePlate | String | 是 | 车牌号 | 唯一，7-8位字符 |
| vin | String | 是 | 车架号 | 唯一，17位字符 |
| brand | String | 否 | 品牌 | 最大50字符 |
| model | String | 否 | 型号 | 最大50字符 |
| ownerName | String | 否 | 车主姓名 | 最大50字符 |
| ownerPhone | String | 否 | 车主电话 | 11位手机号 |

#### 车牌号格式示例
- 普通车牌: `京A12345`、`沪B67890`
- 新能源车牌: `京AD12345`、`沪BF67890`

#### VIN码格式要求
- 长度: 17位字符
- 字符: 大写字母和数字
- 不包含: I、O、Q

#### curl测试命令
```bash
curl -X POST http://localhost:8081/api/vehicles \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "licensePlate": "京A12345",
    "vin": "LHGCM82633A004352",
    "brand": "本田",
    "model": "雅阁",
    "ownerName": "张三",
    "ownerPhone": "13800138000"
  }'
```

---

### 4. 更新车辆

#### 基本信息
- **接口地址**: `PUT /api/vehicles/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
PUT /api/vehicles/1 HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "id": 1,
  "licensePlate": "京A12345",
  "vin": "LHGCM82633A004352",
  "brand": "本田",
  "model": "雅阁",
  "ownerName": "李四",
  "ownerPhone": "13900139000"
}
```

---

### 5. 删除车辆

#### 基本信息
- **接口地址**: `DELETE /api/vehicles/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
DELETE /api/vehicles/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 注意事项
- 删除车辆会级联删除所有相关的维保记录
- 删除操作不可恢复，请谨慎操作

---

## 🔧 维保记录模块

### 1. 分页查询维保记录

#### 基本信息
- **接口地址**: `GET /api/records`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/records?page=1&size=10&vehicleId=1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
| size | int | 否 | 10 | 每页数量 |
| vehicleId | Long | 否 | - | 车辆ID过滤 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 15,
    "list": [
      {
        "id": 1,
        "vehicleId": 1,
        "serviceDate": "2024-01-15",
        "cost": 850.00,
        "servicePerson": "李师傅",
        "remarks": "常规保养",
        "createTime": "2024-01-15T09:00:00",
        "updateTime": "2024-01-15T09:00:00"
      }
    ],
    "current": 1,
    "size": 10
  }
}
```

---

### 2. 查询维保记录详情

#### 基本信息
- **接口地址**: `GET /api/records/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/records/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 响应示例 (包含维保项目明细)
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "vehicleId": 1,
    "serviceDate": "2024-01-15",
    "cost": 850.00,
    "servicePerson": "李师傅",
    "remarks": "常规保养，更换了机油和机滤",
    "createTime": "2024-01-15T09:00:00",
    "updateTime": "2024-01-15T09:00:00",
    "items": [
      {
        "id": 1,
        "recordId": 1,
        "itemName": "更换机油",
        "itemCost": 300.00
      },
      {
        "id": 2,
        "recordId": 1,
        "itemName": "更换机滤",
        "itemCost": 50.00
      },
      {
        "id": 3,
        "recordId": 1,
        "itemName": "四轮定位",
        "itemCost": 500.00
      }
    ]
  }
}
```

---

### 3. 添加维保记录

#### 基本信息
- **接口地址**: `POST /api/records`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
POST /api/records HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "vehicleId": 1,
  "serviceDate": "2024-01-15",
  "servicePerson": "李师傅",
  "remarks": "常规保养",
  "items": [
    {
      "itemName": "更换机油",
      "itemCost": 300.00
    },
    {
      "itemName": "更换机滤",
      "itemCost": 50.00
    },
    {
      "itemName": "四轮定位",
      "itemCost": 500.00
    }
  ]
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 | 约束 |
|--------|------|------|------|------|
| vehicleId | Long | 是 | 车辆ID | 必须存在 |
| serviceDate | String | 是 | 维保日期 | YYYY-MM-DD格式 |
| servicePerson | String | 否 | 维保人员 | 最大50字符 |
| remarks | String | 否 | 备注 | 最大65535字符 |
| items | Array | 否 | 维保项目列表 | 可为空数组 |
| items[].itemName | String | 是 | 项目名称 | 最大100字符 |
| items[].itemCost | BigDecimal | 否 | 项目费用 | 0-99999999.99 |

#### 维保项目名称示例
- **常规保养**: 更换机油、更换机滤、更换空滤、更换汽滤
- **检查项目**: 轮胎检查、刹车检查、电池检查、灯光检查
- **维修项目**: 更换刹车片、更换火花塞、更换皮带、修复漆面
- **其他服务**: 四轮定位、轮胎平衡、空调清洗、内饰清洁

#### curl测试命令
```bash
curl -X POST http://localhost:8081/api/records \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "vehicleId": 1,
    "serviceDate": "2024-01-15",
    "servicePerson": "李师傅",
    "remarks": "常规保养",
    "items": [
      {
        "itemName": "更换机油",
        "itemCost": 300.00
      },
      {
        "itemName": "更换机滤", 
        "itemCost": 50.00
      }
    ]
  }'
```

#### 注意事项
- 总费用会自动计算为所有项目费用之和
- 如果不提供项目费用，总费用为0
- 服务日期不能晚于当前日期

---

### 4. 更新维保记录

#### 基本信息
- **接口地址**: `PUT /api/records/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
PUT /api/records/1 HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "id": 1,
  "vehicleId": 1,
  "serviceDate": "2024-01-15",
  "servicePerson": "王师傅",
  "remarks": "保养完成，状态良好",
  "items": [
    {
      "id": 1,
      "itemName": "更换机油",
      "itemCost": 350.00
    },
    {
      "itemName": "更换空滤",
      "itemCost": 80.00
    }
  ]
}
```

#### 更新逻辑说明
- 会删除原有的所有维保项目
- 重新创建新的维保项目
- 重新计算总费用

---

### 5. 删除维保记录

#### 基本信息
- **接口地址**: `DELETE /api/records/{id}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
DELETE /api/records/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 注意事项
- 删除记录会级联删除所有相关的维保项目
- 删除操作不可恢复

---

### 6. 根据车辆ID查询维保记录

#### 基本信息
- **接口地址**: `GET /api/records/vehicle/{vehicleId}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/records/vehicle/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "vehicleId": 1,
      "serviceDate": "2024-01-15",
      "cost": 850.00,
      "servicePerson": "李师傅",
      "remarks": "常规保养"
    },
    {
      "id": 2,
      "vehicleId": 1,
      "serviceDate": "2024-02-20",
      "cost": 1200.00,
      "servicePerson": "王师傅",
      "remarks": "更换刹车片"
    }
  ]
}
```

---

## 📊 数据统计模块

### 1. 获取月度费用趋势

#### 基本信息
- **接口地址**: `GET /api/statistics/monthly-cost`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/statistics/monthly-cost?vehicleId=1&year=2024 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| vehicleId | Long | 否 | - | 车辆ID，不填查询所有车辆 |
| year | Integer | 否 | 当前年 | 查询年份 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "xAxis": ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
    "series": [1200.00, 800.00, 1500.00, 0.00, 600.00, 900.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00],
    "year": 2024,
    "total": 5000.00
  }
}
```

#### curl测试命令
```bash
# 查询特定车辆的月度费用
curl -X GET "http://localhost:8081/api/statistics/monthly-cost?vehicleId=1&year=2024" \
  -H "Authorization: Bearer {TOKEN}"

# 查询所有车辆的月度费用
curl -X GET "http://localhost:8081/api/statistics/monthly-cost?year=2024" \
  -H "Authorization: Bearer {TOKEN}"
```

---

### 2. 获取热门服务项目

#### 基本信息
- **接口地址**: `GET /api/statistics/popular-items`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/statistics/popular-items?limit=10 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| limit | int | 否 | 10 | 返回数量限制 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "itemName": "更换机油",
      "count": 45,
      "totalCost": 13500.00,
      "avgCost": 300.00
    },
    {
      "itemName": "轮胎检查",
      "count": 32,
      "totalCost": 6400.00,
      "avgCost": 200.00
    },
    {
      "itemName": "更换刹车片",
      "count": 28,
      "totalCost": 11200.00,
      "avgCost": 400.00
    }
  ]
}
```

---

### 3. 获取车辆维保概览

#### 基本信息
- **接口地址**: `GET /api/statistics/vehicle-summary/{vehicleId}`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/statistics/vehicle-summary/1 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "vehicle": {
      "id": 1,
      "licensePlate": "京A12345",
      "brand": "本田",
      "model": "雅阁",
      "ownerName": "张三"
    },
    "totalRecords": 12,
    "totalCost": 15600.00,
    "lastServiceDate": "2024-01-15",
    "avgCostPerService": 1300.00,
    "thisYearRecords": 8,
    "thisYearCost": 10400.00
  }
}
```

---

### 4. 获取系统概览

#### 基本信息
- **接口地址**: `GET /api/statistics/overview`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/statistics/overview HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "totalVehicles": 25,
    "totalRecords": 156,
    "totalCost": 125600.00,
    "yearTotalCost": 89600.00,
    "monthRecords": 12,
    "monthCost": 8600.00,
    "avgCostPerVehicle": 5024.00,
    "avgCostPerRecord": 805.13
  }
}
```

---

### 5. 获取最近维保记录

#### 基本信息
- **接口地址**: `GET /api/statistics/recent-records`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
GET /api/statistics/recent-records?limit=5 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| limit | int | 否 | 5 | 返回数量限制 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 15,
      "vehicleId": 3,
      "licensePlate": "京B67890",
      "serviceDate": "2024-01-20",
      "cost": 1200.00,
      "servicePerson": "赵师傅",
      "remarks": "更换轮胎"
    },
    {
      "id": 14,
      "vehicleId": 1,
      "licensePlate": "京A12345",
      "serviceDate": "2024-01-15",
      "cost": 850.00,
      "servicePerson": "李师傅",
      "remarks": "常规保养"
    }
  ]
}
```

---

## 📋 操作日志模块

### 1. 分页查询操作日志

#### 基本信息
- **接口地址**: `GET /api/logs`
- **认证要求**: 需要Token
- **权限级别**: admin

#### 请求示例
```http
GET /api/logs?page=1&size=20&userId=1&operation=添加车辆 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| page | int | 否 | 1 | 页码 |
| size | int | 否 | 20 | 每页数量 |
| userId | Long | 否 | - | 用户ID过滤 |
| operation | String | 否 | - | 操作类型过滤 |

#### 响应示例
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "records": [
      {
        "id": 1,
        "userId": 1,
        "operation": "添加车辆",
        "ipAddress": "192.168.1.100",
        "createTime": "2024-01-15T10:30:00"
      },
      {
        "id": 2,
        "userId": 1,
        "operation": "用户登录",
        "ipAddress": "192.168.1.100",
        "createTime": "2024-01-15T09:00:00"
      }
    ],
    "total": 50,
    "size": 20,
    "current": 1,
    "pages": 3
  }
}
```

#### 操作类型示例
- **用户操作**: 用户登录、用户注册、修改密码、更新资料
- **车辆操作**: 添加车辆、更新车辆、删除车辆、查询车辆
- **维保操作**: 添加维保记录、更新维保记录、删除维保记录
- **系统操作**: 导出报表、数据备份、系统维护

---

### 2. 记录操作日志

#### 基本信息
- **接口地址**: `GET /api/logs`
- **认证要求**: 需要Token
- **权限级别**: user

#### 请求示例
```http
POST /api/logs HTTP/1.1
Host: localhost:8081
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

{
  "userId": 1,
  "operation": "添加车辆",
  "ipAddress": "192.168.1.100"
}
```

#### 请求参数说明
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| userId | Long | 是 | 用户ID |
| operation | String | 是 | 操作描述 |
| ipAddress | String | 否 | 客户端IP地址 |

---

### 3. 清理过期日志

#### 基本信息
- **接口地址**: `DELETE /api/logs/cleanup`
- **认证要求**: 需要Token
- **权限级别**: admin

#### 请求示例
```http
DELETE /api/logs/cleanup?daysAgo=30 HTTP/1.1
Host: localhost:8081
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

#### Query参数说明
| 参数名 | 类型 | 必填 | 默认值 | 说明 |
|--------|------|------|--------|------|
| daysAgo | int | 否 | 30 | 清理多少天前的日志 |

---

## ❗ 常见问题

### 认证相关

#### Q: Token过期怎么办？
A: JWT Token有效期为24小时，过期后需要重新登录获取新的Token。

#### Q: 如何判断Token是否有效？
A: 调用需要认证的接口，如果返回401状态码，说明Token无效或过期。

#### Q: 权限不足怎么办？
A: 确认用户角色是否符合接口要求，admin权限高于user权限。

### 请求相关

#### Q: 请求失败怎么排查？
A: 
1. 检查请求URL是否正确
2. 检查请求方法 (GET/POST/PUT/DELETE)
3. 检查请求头 Content-Type 和 Authorization
4. 检查请求参数格式和必填字段
5. 查看错误响应中的message字段

#### Q: 中文乱码怎么解决？
A: 确保请求头设置了正确的字符编码:
```http
Content-Type: application/json; charset=utf-8
```

### 数据相关

#### Q: 车牌号重复怎么办？
A: 车牌号必须唯一，如果重复会返回错误信息。

#### Q: VIN码格式不正确？
A: VIN码必须是17位字符，包含大写字母和数字，不能包含I、O、Q。

#### Q: 维保费用计算不正确？
A: 维保记录的总费用会自动计算为所有维保项目费用之和。
