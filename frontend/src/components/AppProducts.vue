<template>
  <div class="p-6 bg-white rounded shadow-md">
    <!-- Header -->
    <div class="flex justify-between items-center mb-4">
      <h2 class="text-xl font-bold">Product List</h2>
      <a-button @click="handleAdd">Add Product</a-button>
    </div>

    <!-- Table with loading spinner -->
    <a-table
      :columns="columns"
      :data-source="products"
      row-key="id"
      bordered
      :loading="loading" 
    />

    <!-- Delete Confirmation Modal -->
    <a-modal v-model:open="showModal" title="Confirm Deletion" @ok="deleteProduct" ok-type="danger" ok-text="Delete"
      cancel-text="Cancel">
      <p>Are you sure you want to delete <strong>{{ productToDelete?.name }}</strong>?</p>
    </a-modal>
  </div>
</template>


<script setup>
import { ref, h, onMounted } from 'vue'
import { message } from 'ant-design-vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

const products = ref([])  // Data for the table
const loading = ref(false)  // Loading state for spinner
const showModal = ref(false)
const productToDelete = ref(null)
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

// Token from localStorage
const token = localStorage.getItem('access_token')

// Fetch product list from API
const fetchProducts = async () => {
  loading.value = true  // Start loading when fetching data
  try {
    const res = await axios.get(`${API_BASE_URL}/store/api/products`, {
      headers: {
        Authorization: `Bearer ${token}`,
      }
    })
    products.value = res.data
    console.log(products.value)
  } catch (err) {
    console.error('Failed to fetch products:', err)
  } finally {
    loading.value = false  // Stop loading when data is fetched
  }
}

onMounted(() => {
  fetchProducts()
})

const handleAdd = () => {
  router.push({ name: 'ProductAdd' })
}

const editProduct = (record) => {
  router.push({ name: 'ProductUpdate', params: { id: record.id } })
}

const confirmDelete = (record) => {
  productToDelete.value = record
  showModal.value = true
}

const deleteProduct = async () => {
  try {
    await axios.delete(
      `http://localhost:9090/store/api/products/${productToDelete.value.id}`,
      {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }
    )

    message.success('Product deleted successfully')
    fetchProducts() // Refresh the list
  } catch (err) {
    message.error('Failed to delete product')
    console.error(err)
  } finally {
    showModal.value = false
  }
}

const columns = [
  {
    title: '#',
    key: 'index',
    customRender: ({ index }) => index + 1,
  },
  {
    title: 'Product Code',
    dataIndex: 'convertProductCode',
    key: 'convertProductCode',
  },
  {
    title: 'Product Name',
    dataIndex: 'nameText',
    key: 'name',
  },
  {
    title: 'Category',
    customRender: ({ record }) => record.productCategory?.name || '',
    key: 'productCategory',
  },
  {
    title: 'Unit',
    customRender: ({ record }) => record.productUnit?.name || '',
    key: 'productUnit.name',
  },
  {
    title: 'Quantity',
    dataIndex: 'convertQuantity',
    key: 'convertQuantity',
    scopedSlots: { customRender: 'quantity' },
  },
  {
    title: 'In Use',
    dataIndex: 'inUsed',
    key: 'inUsed',
    customRender: (text) => (text ? 'Yes' : 'No'),
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
            onClick: () => editProduct(record),
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
]
</script>
