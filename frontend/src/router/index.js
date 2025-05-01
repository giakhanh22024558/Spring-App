import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/AppLogin.vue'
import Dashboard from '../components/AppDashboard.vue'
import Product from '../components/AppProducts.vue'
import Category from '@/components/AppCategory.vue'
import ProductAddUpdate from '@/components/ProductsAction/ProductAddUpdate.vue'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
  },
  {
    path: '/logout',
    name: 'Logout',
    beforeEnter: (to, from, next) => {
      // Clear token, then redirect to login
      localStorage.removeItem('token')
      next('/login')
    }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: Dashboard,
    meta: { 
      requiresAuth: true,
      title: 'Dashboard'
    } // optional: guard it later
  },
  {
    path: '/products',
    name: 'Products',
    component: Product,
    meta: { 
      requiresAuth: true ,
      title: 'Products'
    } // optional: guard it later
  },
  {
    path: '/categories',
    name: 'Categories',
    component: Category,
    meta: { 
      requiresAuth: true ,
      title: 'Categories List'
    } // optional: guard it later
  },
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/login',
  },
  {
    path: '/product/add',
    name: 'ProductAdd',
    component: ProductAddUpdate,  
    meta: { 
      requiresAuth: true ,
      title: 'Add new products'
    }
  },
  {
    path: '/product/update/:id',
    name: 'ProductUpdate',
    component: ProductAddUpdate,  
    meta: { 
      requiresAuth: true ,
      title: 'Products'
    }
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// Optional: Navigation guard for auth
// router.beforeEach((to, from, next) => {
//   const isAuthenticated = !!localStorage.getItem('token')
//   if (to.meta.requiresAuth && !isAuthenticated) {
//     next('/login')
//   } else {
//     next()
//   }
// })

export default router
