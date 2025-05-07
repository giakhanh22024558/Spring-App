<template>
    <div class="p-6 bg-white rounded shadow-md">
        <!-- Header -->
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-bold">Units List</h2>
            <a-button @click="handleAddUnit">Add Unit</a-button>
        </div>

        <!-- Table -->
        <a-table :columns="columns" :data-source="units" row-key="id" bordered />

        <!-- Delete Confirmation Modal -->
        <a-modal v-model:open="showModal" title="Confirm Deletion" @ok="deleteUnit" ok-type="danger" ok-text="Delete"
            cancel-text="Cancel">
            <p>Are you sure you want to delete <strong>{{ unitToDelete?.name }}</strong>?</p>
        </a-modal>
    </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue'
import axios from 'axios'
import { message } from 'ant-design-vue'
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

// States
const units = ref([])
const showModal = ref(false)
const unitToDelete = ref(null)
const token = localStorage.getItem('access_token')

// Columns
const columns = ref([
    {
        title: '#',
        key: 'index',
        customRender: ({ index }) => index + 1,
    },
    {
        title: 'Unit Name',
        dataIndex: 'name',
        key: 'name',
    },
    {
        title: 'Created Time',
        dataIndex: 'createdTime',
        key: 'createdTime'
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
                        class: '!text-blue-600 hover:!cursor-pointer hover:!underline hover:!text-blue-800',
                        onClick: () => editUnit(record),
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

// Fetch Units
const fetchUnits = async () => {
    try {
        const response = await axios.get(`${API_BASE_URL}/store/api/products/units`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        units.value = response.data
    } catch (error) {
        console.error('Error fetching product units:', error)
        message.error('Failed to fetch product units')
    }
}

// Add Unit
const handleAddUnit = () => {
    message.info('Add Unit button clicked')
}

// Edit Unit
const editUnit = (unit) => {
    console.log('Editing unit:', unit)
}

// Confirm Delete
const confirmDelete = (unit) => {
    unitToDelete.value = unit
    showModal.value = true
}

// Delete Unit
const deleteUnit = async () => {
    if (!unitToDelete.value) return
    try {
        await axios.delete(`http://localhost:9090/store/api/products/units/${unitToDelete.value.id}`, {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
        message.success('Unit deleted successfully')
        fetchUnits()
    } catch (error) {
        message.error('Failed to delete unit')
    } finally {
        showModal.value = false
        unitToDelete.value = null
    }
}

// Load units on component mount
onMounted(() => {
    fetchUnits()
})
</script>

<style scoped>
/* Optional styling */
</style>