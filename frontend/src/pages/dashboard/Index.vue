<template>
  <div class="dashboard">
    <div class="welcome-card">
      <el-card>
        <h1>欢迎使用智能车辆维保记录管理系统</h1>
        <p>您可以通过左侧菜单进行以下操作：</p>
        <div class="feature-grid">
          <div class="feature-item">
            <el-icon size="48" color="#409EFF"><Van /></el-icon>
            <h3>车辆管理</h3>
            <p>添加、编辑、查询车辆信息</p>
            <el-button type="primary" @click="$router.push('/vehicles')">
              进入车辆管理
            </el-button>
          </div>
          <div class="feature-item">
            <el-icon size="48" color="#E6A23C"><Tools /></el-icon>
            <h3>维保记录</h3>
            <p>管理车辆维保记录</p>
            <el-button type="warning" @click="$router.push('/maintenance')">
              维保记录管理
            </el-button>
          </div>
          <div class="feature-item">
            <el-icon size="48" color="#67C23A"><Document /></el-icon>
            <h3>操作日志</h3>
            <p>查看系统操作记录</p>
            <el-button type="success" @click="$router.push('/logs')">
              查看操作日志
            </el-button>
          </div>
        </div>
      </el-card>
    </div>

    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon">
                <el-icon size="40" color="#409EFF"><Van /></el-icon>
              </div>
              <div class="stats-info">
                <h3>{{ stats.vehicleCount || 0 }}</h3>
                <p>车辆总数</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon">
                <el-icon size="40" color="#67C23A"><User /></el-icon>
              </div>
              <div class="stats-info">
                <h3>{{ stats.userCount || 0 }}</h3>
                <p>用户总数</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon">
                <el-icon size="40" color="#F56C6C"><Tools /></el-icon>
              </div>
              <div class="stats-info">
                <h3>{{ stats.maintenanceCount || 0 }}</h3>
                <p>维保记录</p>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="stats-card">
            <div class="stats-content">
              <div class="stats-icon">
                <el-icon size="40" color="#E6A23C"><Document /></el-icon>
              </div>
              <div class="stats-info">
                <h3>{{ stats.logCount || 0 }}</h3>
                <p>操作记录</p>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div class="quick-actions">
      <el-card>
        <template #header>
          <span>快捷操作</span>
        </template>
        <el-row :gutter="16">
          <el-col :span="6">
            <el-button 
              type="primary" 
              size="large" 
              class="quick-btn"
              @click="$router.push('/vehicles/add')"
            >
              <el-icon><Plus /></el-icon>
              添加车辆
            </el-button>
          </el-col>
          <el-col :span="6">
            <el-button 
              type="success" 
              size="large" 
              class="quick-btn"
              @click="$router.push('/vehicles')"
            >
              <el-icon><Search /></el-icon>
              查询车辆
            </el-button>
          </el-col>
          <el-col :span="4">
            <el-button 
              type="warning" 
              size="large" 
              class="quick-btn"
              @click="$router.push('/maintenance/add')"
            >
              <el-icon><Tools /></el-icon>
              添加维保
            </el-button>
          </el-col>
          <el-col :span="4">
            <el-button 
              type="info" 
              size="large" 
              class="quick-btn"
              @click="$router.push('/logs')"
            >
              <el-icon><View /></el-icon>
              查看日志
            </el-button>
          </el-col>
          <el-col :span="4">
            <el-button 
              type="default" 
              size="large" 
              class="quick-btn"
              @click="refreshData"
              :loading="loading"
            >
              <el-icon><Refresh /></el-icon>
              刷新数据
            </el-button>
          </el-col>
        </el-row>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Van, 
  Document, 
  User, 
  Plus, 
  Search, 
  View, 
  Refresh,
  Tools 
} from '@element-plus/icons-vue'
import { getVehicleList } from '@/api/vehicle'
import { getLogList } from '@/api/log'
import { getMaintenanceRecordList } from '@/api/maintenance'

const loading = ref(false)
const stats = ref({
  vehicleCount: 0,
  userCount: 1,
  logCount: 0,
  maintenanceCount: 0
})

// 获取统计数据
const loadStats = async () => {
  try {
    loading.value = true
    
    // 获取车辆总数
    const vehicleResult = await getVehicleList({ page: 1, size: 1 })
    stats.value.vehicleCount = vehicleResult.total || 0
    
    // 获取日志总数
    const logResult = await getLogList({ page: 1, size: 1 })
    stats.value.logCount = logResult.total || 0
    
    // 获取维保记录总数
    const maintenanceResult = await getMaintenanceRecordList({ page: 1, size: 1 })
    stats.value.maintenanceCount = maintenanceResult.total || 0
    
  } catch (error) {
    console.error('获取统计数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 刷新数据
const refreshData = async () => {
  await loadStats()
  ElMessage.success('数据已刷新')
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.dashboard {
  max-width: 1200px;
  margin: 0 auto;
}

.welcome-card {
  margin-bottom: 20px;
}

.welcome-card h1 {
  text-align: center;
  color: #409EFF;
  margin-bottom: 20px;
}

.welcome-card p {
  text-align: center;
  color: #666;
  margin-bottom: 30px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 30px;
  margin-top: 30px;
}

.feature-item {
  text-align: center;
  padding: 20px;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  transition: transform 0.2s;
}

.feature-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.feature-item h3 {
  margin: 15px 0 10px;
  color: #333;
}

.feature-item p {
  color: #666;
  margin-bottom: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stats-card {
  height: 120px;
}

.stats-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stats-icon {
  margin-right: 20px;
}

.stats-info h3 {
  font-size: 28px;
  margin: 0 0 8px 0;
  color: #333;
}

.stats-info p {
  margin: 0;
  color: #666;
  font-size: 14px;
}

.quick-actions {
  margin-bottom: 20px;
}

.quick-btn {
  width: 100%;
  height: 60px;
  font-size: 16px;
}

.quick-btn .el-icon {
  margin-right: 8px;
}
</style> 