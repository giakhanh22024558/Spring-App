<template>
  <div class="p-6 bg-white rounded shadow-md mx-auto">
    <a-form :model="form" :rules="rules" @finish="handleSubmit" layout="vertical">

      <!-- Product Code (Generated automatically) -->
      <a-form-item label="Product Code" name="convertProductCode">
        <a-input v-model:value="form.convertProductCode" disabled placeholder="Product code" />
      </a-form-item>

      <!-- Name -->
      <a-form-item label="Name" name="name">
        <a-input v-model:value="form.name" placeholder="Enter product name" />
      </a-form-item>

      <!-- Category -->
      <a-form-item label="Category" name="productCategory">
        <a-select v-model:value="form.productCategory" :options="categories.map(c => ({ label: c.name, value: c.id }))"
          placeholder="Select category" />
      </a-form-item>

      <!-- Unit -->
      <a-form-item label="Unit" name="productUnit">
        <a-select v-model:value="form.productUnit" :options="units.map(u => ({ label: u.name, value: u.id }))"
          placeholder="Select unit" />
      </a-form-item>

      <!-- Description -->
      <a-form-item label="Description" name="description">
        <a-input v-model:value="form.description" placeholder="Enter description" />
      </a-form-item>

      <!-- Quantity -->
      <a-form-item label="Quantity" name="convertQuantity">
        <a-input-number v-model:value="form.convertQuantity" :min="0" class="w-full" />
      </a-form-item>

      <!-- Min Quantity -->
      <a-form-item label="Min Quantity" name="minQuantity">
        <a-input-number v-model:value="form.minQuantity" :min="0" class="w-full" />
      </a-form-item>

      <!-- Buttons -->
      <div class="flex justify-end space-x-4 mt-6">
        <a-button @click="cancelForm">Cancel</a-button>
        <a-button html-type="submit">{{ formAction }}</a-button>
      </div>
    </a-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { message } from 'ant-design-vue'

const router = useRouter()
const route = useRoute()

const formAction = ref('Add')

// Form model
const form = reactive({
  name: '',
  description: '',
  productCategory: null,
  productUnit: null,
  convertQuantity: 0,
  minQuantity: 0,
  convertProductCode: '', // Product code (auto-generated)
})

// Validation rules
const rules = {
  name: [{ required: true, message: 'Please enter product name' }],
  productCategory: [{ required: true, message: 'Please select a category' }],
  productUnit: [{ required: true, message: 'Please select a unit' }],
}

const categories = ref([])
const units = ref([])

const getAuthHeader = () => ({
  headers: {
    Authorization: `Bearer ${localStorage.getItem('access_token')}`,
  },
})

const fetchCategories = async () => {
  const res = await axios.get(
    'http://localhost:9090/store/api/products/categories',
    getAuthHeader()
  )
  categories.value = res.data
}

const fetchUnits = async () => {
  const res = await axios.get(
    'http://localhost:9090/store/api/products/units',
    getAuthHeader()
  )
  units.value = res.data
}

onMounted(async () => {
  await Promise.all([fetchCategories(), fetchUnits()])

  const id = parseInt(route.params.id)
  if (!isNaN(id)) {
    try {
      // Fetch the product by ID from the backend
      const res = await axios.get(`http://localhost:9090/store/api/products/${id}`, getAuthHeader())
      const product = res.data

      // Fill the form with product details
      Object.assign(form, {
        name: product.name,
        description: product.description,
        productCategory: product.productCategory.id,  // Assuming productCategory is an object
        productUnit: product.productUnit.id,  // Assuming productUnit is an object
        convertQuantity: product.convertQuantity,
        minQuantity: product.minQuantity,
        convertProductCode: product.convertProductCode, // Assuming product already has a code
      })

      formAction.value = 'Update'
    } catch (error) {
      console.error('Failed to fetch product details', error)
      message.error('Failed to fetch product details. Please try again.')
    }
  }
})

const handleSubmit = async () => {
  try {

    if (formAction.value === 'Add') {
      const nextProductCode = `P${String(new Date().getTime()).slice(-3)}`  // Auto generate code like P001, P002, etc.
      form.convertProductCode = nextProductCode
    }

    const selectedCategory = categories.value.find(c => c.id === form.productCategory)
    const selectedUnit = units.value.find(u => u.id === form.productUnit)

    const payload = {
      ...form,
      productCategory: selectedCategory,
      productUnit: selectedUnit,
    }

    const token = localStorage.getItem('access_token')
    const config = {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }

    if (formAction.value === 'Add') {
      await axios.post('http://localhost:9090/store/api/products', payload, config)
      message.success('Product added successfully!')
    } else {
      await axios.put(`http://localhost:9090/store/api/products/${route.params.id}`, payload, config)
      message.success('Product updated successfully!')
    }

    router.push('/products')
  } catch (error) {
    console.error(error)
    message.error('Failed to save product. Please try again.')
  }
}

const cancelForm = () => {
  router.push('/products')
}
</script>

