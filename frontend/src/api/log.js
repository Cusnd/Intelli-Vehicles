import request from './request'

// 获取操作日志列表
export const getLogList = (params) => {
  return request({
    url: '/api/logs',
    method: 'get',
    params
  })
}

// 记录操作日志
export const addLog = (data) => {
  return request({
    url: '/api/logs',
    method: 'post',
    data
  })
}

// 清理过期日志
export const cleanupLogs = (daysAgo = 30) => {
  return request({
    url: `/api/logs/cleanup?daysAgo=${daysAgo}`,
    method: 'delete'
  })
} 