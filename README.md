# 智能车辆维保记录管理系统 (Intelli-Vehicles)

## 📋 项目简介

智能车辆维保记录管理系统是一个全栈Web应用，用于管理车辆信息和维保记录。系统采用前后端分离架构，提供了完整的车辆管理、维保记录管理、数据统计和操作日志等功能。

## 🏗️ 技术架构

### 后端技术栈
- **Java 17+** - 编程语言
- **Spring Boot 3.x** - 主框架
- **Spring Security** - 安全框架
- **MyBatis-Plus** - ORM框架
- **MySQL 8.0+** - 数据库
- **JWT** - 身份认证
- **Maven** - 依赖管理

### 前端技术栈
- **Vue 3** - 前端框架
- **Element Plus** - UI组件库
- **Vue Router** - 路由管理
- **Axios** - HTTP客户端
- **Vite** - 构建工具

## 🚀 功能特性

### 🔐 用户认证
- 用户注册/登录
- JWT Token认证
- 用户信息管理
- 权限控制

### 🚗 车辆管理
- 车辆信息录入
- 车辆列表查询
- 车辆信息编辑
- 车辆删除管理
- 支持车牌号、VIN码等信息

### 🔧 维保记录管理
- 维保记录添加
- 维保项目明细管理
- 维保记录查询和筛选
- 维保记录编辑和删除
- 费用自动计算

### 📊 数据统计
- 月度费用趋势分析
- 热门服务项目统计
- 车辆维保概览
- 系统数据概览
- 最近维保记录

### 📋 操作日志
- 用户操作记录
- 日志查询和筛选
- 过期日志清理

## 📁 项目结构

```
Intelli-Vehicles-master/
├── backend/                    # 后端项目
│   └── vehicles/
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/
│       │   │   │   └── com/zephyrover/vehicles/
│       │   │   │       ├── config/          # 配置类
│       │   │   │       ├── controller/      # 控制器
│       │   │   │       ├── entity/          # 实体类
│       │   │   │       ├── mapper/          # 数据访问层
│       │   │   │       ├── service/         # 业务逻辑层
│       │   │   │       ├── common/          # 通用类
│       │   │   │       └── util/            # 工具类
│       │   │   └── resources/
│       │   │       ├── application.yml     # 配置文件
│       │   │       └── mapper/              # MyBatis映射文件
│       │   └── test/                        # 测试代码
│       ├── pom.xml                          # Maven配置
│       ├── start.bat                        # Windows启动脚本
│       └── start.sh                         # Linux启动脚本
├── frontend/                   # 前端项目
│   ├── src/
│   │   ├── api/                # API接口
│   │   ├── pages/              # 页面组件
│   │   │   ├── auth/           # 认证页面
│   │   │   ├── dashboard/      # 仪表板
│   │   │   ├── vehicle/        # 车辆管理
│   │   │   ├── maintenance/    # 维保管理
│   │   │   └── log/            # 日志管理
│   │   ├── layouts/            # 布局组件
│   │   ├── router/             # 路由配置
│   │   └── assets/             # 静态资源
│   ├── package.json            # 依赖配置
│   └── vite.config.js          # Vite配置
├── vehicle_maintenance_schema.sql  # 数据库脚本
└── README.md                   # 项目说明
```

## 🛠️ 环境要求

### 开发环境
- **Java**: 17 或更高版本
- **Node.js**: 16 或更高版本
- **MySQL**: 8.0 或更高版本
- **Maven**: 3.6 或更高版本

### 推荐IDE
- **后端**: IntelliJ IDEA / Eclipse
- **前端**: VS Code / WebStorm

## 📦 安装部署

### 1. 克隆项目
```bash
git clone <repository-url>
cd Intelli-Vehicles-master
```

### 2. 数据库配置
```sql
# 创建数据库
CREATE DATABASE vehicle_maintenance CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库脚本
mysql -u root -p vehicle_maintenance < vehicle_maintenance_schema.sql
```

### 3. 后端配置
```bash
cd backend/vehicles

# 修改数据库配置 (src/main/resources/application.yml)
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vehicle_maintenance
    username: your_username
    password: your_password

# 编译项目
mvn clean package

# 启动服务
java -jar target/vehicles-0.0.1-SNAPSHOT.jar
# 或使用启动脚本
./start.sh  # Linux/Mac
start.bat   # Windows
```

### 4. 前端配置
```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 🔧 配置说明

### 后端配置 (application.yml)
```yaml
server:
  port: 8081                    # 服务端口

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/vehicle_maintenance
    username: root              # 数据库用户名
    password: your_password     # 数据库密码
```

### 前端配置 (src/api/request.js)
```javascript
const request = axios.create({
  baseURL: 'http://localhost:8081',  // 后端服务地址
  timeout: 10000
})
```

## 🌐 API接口

系统提供完整的RESTful API接口，详细文档请参考：
- [API详细文档](backend/vehicles/API_DOCUMENTATION_DETAILED.md)

### 主要接口模块
- **认证模块**: `/api/auth/*`
- **车辆管理**: `/api/vehicles/*`
- **维保记录**: `/api/records/*`
- **数据统计**: `/api/statistics/*`
- **操作日志**: `/api/logs/*`

## 🎯 使用指南

### 1. 用户注册登录
- 访问 `http://localhost:3000/login`
- 注册新用户或使用现有账户登录

### 2. 车辆管理
- 添加车辆信息（车牌号、VIN码、品牌型号等）
- 查看和编辑车辆列表
- 支持关键词搜索

### 3. 维保记录管理
- 为车辆添加维保记录
- 记录维保项目和费用
- 查看维保历史和统计

### 4. 数据统计
- 查看月度费用趋势
- 分析热门维保项目
- 获取系统数据概览

## 🔍 测试

### 后端测试
```bash
cd backend/vehicles
mvn test
```

### 前端测试
```bash
cd frontend
npm run test
```

### API测试
可以使用以下工具测试API：
- **Postman**: 导入API集合进行测试
- **curl**: 使用命令行测试
- **浏览器**: 直接访问GET接口

## 🐛 常见问题

### 1. 数据库连接失败
- 检查MySQL服务是否启动
- 确认数据库配置信息正确
- 检查防火墙设置

### 2. 前端无法访问后端
- 确认后端服务正常启动（端口8081）
- 检查前端API baseURL配置
- 确认跨域配置正确

### 3. Token认证失败
- 检查JWT配置
- 确认Token格式正确
- 检查Token是否过期

### 4. 数据显示异常
- 检查API返回数据格式
- 确认前端数据处理逻辑
- 查看浏览器控制台错误信息

## 📝 更新日志

### v1.0.0 (2025-06-20)
- 初始版本发布
- 完成基础功能开发
- 用户认证系统
- 车辆和维保记录管理
- 数据统计功能

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👥 作者

- **开发者**: GUC-LiuTong
- **邮箱**: admin@zephyrover.com
- **项目主页**: https://github.com/Cusnd/Intelli-Vehicles

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户！

---

如果您在使用过程中遇到任何问题，请提交 Issue 或联系我们。 