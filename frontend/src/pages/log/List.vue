<template>
  <div class="log-list">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>操作日志</h2>
      <el-button type="warning" @click="handleCleanup">
        <el-icon><Delete /></el-icon>
        清理日志
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="用户ID">
          <el-input
            v-model="searchForm.userId"
            placeholder="请输入用户ID"
            style="width: 120px"
            clearable
          />
        </el-form-item>
        <el-form-item label="操作内容">
          <el-input
            v-model="searchForm.operation"
            placeholder="请输入操作内容关键词"
            style="width: 200px"
            clearable
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 日志表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无日志数据"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userId" label="用户ID" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.userId" type="info">{{ row.userId }}</el-tag>
            <span v-else class="text-muted">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="operation" label="操作内容" min-width="200">
          <template #default="{ row }">
            <span class="operation-text">{{ row.operation }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="ipAddress" label="IP地址" width="140">
          <template #default="{ row }">
            <el-tag type="success" size="small">{{ row.ipAddress || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="操作时间" width="180">
          <template #default="{ row }">
            <el-icon class="time-icon"><Clock /></el-icon>
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[20, 50, 100, 200]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 清理日志对话框 -->
    <el-dialog v-model="showCleanupDialog" title="清理日志" width="400px">
      <p>请选择要清理多少天前的日志：</p>
      <el-form>
        <el-form-item label="保留天数">
          <el-input-number
            v-model="cleanupDays"
            :min="1"
            :max="365"
            placeholder="30"
          />
          <span style="margin-left: 10px; color: #999;">天前的日志将被删除</span>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCleanupDialog = false">取消</el-button>
        <el-button 
          type="danger" 
          :loading="cleanupLoading"
          @click="confirmCleanup"
        >
          确认清理
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  Search, 
  Refresh, 
  Delete, 
  Clock 
} from '@element-plus/icons-vue'
import { getLogList, cleanupLogs } from '@/api/log'

const loading = ref(false)
const tableData = ref([])
const showCleanupDialog = ref(false)
const cleanupLoading = ref(false)
const cleanupDays = ref(30)

// 搜索表单
const searchForm = reactive({
  userId: '',
  operation: ''
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 20,
  total: 0
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  })
}

// 加载日志列表
const loadLogList = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.page,
      size: pagination.size,
      userId: searchForm.userId || undefined,
      operation: searchForm.operation || undefined
    }
    
    const result = await getLogList(params)
    tableData.value = result.records || []
    pagination.total = result.total || 0
  } catch (error) {
    console.error('获取日志列表失败:', error)
    ElMessage.error('获取日志列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadLogList()
}

// 重置
const handleReset = () => {
  searchForm.userId = ''
  searchForm.operation = ''
  pagination.page = 1
  loadLogList()
}

// 显示清理对话框
const handleCleanup = () => {
  showCleanupDialog.value = true
}

// 确认清理
const confirmCleanup = async () => {
  try {
    await ElMessageBox.confirm(
      `确认清理${cleanupDays.value}天前的日志吗？此操作不可恢复！`,
      '清理确认',
      { type: 'warning' }
    )
    
    cleanupLoading.value = true
    await cleanupLogs(cleanupDays.value)
    
    ElMessage.success('日志清理成功')
    showCleanupDialog.value = false
    loadLogList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('清理日志失败:', error)
    }
  } finally {
    cleanupLoading.value = false
  }
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadLogList()
}

// 当前页改变
const handleCurrentChange = (page) => {
  pagination.page = page
  loadLogList()
}

onMounted(() => {
  loadLogList()
})
</script>

<style scoped>
.log-list {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  color: #333;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: center;
}

.operation-text {
  color: #606266;
  font-size: 14px;
}

.time-icon {
  margin-right: 4px;
  color: #909399;
}

.text-muted {
  color: #999;
}
</style> 