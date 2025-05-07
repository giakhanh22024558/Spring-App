import { createRouter, createWebHistory } from 'vue-router'
import Login from '../components/AppLogin.vue'
import Dashboard from '../components/AppDashboard.vue'
import Product from '../components/AppProducts.vue'
import Category from '@/components/AppCategory.vue'
import Unit from '@/components/AppUnit.vue'
import ProductAddUpdate from '@/components/ProductsAction/ProductAddUpdate.vue'
import Users from '@/components/AppUsers.vue'
import UserAddUpdate from '@/components/UserAction/UserAddUpdate.vue' // ✅ Added import

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: {
      layout: 'auth',
      title: 'Login'
    }
  },
  {
    path: '/logout',
    name: 'Logout',
    beforeEnter: (to, from, next) => {
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
    }
  },
  {
    path: '/products',
    name: 'Products',
    component: Product,
    meta: { 
      requiresAuth: true ,
      title: 'Products'
    }
  },
  {
    path: '/products/categories',
    name: 'Categories',
    component: Category,
    meta: { 
      requiresAuth: true ,
      title: 'Products categories'
    }
  },
  {
    path: '/products/units',
    name: 'Units',
    component: Unit,
    meta: { 
      requiresAuth: true ,
      title: 'Products units'
    }
  },
  {
    path: '/users',
    name: 'User',
    component: Users,
    meta: { 
      requiresAuth: true ,
      title: 'Users',
      requiresAdmin: true
    }
  },
  {
    path: '/users/add',
    name: 'UserAdd',
    component: UserAddUpdate, // ✅ Add route for adding a user
    meta: { 
      requiresAuth: true,
      title: 'Add new user'
    }
  },
  {
    path: '/users/update/:id',
    name: 'UserUpdate',
    component: UserAddUpdate, // ✅ Add route for updating a user
    meta: { 
      requiresAuth: true,
      title: 'Edit user'
    }
  },
  {
    path: '/products/add',
    name: 'ProductAdd',
    component: ProductAddUpdate,  
    meta: { 
      requiresAuth: true ,
      title: 'Add new products'
    }
  },
  {
    path: '/products/update/:id',
    name: 'ProductUpdate',
    component: ProductAddUpdate,  
    meta: { 
      requiresAuth: true ,
      title: 'Products'
    }
  },
  {
    path: '/',
    redirect: '/dashboard',
  },
  {
    path: '/:catchAll(.*)',
    redirect: '/login',
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = !!localStorage.getItem('access_token')
  const roles = JSON.parse(localStorage.getItem('roles') || '[]')

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Not logged in
    return next('/login')
  }

  if (to.meta.requiresAdmin) {
    // Block if only has 'USER' role
    const onlyUserRole = roles.length === 1 && roles[0] === 'USER'
    if (onlyUserRole) {
      alert("You're not authorized to access this page.")
      return next('/') // Redirect to home or dashboard
    }
  }

  next()
})


export default router
