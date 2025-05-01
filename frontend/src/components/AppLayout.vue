<template>
    <div class="min-h-screen flex flex-col">
      <!-- Header -->
      <header class="bg-blue-600 text-white p-4 shadow-md">
        <h1 class="text-xl font-semibold">Product Management</h1>
      </header>
  
      <!-- Body -->
      <div class="flex flex-1">
        <!-- Sidebar -->
        <aside class="w-64 bg-gray-100 p-4 border-r">
          <nav class="space-y-2">
            <RouterLink to="/dashboard" class="block px-2 py-1 rounded hover:bg-gray-200">Dashboard</RouterLink>
            <RouterLink to="/api-test" class="block px-2 py-1 rounded hover:bg-gray-200">API Test</RouterLink>
            <RouterLink to="/login" class="block px-2 py-1 rounded hover:bg-gray-200">Login</RouterLink>
          </nav>
        </aside>
  
        <!-- Main Content -->
        <main class="flex-1 p-6 overflow-auto">
          <slot />
        </main>
      </div>
    </div>
</template>
  
<script setup>
  import { RouterLink } from 'vue-router'
</script>
  