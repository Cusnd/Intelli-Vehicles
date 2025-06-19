import request from './request'

// 分页查询维保记录
export const getMaintenanceRecordList = (params) => {
  return request({
    url: '/api/records',
    method: 'get',
    params
  })
}

// 根据ID查询维保记录详情
export const getMaintenanceRecordById = (id) => {
  return request({
    url: `/api/records/${id}`,
    method: 'get'
  })
}

// 添加维保记录
export const addMaintenanceRecord = (data) => {
  return request({
    url: '/api/records',
    method: 'post',
    data
  })
}

// 更新维保记录
export const updateMaintenanceRecord = (id, data) => {
  return request({
    url: `/api/records/${id}`,
    method: 'put',
    data
  })
}

// 删除维保记录
export const deleteMaintenanceRecord = (id) => {
  return request({
    url: `/api/records/${id}`,
    method: 'delete'
  })
}

// 根据车辆ID查询维保记录
export const getMaintenanceRecordsByVehicleId = (vehicleId) => {
  return request({
    url: `/api/records/vehicle/${vehicleId}`,
    method: 'get'
  })
} 