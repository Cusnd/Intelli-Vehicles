import request from './request'

// 获取车辆列表
export const getVehicleList = (params) => {
  return request({
    url: '/vehicles',
    method: 'get',
    params
  })
}

// 获取车辆详情
export const getVehicleById = (id) => {
  return request({
    url: `/vehicles/${id}`,
    method: 'get'
  })
}

// 添加车辆
export const addVehicle = (data) => {
  return request({
    url: '/vehicles',
    method: 'post',
    data
  })
}

// 更新车辆
export const updateVehicle = (id, data) => {
  return request({
    url: `/vehicles/${id}`,
    method: 'put',
    data
  })
}

// 删除车辆
export const deleteVehicle = (id) => {
  return request({
    url: `/vehicles/${id}`,
    method: 'delete'
  })
} 