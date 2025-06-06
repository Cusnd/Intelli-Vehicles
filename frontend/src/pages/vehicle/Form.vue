<template>
  <div class="vehicle-form">
    <div class="page-header">
      <h2>{{ isEdit ? '编辑车辆' : '添加车辆' }}</h2>
      <el-button @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
    </div>

    <el-card>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
        class="vehicle-form-content"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="车牌号" prop="licensePlate">
              <el-input
                v-model="formData.licensePlate"
                placeholder="请输入车牌号"
                :disabled="isEdit"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车架号VIN" prop="vin">
              <el-input
                v-model="formData.vin"
                placeholder="请输入车架号VIN"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="品牌" prop="brand">
              <el-input
                v-model="formData.brand"
                placeholder="请输入车辆品牌"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="型号" prop="model">
              <el-input
                v-model="formData.model"
                placeholder="请输入车辆型号"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="车主姓名" prop="ownerName">
              <el-input
                v-model="formData.ownerName"
                placeholder="请输入车主姓名"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="车主电话" prop="ownerPhone">
              <el-input
                v-model="formData.ownerPhone"
                placeholder="请输入车主电话"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item>
          <el-button
            type="primary"
            :loading="loading"
            @click="handleSubmit"
          >
            {{ isEdit ? '更新' : '添加' }}
          </el-button>
          <el-button @click="handleReset">
            重置
          </el-button>
          <el-button @click="$router.back()">
            取消
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { addVehicle, updateVehicle, getVehicleById } from '@/api/vehicle'

const router = useRouter()
const route = useRoute()
const formRef = ref()
const loading = ref(false)

// 判断是否为编辑模式
const isEdit = computed(() => !!route.params.id)

// 表单数据
const formData = reactive({
  licensePlate: '',
  vin: '',
  brand: '',
  model: '',
  ownerName: '',
  ownerPhone: '',
  userId: null
})

// 表单验证规则
const rules = {
  licensePlate: [
    { required: true, message: '请输入车牌号', trigger: 'blur' }
  ],
  vin: [
    { required: true, message: '请输入车架号VIN', trigger: 'blur' }
  ],
  brand: [
    { required: true, message: '请输入车辆品牌', trigger: 'blur' }
  ],
  model: [
    { required: true, message: '请输入车辆型号', trigger: 'blur' }
  ],
  ownerName: [
    { required: true, message: '请输入车主姓名', trigger: 'blur' }
  ],
  ownerPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ]
}

// 加载车辆详情（编辑模式）
const loadVehicleDetail = async () => {
  if (!isEdit.value) return
  
  try {
    const vehicleId = route.params.id
    const vehicle = await getVehicleById(vehicleId)
    
    Object.assign(formData, vehicle)
  } catch (error) {
    console.error('获取车辆详情失败:', error)
    ElMessage.error('获取车辆详情失败')
    router.back()
  }
}

// 提交表单
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    loading.value = true
    
    // 获取当前用户ID
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    formData.userId = userInfo.userId
    
    if (isEdit.value) {
      await updateVehicle(route.params.id, formData)
      ElMessage.success('更新成功')
    } else {
      await addVehicle(formData)
      ElMessage.success('添加成功')
    }
    
    router.push('/vehicles')
  } catch (error) {
    console.error('操作失败:', error)
  } finally {
    loading.value = false
  }
}

// 重置表单
const handleReset = () => {
  formRef.value.resetFields()
}

onMounted(() => {
  loadVehicleDetail()
})
</script>

<style scoped>
.vehicle-form {
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

.vehicle-form-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}
</style> 