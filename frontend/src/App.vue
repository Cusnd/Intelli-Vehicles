<template>
  <router-view />
</template>

<script setup>
// 主应用组件，通过router-view显示当前路由对应的组件
</script>

<style>
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
}

#app {
  height: 100vh;
}

* {
  box-sizing: border-box;
}

.el-button + .el-button {
  margin-left: 8px;
}
</style>
