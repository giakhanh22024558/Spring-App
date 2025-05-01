<script setup>
import { useRoute, useRouter } from 'vue-router'
import { ref, watchEffect } from 'vue'

const route = useRoute()
const router = useRouter()

const headerTitle = ref('Product Management Dashboard')  // Default header title
const username = ref(localStorage.getItem('username') || 'Guest') // Get username from localStorage or set as 'Guest'

// Watch route changes and update the header title
watchEffect(() => {
  headerTitle.value = route.meta.title || 'Product Management Dashboard'  // Default fallback title
})

// Logout function
const logout = () => {
  // Clear any stored authentication token and user data
  localStorage.removeItem('token')
  localStorage.removeItem('username')

  // Redirect to login page after logout
  router.push('/login')
}

// Toggle visibility of user menu
const isMenuVisible = ref(false)
</script>

<template>
  <div class="flex h-screen">
    <!-- Sidebar -->
    <aside class="w-64 bg-gray-800 text-white flex flex-col p-4">
      <div class="text-2xl font-bold mb-8">
        Product Manager
      </div>
      <nav class="flex flex-col gap-4">
        <RouterLink to="/" class="hover:text-green-400" active-class="text-green-400 font-semibold">
          Dashboard
        </RouterLink>
        <RouterLink to="/products" class="hover:text-green-400" active-class="text-green-400 font-semibold">
          Products
        </RouterLink>
        <RouterLink to="/categories" class="hover:text-green-400" active-class="text-green-400 font-semibold">
          Categories
        </RouterLink>
      </nav>
    </aside>

    <!-- Main content -->
    <div class="flex-1 flex flex-col">
      <!-- Header -->
      <header class="bg-white shadow-md p-4 flex justify-between items-center">
        <div class="flex items-center gap-4">
          <h1 class="text-xl font-semibold">{{ headerTitle }}</h1>
        </div>
        
        <!-- User info and toggle menu -->
        <div class="flex items-center gap-4 relative">
          <span 
            @click="isMenuVisible = !isMenuVisible" 
            class="text-sm text-gray-700 cursor-pointer">
            Hello, {{ username }}
          </span>

          <!-- User menu -->
          <div v-if="isMenuVisible" 
            class="absolute right-0 top-10 bg-gray-800 text-white text-xs p-2 rounded shadow-lg w-40">
            <a @click="logout" class="block hover:text-green-400 cursor-pointer">Logout</a>
          </div>
        </div>
      </header>

      <!-- Router View -->
      <main class="flex-1 p-6 overflow-auto bg-gray-50">
        <RouterView />
      </main>
    </div>
  </div>
</template>

<style scoped>
/* You can add additional styles here */
</style>
