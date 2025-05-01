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
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const form = ref({
  name: '',
  category: '',
})

const formAction = ref('Add')

const handleSubmit = () => {
  console.log('Form Submitted:', form.value)
  // Your form submission logic here
}

const cancelForm = () => {
  console.log('Form Cancelled')
  router.push('/products') // 👈 navigate to product list
}
</script>

<style scoped>
/* You can add custom styles here if necessary */
</style>
