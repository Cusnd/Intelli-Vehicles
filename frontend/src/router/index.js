import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../pages/auth/Login.vue'),
    meta: { hideLayout: true }
  },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../pages/dashboard/Index.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'vehicles',
        name: 'VehicleList',
        component: () => import('../pages/vehicle/List.vue'),
        meta: { title: '车辆管理' }
      },
      {
        path: 'vehicles/add',
        name: 'VehicleAdd',
        component: () => import('../pages/vehicle/Form.vue'),
        meta: { title: '添加车辆' }
      },
      {
        path: 'vehicles/edit/:id',
        name: 'VehicleEdit',
        component: () => import('../pages/vehicle/Form.vue'),
        meta: { title: '编辑车辆' }
      },
      {
        path: 'logs',
        name: 'LogList',
        component: () => import('../pages/log/List.vue'),
        meta: { title: '操作日志' }
      },
      {
        path: 'maintenance',
        name: 'MaintenanceList',
        component: () => import('../pages/maintenance/List.vue'),
        meta: { title: '维保记录' }
      },
      {
        path: 'maintenance/add',
        name: 'MaintenanceAdd',
        component: () => import('../pages/maintenance/Form.vue'),
        meta: { title: '添加维保记录' }
      },
      {
        path: 'maintenance/edit/:id',
        name: 'MaintenanceEdit',
        component: () => import('../pages/maintenance/Form.vue'),
        meta: { title: '编辑维保记录' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  console.log('路由守卫 - 目标路径:', to.path)
  console.log('路由守卫 - 来源路径:', from.path)
  console.log('路由守卫 - Token状态:', token ? '存在' : '不存在')
  
  if (to.path === '/login') {
    console.log('访问登录页，直接通过')
    next()
  } else if (!token) {
    console.log('无Token，重定向到登录页')
    next('/login')
  } else {
    console.log('有Token，允许访问')
    next()
  }
})

export default router 