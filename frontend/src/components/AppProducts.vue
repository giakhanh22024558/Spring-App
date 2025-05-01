<template>
    <div class="p-6 bg-white rounded shadow-md">
      <!-- Header -->
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-bold">Product List</h2>
        <a-button @click="handleAdd">Add Product</a-button>
      </div>
  
      <!-- Table -->
      <a-table :columns="columns" :data-source="products" row-key="id" bordered />
  
      <!-- Delete Confirmation Modal -->
      <a-modal
        v-model:open="showModal"
        title="Confirm Deletion"
        @ok="deleteProduct"
        ok-type="danger"
        ok-text="Delete"
        cancel-text="Cancel"
      >
        <p>Are you sure you want to delete <strong>{{ productToDelete?.name }}</strong>?</p>
      </a-modal>
    </div>
</template>
  
<script setup>
  import { ref, h } from 'vue'
  import { message } from 'ant-design-vue'
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  
  const products = ref([
    { id: 1, name: 'Product A', price: 10.99, category: 'Category 1' },
    { id: 2, name: 'Product B', price: 20.0, category: 'Category 2' },
    { id: 3, name: 'Product C', price: 15.5, category: 'Category 3' },
  ])
  
  const showModal = ref(false)
  const productToDelete = ref(null)
  
  const handleAdd = () => {
    // Redirect to add product page
    router.push({ name: 'ProductAdd' })
  }
  
  const editProduct = (record) => {
    message.info(`Edit ${record.name}`)
    router.push({ name: 'ProductUpdate', params: { id: record.id } })
  }
  
  const confirmDelete = (record) => {
    productToDelete.value = record
    showModal.value = true
  }
  
  const deleteProduct = () => {
    products.value = products.value.filter(p => p.id !== productToDelete.value.id)
    showModal.value = false
    message.success('Product deleted successfully')
  }
  
  // Define table columns
  const columns = [
    {
      title: '#',
      key: 'index',
      customRender: ({ index }) => index + 1,
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      customRender: ({ text }) => `$${text.toFixed(2)}`,
    },
    {
      title: 'Category',
      dataIndex: 'category',
      key: 'category',
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
  