<template>
  <div>
    <h1>API Test</h1>
    <p>{{ message }}</p>
  </div>
</template>

<script setup>
    import { ref, onMounted } from 'vue'
    import apiService from '../apiService'

    const message = ref('')

    const fetchExampleData = async () => {
        try {
            const response = await apiService.getHelloMessage()
            message.value = response.data
        } catch (error) {
            console.error('Error fetching example data:', error)
        }
    }

    onMounted(() => {
        fetchExampleData()
    })
</script>

<style scoped>
/* Add your styles here */
</style>
