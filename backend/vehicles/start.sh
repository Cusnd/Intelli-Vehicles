#!/bin/bash

echo "正在启动智能车辆维保记录管理系统..."
echo

echo "检查Maven环境..."
mvn -version
if [ $? -ne 0 ]; then
    echo "错误：Maven未正确安装或配置"
    exit 1
fi

echo
echo "正在下载依赖并编译项目..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "错误：项目编译失败"
    exit 1
fi

echo
echo "正在启动Spring Boot应用..."
mvn spring-boot:run 