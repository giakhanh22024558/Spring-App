<!-- src/layouts/MainLayout.vue -->
<template>
    <div class="flex h-screen">
        <!-- Sidebar -->
        <aside class="w-64 bg-gray-800 text-white flex flex-col p-4">
            <div class="text-2xl font-bold mb-8">
                Product Manager
            </div>
            <nav class="flex flex-col gap-4">
                <!-- Dashboard -->
                <RouterLink to="/" class="hover:text-green-400"
                    :class="{ 'text-green-400 font-semibold': $route.path === '/' }">
                    Dashboard
                </RouterLink>

                <!-- Products Hover -->
                <div class="relative group">
                    <!-- Products Link -->
                    <RouterLink to="/products"
                        :class="['hover:text-green-400', { 'text-green-400 font-semibold': $route.path === '/products' }, 'flex justify-between items-center']">
                        <span>Products</span>
                        <svg xmlns="http://www.w3.org/2000/svg"
                            class="w-4 h-4 ml-1 transition-transform transform group-hover:rotate-90" fill="none"
                            stroke="currentColor" viewBox="0 0 24 24" stroke-width="2">
                            <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
                        </svg>
                    </RouterLink>

                    <!-- Dropdown Links (Show on hover) -->
                    <div
                        class="absolute right-[-205px] z-50 top-0 w-48 mt-2 bg-gray-700 text-white rounded shadow-lg opacity-0 group-hover:opacity-100 group-hover:block transition-opacity">
                        <RouterLink to="/products" class="block px-4 py-2 hover:text-green-400"
                            :class="{ 'text-green-400 font-semibold': $route.path === '/products' }">
                            All Products
                        </RouterLink>
                        <RouterLink to="/products/categories" class="block px-4 py-2 hover:text-green-400"
                            :class="{ 'text-green-400 font-semibold': $route.path === '/products/categories' }">
                            Product Categories
                        </RouterLink>
                        <RouterLink to="/products/units" class="block px-4 py-2 hover:text-green-400"
                            :class="{ 'text-green-400 font-semibold': $route.path === '/products/units' }">
                            Product Units
                        </RouterLink>
                    </div>
                </div>

                <!-- Users Link -->
                <RouterLink v-if="canAccessUsersMenu" to="/users" class="hover:text-green-400"
                    :class="{ 'text-green-400 font-semibold': $route.path === '/users' }">
                    Users
                </RouterLink>

            </nav>
        </aside>

        <!-- Main content -->
        <div class="flex-1 flex flex-col">
            <!-- Header -->
            <header class="bg-white shadow-md p-4 flex justify-between items-center">
                <h1 class="text-xl font-semibold">{{ headerTitle }}</h1>
                <div class="flex items-center gap-4 relative">
                    <span @click="isMenuVisible = !isMenuVisible" class="text-sm text-gray-700 cursor-pointer">
                        Hello, {{ username }}
                    </span>
                    <div v-if="isMenuVisible"
                        class="absolute right-0 top-10 bg-gray-800 text-white text-xs p-2 rounded shadow-lg w-40">
                        <a @click="logout" class="block hover:text-green-400 cursor-pointer">Logout</a>
                    </div>
                </div>
            </header>

            <!-- Page Content -->
            <main class="flex-1 p-6 overflow-auto bg-gray-50">
                <slot />
            </main>
        </div>
    </div>
</template>


<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, watchEffect, computed } from 'vue'

const route = useRoute()
const router = useRouter()

const headerTitle = ref('Product Management Dashboard')
const username = ref(localStorage.getItem('username') || 'Guest')

// ✅ Get roles from localStorage and parse
const userRoles = ref([])
try {
    const storedRoles = JSON.parse(localStorage.getItem('roles'))
    userRoles.value = Array.isArray(storedRoles) ? storedRoles : []
} catch (e) {
    console.error('Failed to parse roles', e)
}

// ✅ Only show Users menu if user has more than just USER role
const canAccessUsersMenu = computed(() => {
    return !(userRoles.value.length === 1 && userRoles.value[0] === 'USER')
})

watchEffect(() => {
    headerTitle.value = route.meta.title || 'Product Management Dashboard'
})

const isMenuVisible = ref(false)

const logout = () => {
    localStorage.removeItem('access_token')
    localStorage.removeItem('refresh_token')
    localStorage.removeItem('username')
    localStorage.removeItem('roles') // clear roles too
    router.push('/login')
}
</script>
