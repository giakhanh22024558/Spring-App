<template>
  <div class="p-6 bg-white rounded shadow-md mx-auto">
    <!-- Form -->
    <a-form :model="form" @finish="handleSubmit" class="space-y-2" layout="vertical">
      <!-- Name -->
      <a-form-item label="Name" name="name" class="!mb-4">
        <a-input v-model:value="form.name" placeholder="Enter product name" class="w-full" />
      </a-form-item>

      <!-- Category -->
      <a-form-item label="Category" name="category" class="!mb-4">
        <a-input
          v-model:value="form.category"
          placeholder="Enter product category"
          class="w-full"
        />
      </a-form-item>

      <!-- Buttons -->
      <div class="flex justify-end space-x-4 mt-6">
        <a-button @click="cancelForm" class="bg-gray-300 text-gray-700 hover:bg-gray-400">
          Cancel
        </a-button>
        <a-button
          type="primary"
          html-type="submit"
          class="bg-blue-500 text-white hover:bg-blue-600"
        >
          {{ formAction }}
        </a-button>
      </div>
    </a-form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const route = useRoute()
const router = useRouter()

const form = ref({
  name: '',
  category: '',
})

// Mock data source
const products = [
  { id: 1, name: 'Product A', category: 'Category 1' },
  { id: 2, name: 'Product B', category: 'Category 2' },
  { id: 3, name: 'Product C', category: 'Category 3' },
]

const formAction = ref('Add')

onMounted(() => {
  const id = parseInt(route.params.id)
  if (!isNaN(id)) {
    const product = products.find(p => p.id === id)
    if (product) {
      form.value.name = product.name
      form.value.category = product.category
      formAction.value = 'Update'
    }
  }
})

const handleSubmit = () => {
  console.log(`${formAction.value} product:`, form.value)
  // Submit logic here
  router.push('/products')
}

const cancelForm = () => {
  router.push('/products')
}
</script>

<style scoped>
/* You can add custom styles here if necessary */
</style>
