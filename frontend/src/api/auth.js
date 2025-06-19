import request from './request'

// 用户登录
export const login = (data) => {
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

// 用户注册
export const register = (data) => {
  return request({
    url: '/api/auth/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export const getUserProfile = (userId) => {
  return request({
    url: `/api/auth/profile?userId=${userId}`,
    method: 'get'
  })
}

// 更新用户信息
export const updateProfile = (data) => {
  return request({
    url: '/api/auth/profile',
    method: 'put',
    data
  })
} 