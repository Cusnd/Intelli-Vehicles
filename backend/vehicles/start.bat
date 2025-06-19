@echo off
echo 正在启动智能车辆维保记录管理系统...
echo.

echo 检查Java环境...
java -version
if %errorlevel% neq 0 (
    echo 错误：Java未正确安装或配置
    echo 请安装Java 17或更高版本
    pause
    exit /b 1
)

echo.
echo 正在使用Maven Wrapper下载依赖并编译项目...
echo 首次运行可能需要较长时间下载Maven和依赖...
.\mvnw.cmd clean compile
if %errorlevel% neq 0 (
    echo 错误：项目编译失败
    pause
    exit /b 1
)

echo.
echo 正在启动Spring Boot应用...
.\mvnw.cmd spring-boot:run

pause 