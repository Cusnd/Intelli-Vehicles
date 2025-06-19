<template>
  <div class="maintenance-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? '编辑维保记录' : '添加维保记录' }}</span>
          <el-button @click="goBack">返回</el-button>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="120px"
        v-loading="loading"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="选择车辆" prop="vehicleId">
              <el-select
                v-model="form.vehicleId"
                placeholder="请选择车辆"
                filterable
                style="width: 100%"
              >
                <el-option
                  v-for="vehicle in vehicleOptions"
                  :key="vehicle.id"
                  :label="`${vehicle.licensePlate} - ${vehicle.brand} ${vehicle.model}`"
                  :value="vehicle.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="维保日期" prop="serviceDate">
              <el-date-picker
                v-model="form.serviceDate"
                type="date"
                placeholder="选择维保日期"
                style="width: 100%"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="维保人员" prop="servicePerson">
              <el-input
                v-model="form.servicePerson"
                placeholder="请输入维保人员"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总费用" prop="cost">
              <el-input-number
                v-model="form.cost"
                :min="0"
                :precision="2"
                placeholder="请输入总费用"
                style="width: 100%"
                @change="updateTotalCost"
              />
              <div class="cost-tip">
                <span v-if="calculatedCost > 0">
                  项目费用合计: ¥{{ calculatedCost.toFixed(2) }}
                </span>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="备注" prop="remarks">
          <el-input
            v-model="form.remarks"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>

        <!-- 维保项目 -->
        <el-form-item label="维保项目">
          <div class="maintenance-items">
            <div class="items-header">
              <span>维保项目明细</span>
              <el-button
                type="primary"
                size="small"
                @click="addMaintenanceItem"
              >
                添加项目
              </el-button>
            </div>

            <div v-if="form.items && form.items.length > 0" class="items-list">
              <el-table :data="form.items" border size="small">
                <el-table-column label="项目名称" min-width="200">
                  <template #default="scope">
                    <el-input
                      v-model="scope.row.itemName"
                      placeholder="请输入项目名称"
                      @input="validateItem(scope.$index)"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="项目费用" width="150">
                  <template #default="scope">
                    <el-input-number
                      v-model="scope.row.itemCost"
                      :min="0"
                      :precision="2"
                      placeholder="费用"
                      style="width: 100%"
                      @change="calculateTotalCost"
                    />
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="80">
                  <template #default="scope">
                    <el-button
                      type="danger"
                      size="small"
                      @click="removeMaintenanceItem(scope.$index)"
                    >
                      删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <div v-else class="no-items">
              <el-empty description="暂无维保项目" :image-size="60" />
            </div>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            @click="handleSubmit"
            :loading="submitting"
          >
            {{ isEdit ? '更新' : '保存' }}
          </el-button>
          <el-button @click="resetForm">重置</el-button>
          <el-button @click="goBack">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  addMaintenanceRecord,
  updateMaintenanceRecord,
  getMaintenanceRecordById
} from '@/api/maintenance'
import { getVehicleList } from '@/api/vehicle'

const router = useRouter()
const route = useRoute()

const formRef = ref()
const loading = ref(false)
const submitting = ref(false)
const vehicleOptions = ref([])

// 判断是否为编辑模式
const isEdit = computed(() => !!route.params.id)

// 表单数据
const form = reactive({
  vehicleId: null,
  serviceDate: '',
  cost: 0,
  servicePerson: '',
  remarks: '',
  items: []
})

// 计算的总费用
const calculatedCost = computed(() => {
  if (!form.items || form.items.length === 0) return 0
  return form.items.reduce((total, item) => {
    return total + (parseFloat(item.itemCost) || 0)
  }, 0)
})

// 表单验证规则
const rules = {
  vehicleId: [
    { required: true, message: '请选择车辆', trigger: 'change' }
  ],
  serviceDate: [
    { required: true, message: '请选择维保日期', trigger: 'change' }
  ],
  servicePerson: [
    { required: true, message: '请输入维保人员', trigger: 'blur' }
  ],
  cost: [
    { required: true, message: '请输入总费用', trigger: 'blur' }
  ]
}

