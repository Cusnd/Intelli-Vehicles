<template>
  <div class="vehicle-list">
    <!-- 页面标题和操作按钮 -->
    <div class="page-header">
      <h2>车辆管理</h2>
      <el-button type="primary" @click="$router.push('/vehicles/add')">
        <el-icon><Plus /></el-icon>
        添加车辆
      </el-button>
    </div>

    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入车牌号、车主姓名等"
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

    <!-- 车辆表格 -->
    <el-card class="table-card">
      <el-table
        v-loading="loading"
        :data="tableData"
        style="width: 100%"
        empty-text="暂无车辆数据"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="licensePlate" label="车牌号" width="120">
          <template #default="{ row }">
            <el-tag type="primary">{{ row.licensePlate }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="brand" label="品牌" width="100" />
        <el-table-column prop="model" label="型号" width="120" />
        <el-table-column prop="vin" label="车架号VIN" width="180" />
        <el-table-column prop="ownerName" label="车主姓名" width="100" />
        <el-table-column prop="ownerPhone" label="车主电话" width="130" />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="160" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="danger"
              size="small"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search, Refresh } from '@element-plus/icons-vue'
import { getVehicleList, deleteVehicle } from '@/api/vehicle'

const router = useRouter()
const loading = ref(false)
const tableData = ref([])

// 搜索表单
const searchForm = reactive({
  keyword: ''
})

// 分页信息
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  return new Date(dateString).toLocaleString('zh-CN')
}

// 加载车辆列表
const loadVehicleList = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.page,
      size: pagination.size,
      keyword: searchForm.keyword || undefined
    }
    
    const result = await getVehicleList(params)
    tableData.value = result.records || []
    pagination.total = result.total || 0
  } catch (error) {
    console.error('获取车辆列表失败:', error)
    ElMessage.error('获取车辆列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadVehicleList()
}

// 重置
const handleReset = () => {
  searchForm.keyword = ''
  pagination.page = 1
  loadVehicleList()
}

// 编辑车辆
const handleEdit = (row) => {
  router.push(`/vehicles/edit/${row.id}`)
}

// 删除车辆
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确认删除车辆 "${row.licensePlate}" 吗？此操作不可恢复！`,
      '删除确认',
      {
        type: 'warning'
      }
    )
    
    await deleteVehicle(row.id)
    ElMessage.success('删除成功')
    loadVehicleList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除车辆失败:', error)
    }
  }
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadVehicleList()
}

// 当前页改变
const handleCurrentChange = (page) => {
  pagination.page = page
  loadVehicleList()
}

onMounted(() => {
  loadVehicleList()
})
</script>

<style scoped>
.vehicle-list {
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
</style> 