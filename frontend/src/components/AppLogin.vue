<template>
    <div class="min-h-screen flex items-center justify-center bg-gray-100">
      <div class="bg-white p-8 rounded shadow-md w-full max-w-md">
        <h2 class="text-2xl font-bold mb-6 text-center">Login</h2>
        <form @submit.prevent="handleLogin">
          <div class="mb-4">
            <label class="block text-gray-700">Username</label>
            <input
              v-model="username"
              type="text"
              class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
          </div>
          <div class="mb-6">
            <label class="block text-gray-700">Password</label>
            <input
              v-model="password"
              type="password"
              class="w-full px-4 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
              required
            />
          </div>
          <button
            type="submit"
            class="w-full bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded"
          >
            Login
          </button>
          <p v-if="error" class="text-red-500 text-sm mt-4">{{ error }}</p>
        </form>
      </div>
    </div>
</template>
  
<script setup>
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import axios from 'axios'
  
  const router = useRouter()
  const username = ref('')
  const password = ref('')
  const error = ref(null)
  
  const handleLogin = async () => {
    try {
      const res = await axios.post('', {
        username: username.value,
        password: password.value
      })
      const token = res.data.token
      localStorage.setItem('token', token)
      error.value = null
      router.push('/dashboard')
    } catch (err) {
      error.value = 'Login failed. Please check your credentials.'
    }
  }
</script>