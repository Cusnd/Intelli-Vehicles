<template>
  <div class="maintenance-list">
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-select
            v-model="searchParams.vehicleId"
            placeholder="选择车辆"
            clearable
            filterable
            @change="handleSearch"
          >
            <el-option
              v-for="vehicle in vehicleOptions"
              :key="vehicle.id"
              :label="`${vehicle.licensePlate} - ${vehicle.brand} ${vehicle.model}`"
              :value="vehicle.id"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-button type="primary" @click="handleSearch" :loading="loading">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-col>
        <el-col :span="10" class="text-right">
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            添加维保记录
          </el-button>
        </el-col>
      </el-row>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card">
      <el-table
        :data="tableData"
        v-loading="loading"
        style="width: 100%"
        stripe
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="车辆信息" width="200">
          <template #default="scope">
            <div v-if="scope.row.vehicleInfo">
              <div>{{ scope.row.vehicleInfo.licensePlate }}</div>
              <div class="text-secondary">
                {{ scope.row.vehicleInfo.brand }} {{ scope.row.vehicleInfo.model }}
              </div>
            </div>
            <div v-else class="text-secondary">车辆ID: {{ scope.row.vehicleId }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="serviceDate" label="维保日期" width="120">
          <template #default="scope">
            {{ formatDate(scope.row.serviceDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="cost" label="费用" width="100">
          <template #default="scope">
            ¥{{ scope.row.cost || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="servicePerson" label="维保人员" width="120" />
        <el-table-column label="维保项目" min-width="200">
          <template #default="scope">
            <div v-if="scope.row.items && scope.row.items.length > 0">
              <el-tag 
                v-for="item in scope.row.items.slice(0, 3)" 
                :key="item.id"
                size="small"
                class="mr-1 mb-1"
              >
                {{ item.itemName }}
              </el-tag>
              <span v-if="scope.row.items.length > 3" class="text-secondary">
                等{{ scope.row.items.length }}项
              </span>
            </div>
            <span v-else class="text-secondary">无项目信息</span>
          </template>
        </el-table-column>
        <el-table-column prop="remarks" label="备注" min-width="150" show-overflow-tooltip />
        <el-table-column prop="createTime" label="创建时间" width="160">
          <template #default="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" type="primary" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="small" type="warning" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-button 
              size="small" 
              type="danger" 
              @click="handleDelete(scope.row)"
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

    <!-- 详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="维保记录详情"
      width="60%"
      :before-close="handleCloseDetail"
    >
      <div v-if="currentRecord">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="维保ID">{{ currentRecord.id }}</el-descriptions-item>
          <el-descriptions-item label="车辆信息">
            <div v-if="currentRecord.vehicleInfo">
              {{ currentRecord.vehicleInfo.licensePlate }} - 
              {{ currentRecord.vehicleInfo.brand }} {{ currentRecord.vehicleInfo.model }}
            </div>
            <div v-else>车辆ID: {{ currentRecord.vehicleId }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="维保日期">{{ formatDate(currentRecord.serviceDate) }}</el-descriptions-item>
          <el-descriptions-item label="总费用">¥{{ currentRecord.cost || 0 }}</el-descriptions-item>
          <el-descriptions-item label="维保人员">{{ currentRecord.servicePerson || '未填写' }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDateTime(currentRecord.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="更新时间">{{ formatDateTime(currentRecord.updateTime) }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ currentRecord.remarks || '无备注' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 维保项目列表 -->
        <div v-if="currentRecord.items && currentRecord.items.length > 0" class="mt-4">
          <h4>维保项目明细</h4>
          <el-table :data="currentRecord.items" border size="small">
            <el-table-column prop="itemName" label="项目名称" />
            <el-table-column prop="itemCost" label="项目费用" width="120">
              <template #default="scope">
                ¥{{ scope.row.itemCost || 0 }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Refresh, Plus } from '@element-plus/icons-vue'
import { 
  getMaintenanceRecordList, 
  deleteMaintenanceRecord,
  getMaintenanceRecordById 
} from '@/api/maintenance'
import { getVehicleList } from '@/api/vehicle'

const router = useRouter()

const loading = ref(false)
const tableData = ref([])
const vehicleOptions = ref([])
const detailDialogVisible = ref(false)
const currentRecord = ref(null)

// 搜索参数
const searchParams = reactive({
  vehicleId: null
})

// 分页参数
const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取车辆列表用于搜索筛选
const loadVehicleOptions = async () => {
  try {
    const result = await getVehicleList({ page: 1, size: 1000 })
    vehicleOptions.value = result.records || []
  } catch (error) {
    console.error('获取车辆列表失败:', error)
  }
}

// 获取维保记录列表
const loadData = async () => {
  try {
    loading.value = true
    const params = {
      page: pagination.page,
      size: pagination.size,
      ...searchParams
    }
    
    const result = await getMaintenanceRecordList(params)
    tableData.value = result.list || []
    pagination.total = result.total || 0
    
    // 加载车辆信息
    await loadVehicleInfo()
  } catch (error) {
    console.error('获取维保记录列表失败:', error)
    ElMessage.error('获取维保记录列表失败')
  } finally {
    loading.value = false
  }
}

// 加载车辆信息
const loadVehicleInfo = async () => {
  for (const record of tableData.value) {
    const vehicle = vehicleOptions.value.find(v => v.id === record.vehicleId)
    if (vehicle) {
      record.vehicleInfo = vehicle
    }
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置搜索
const resetSearch = () => {
  Object.keys(searchParams).forEach(key => {
    searchParams[key] = null
  })
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.page = 1
  loadData()
}

// 页码改变
const handleCurrentChange = (page) => {
  pagination.page = page
  loadData()
}

// 添加
const handleAdd = () => {
  router.push('/maintenance/add')
}

// 编辑
const handleEdit = (row) => {
  router.push(`/maintenance/edit/${row.id}`)
}

// 查看详情
const handleView = async (row) => {
  try {
    loading.value = true
    const result = await getMaintenanceRecordById(row.id)
    currentRecord.value = result
    
    // 加载车辆信息
    const vehicle = vehicleOptions.value.find(v => v.id === result.vehicleId)
    if (vehicle) {
      currentRecord.value.vehicleInfo = vehicle
    }
    
    detailDialogVisible.value = true
  } catch (error) {
    console.error('获取维保记录详情失败:', error)
    ElMessage.error('获取维保记录详情失败')
  } finally {
    loading.value = false
  }
}

// 关闭详情对话框
const handleCloseDetail = () => {
  detailDialogVisible.value = false
  currentRecord.value = null
}

// 删除
const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确认删除这条维保记录吗？', '确认删除', {
      type: 'warning'
    })
    
    await deleteMaintenanceRecord(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除维保记录失败:', error)
      ElMessage.error('删除失败')
    }
  }
}

// 格式化日期
const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

// 格式化日期时间
const formatDateTime = (datetime) => {
  if (!datetime) return '-'
  return new Date(datetime).toLocaleString('zh-CN')
}

onMounted(() => {
  loadVehicleOptions()
  loadData()
})
</script>

<style scoped>
.maintenance-list {
  padding: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.text-right {
  text-align: right;
}

.text-secondary {
  color: #999;
  font-size: 12px;
}

.mr-1 {
  margin-right: 4px;
}

.mb-1 {
  margin-bottom: 4px;
}

.mt-4 {
  margin-top: 20px;
}
</style> 