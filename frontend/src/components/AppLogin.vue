<template>
  <div class="min-h-screen flex flex-col items-center justify-center bg-gray-100">
    <!-- Page Title -->
    <h1 class="text-2xl font-bold mb-8 text-gray-800">Product Management System</h1>

    <!-- Login Card -->
    <div class="bg-white p-8 rounded shadow-md w-full max-w-lg">
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
          :disabled="loading"
          class="w-full flex justify-center items-center bg-blue-500 hover:bg-blue-600 text-white font-semibold py-2 rounded disabled:opacity-50 disabled:cursor-not-allowed"
        >
          <svg
            v-if="loading"
            class="animate-spin h-5 w-5 mr-2 text-white"
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
          >
            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
            <path
              class="opacity-75"
              fill="currentColor"
              d="M4 12a8 8 0 018-8v4l3-3-3-3v4a8 8 0 00-8 8h4z"
            ></path>
          </svg>
          <span>{{ loading ? 'Logging in...' : 'Login' }}</span>
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
const loading = ref(false)

const handleLogin = async () => {
  loading.value = true
  error.value = null

  try {
    const res = await axios.post('http://localhost:9090/auth/login', {
      username: username.value,
      password: password.value
    })

    const accessToken = res.data.access_token
    const refreshToken = res.data.refresh_token

    const decoded = decodeJWT(accessToken)

    if (decoded) {
      const username = decoded.preferred_username
      const name = decoded.name
      const roles = decoded.resource_access?.['spring-boot-client']?.roles || []

      localStorage.setItem('username', username)
      localStorage.setItem('roles', JSON.stringify(roles)) // Store roles if needed

      console.log('Logged in user:', name)
      console.log('Roles:', roles)
    }

    localStorage.setItem('access_token', accessToken)
    localStorage.setItem('refresh_token', refreshToken)

    router.push('/dashboard')
  } catch (err) {
    console.error(err)
    error.value = 'Login failed. Please check your credentials.'
  } finally {
    loading.value = false
  }
}

function decodeJWT(token) {
  try {
    const base64Url = token.split('.')[1]
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
    const jsonPayload = decodeURIComponent(
      atob(base64)
        .split('')
        .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
        .join('')
    )
    return JSON.parse(jsonPayload)
  } catch (e) {
    console.error('Invalid token', e)
    return null
  }
}

</script>