// 获取车辆列表
const loadVehicleOptions = async () => {
  try {
    const result = await getVehicleList({ page: 1, size: 1000 })
    vehicleOptions.value = result.records || []
  } catch (error) {
    console.error('获取车辆列表失败:', error)
  }
}

// 加载编辑数据
const loadEditData = async () => {
  if (!isEdit.value) return
  
  try {
    loading.value = true
    const result = await getMaintenanceRecordById(route.params.id)
    
    // 填充表单
    Object.keys(form).forEach(key => {
      if (result[key] !== undefined) {
        form[key] = result[key]
      }
    })
    
    // 处理维保项目
    if (result.items && result.items.length > 0) {
      form.items = result.items.map(item => ({
        id: item.id,
        itemName: item.itemName,
        itemCost: item.itemCost || 0
      }))
    }
    
  } catch (error) {
    console.error('获取维保记录详情失败:', error)
    ElMessage.error('获取维保记录详情失败')
    goBack()
  } finally {
    loading.value = false
  }
}

// 添加维保项目
const addMaintenanceItem = () => {
  form.items.push({
    itemName: '',
    itemCost: 0
  })
}

// 删除维保项目
const removeMaintenanceItem = (index) => {
  form.items.splice(index, 1)
  calculateTotalCost()
}

// 验证项目
const validateItem = (index) => {
  // 可以在这里添加项目验证逻辑
}

// 计算总费用
const calculateTotalCost = () => {
  nextTick(() => {
    // 如果用户没有手动设置费用，自动计算
    if (form.cost === 0 || form.cost === calculatedCost.value) {
      form.cost = calculatedCost.value
    }
  })
}

// 更新总费用
const updateTotalCost = () => {
  // 用户手动修改总费用时的处理
}

// 重置表单
const resetForm = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
  form.items = []
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    // 验证至少有一个维保项目
    if (!form.items || form.items.length === 0) {
      ElMessage.warning('请至少添加一个维保项目')
      return
    }
    
    // 验证维保项目完整性
    const invalidItems = form.items.some(item => !item.itemName.trim())
    if (invalidItems) {
      ElMessage.warning('请填写完整的维保项目信息')
      return
    }
    
    submitting.value = true
    
    const submitData = {
      ...form,
      // 过滤掉空的维保项目
      items: form.items.filter(item => item.itemName.trim())
    }
    
    if (isEdit.value) {
      await updateMaintenanceRecord(route.params.id, submitData)
      ElMessage.success('更新维保记录成功')
    } else {
      await addMaintenanceRecord(submitData)
      ElMessage.success('添加维保记录成功')
    }
    
    // 添加或更新成功后需要刷新列表
    goBack(true)
    
  } catch (error) {
    console.error('保存维保记录失败:', error)
    ElMessage.error('保存失败: ' + (error.message || '未知错误'))
  } finally {
    submitting.value = false
  }
}

// 返回列表页
const goBack = (needRefresh = false) => {
  if (needRefresh) {
    // 添加时间戳参数强制刷新
    router.push(`/maintenance?refresh=${Date.now()}`)
  } else {
    router.push('/maintenance')
  }
}

onMounted(() => {
  loadVehicleOptions()
  if (isEdit.value) {
    loadEditData()
  } else {
    // 添加模式下默认添加一个维保项目
    addMaintenanceItem()
  }
})
</script>

<style scoped>
.maintenance-form {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.maintenance-items {
  width: 100%;
}

.items-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
  font-weight: bold;
}

.items-list {
  margin-bottom: 10px;
}

.no-items {
  text-align: center;
  padding: 20px;
  border: 1px dashed #dcdfe6;
  border-radius: 4px;
}

.cost-tip {
  margin-top: 5px;
  font-size: 12px;
  color: #909399;
}
</style> 