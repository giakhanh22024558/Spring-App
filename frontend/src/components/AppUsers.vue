<template>
    <div class="p-6 bg-white rounded shadow-md">
        <!-- Header -->
        <div class="flex justify-between items-center mb-4">
            <h2 class="text-xl font-bold">User List</h2>
            <a-button @click="handleAdd">Add User</a-button>
        </div>

        <!-- Table with loading spinner -->
        <a-table :columns="columns" :data-source="users" row-key="id" bordered :loading="loading" />

        <!-- Delete Confirmation Modal -->
        <a-modal v-model:open="showModal" title="Confirm Deletion" @ok="deleteUser" ok-type="danger" ok-text="Delete"
            cancel-text="Cancel">
            <p>Are you sure you want to delete <strong>{{ userToDelete?.username }}</strong>?</p>
        </a-modal>
    </div>
</template>


<script setup>
import { ref, h, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import axios from 'axios'
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

// Router
const router = useRouter()

// State
const users = ref([])  // Data for the table
const loading = ref(false)  // Loading state for spinner
const showModal = ref(false)
const userToDelete = ref(null)
const token = localStorage.getItem('access_token')

// Fetch Users
const fetchUsers = async () => {
    loading.value = true  // Start loading when fetching data
    try {
        const response = await axios.get(`${API_BASE_URL}/admin/api/users`, {
            headers: { Authorization: `Bearer ${token}` },
        })
        users.value = response.data
    } catch (error) {
        console.error('Failed to fetch users:', error)
        message.error('Unable to load users')
    } finally {
        loading.value = false  // Stop loading when data is fetched
    }
}

onMounted(() => {
    fetchUsers()
})

// Add User
const handleAdd = () => {
    router.push({ name: 'UserAdd' })
}

// Edit User
const editUser = (user) => {
    router.push({ name: 'UserUpdate', params: { id: user.id } })
}

// Confirm Delete
const confirmDelete = (user) => {
    userToDelete.value = user
    showModal.value = true
}

// Delete User
const deleteUser = async () => {
    try {
        await axios.delete(`${API_BASE_URL}/admin/api/users/${userToDelete.value.id}`, {
            headers: { Authorization: `Bearer ${token}` },
        })
        message.success('User deleted successfully')
        fetchUsers()  // Refresh the list
    } catch (error) {
        message.error('Failed to delete user')
    } finally {
        showModal.value = false
        userToDelete.value = null
    }
}

// Columns
const columns = [
    {
        title: '#',
        key: 'index',
        customRender: ({ index }) => index + 1,
    },
    {
        title: 'Full Name',
        key: 'name',
        customRender: ({ record }) => {
            const fullName = `${record.firstName || ''} ${record.lastName || ''}`.trim()
            return fullName || 'N/A'
        },
    },
    {
        title: 'Email',
        dataIndex: 'email',
        key: 'email',
    },
    {
        title: 'Username',
        dataIndex: 'username',
        key: 'username',
    },
    {
        title: 'Roles',
        key: 'roles',
        customRender: ({ record }) => record.roles?.[1] || 'None',
    },
    {
        title: 'Actions',
        key: 'actions',
        customRender: ({ record }) => {
            // Skip action buttons for "superadmin"
            if (record.username === 'superadmin') return null

            return h('div', { class: 'flex gap-2' }, [
                h(
                    'a-button',
                    {
                        type: 'default',
                        size: 'small',
                        class: '!text-blue-600 hover:!cursor-pointer hover:!underline hover:!text-blue-800',
                        onClick: () => editUser(record),
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


<style scoped>
/* Optional scoped styles */
</style>