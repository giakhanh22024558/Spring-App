<template>
  <div class="p-6 bg-white rounded shadow-md">
    <!-- Header -->
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold">Categories list</h2>
      <a-button @click="handleAddCategory">Add Category</a-button>
    </div>

    <!-- Table -->
    <a-table :columns="columns" :data-source="categories" row-key="id" bordered />

    <!-- Delete Confirmation Modal -->
    <a-modal
      v-model:open="showModal"
      title="Confirm Deletion"
      @ok="deleteCategory"
      ok-type="danger"
      ok-text="Delete"
      cancel-text="Cancel"
    >
      <p>Are you sure you want to delete <strong>{{ categoryToDelete?.name }}</strong>?</p>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue'
import axios from 'axios'
import { message } from 'ant-design-vue'

// Data and states
const categories = ref([]) // Categories list
const columns = ref([
  {
    title: '#',
    key: 'index',
    customRender: ({ index }) => index + 1,
  },
  {
    title: 'Category Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Created Time',
    dataIndex: 'createdTime',
    key: 'createdTime',
  },
  {
    title: 'In Use',
    dataIndex: 'inUsed',
    key: 'inUsed',
    customRender: (text) => (text ? 'Active' : 'Inactive'),
  },
  {
    title: 'Actions',
    key: 'actions',
    customRender: ({ record }) => {
      return h('div', { class: 'flex gap-2' }, [
        h(
          'a-button',
          {
            type: 'default',
            size: 'small',
            class: '!text-blue-600 hover:!cursor-pointer hover:!underline hover:!text-blue-800 ',
            onClick: () => editCategory(record),
          },
          'Edit'
        ),
        h(
          'a-button',
          {
            type: 'default',
            size: 'small',
            danger: true,
            class: 'hover:!underline hover:!cursor-pointer text-red-600',
            onClick: () => confirmDelete(record),
          },
          'Delete'
        ),
      ])
    },
  },
])

const showModal = ref(false)
const categoryToDelete = ref(null)
const token = localStorage.getItem('access_token')

// Fetch categories data from API
const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:9090/store/api/products/categories', {
      headers: {
          Authorization: `Bearer ${token}`,
      }
    })
    categories.value = response.data
  } catch (error) {
    console.error('Error fetching categories:', error)
    message.error('Failed to fetch categories data')
  }
}

// Handle Add Category (to be implemented)
const handleAddCategory = () => {
  message.info('Add Category button clicked')
}

// Edit Category
const editCategory = (category) => {
  // Logic to edit the category
  console.log('Editing category:', category)
}

// Confirm delete
const confirmDelete = (category) => {
  categoryToDelete.value = category
  showModal.value = true
}

// Delete Category
const deleteCategory = async () => {
  if (!categoryToDelete.value) return
  try {
    await axios.delete(`http://localhost:9090/store/api/products/categories/${categoryToDelete.value.id}`)
    message.success('Category deleted successfully')
    fetchCategories() // Re-fetch categories after deletion
  } catch (error) {
    message.error('Failed to delete category')
  } finally {
    showModal.value = false
    categoryToDelete.value = null
  }
}

// On mounted, fetch categories
onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
/* Add custom styles here if needed */
</style>
