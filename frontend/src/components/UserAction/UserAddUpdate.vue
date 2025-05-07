<template>
    <div class="p-6 bg-white rounded shadow-md mx-auto">
        <a-form :model="form" :rules="rules" @finish="handleSubmit" layout="vertical">
            <!-- Username -->
            <a-form-item label="Username" name="username">
                <a-input v-model:value="form.username" placeholder="Enter username" />
            </a-form-item>

            <!-- Email -->
            <a-form-item label="Email" name="email">
                <a-input v-model:value="form.email" placeholder="Enter email" />
            </a-form-item>

            <!-- First Name -->
            <a-form-item label="First Name" name="firstName">
                <a-input v-model:value="form.firstName" placeholder="Enter first name" />
            </a-form-item>

            <!-- Last Name -->
            <a-form-item label="Last Name" name="lastName">
                <a-input v-model:value="form.lastName" placeholder="Enter last name" />
            </a-form-item>

            <!-- Roles -->
            <a-form-item label="Role" name="role">
                <a-select v-model:value="form.role" placeholder="Select a role" :options="roleOptions" />
            </a-form-item>

            <!-- Password (only for Add mode) -->
            <a-form-item v-if="formAction === 'Add'" label="Password" name="password">
                <a-input-password v-model:value="form.password" placeholder="Enter password" />
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
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

const router = useRouter()
const route = useRoute()

const formAction = ref('Add')

const form = reactive({
    username: '',
    email: '',
    firstName: '',
    lastName: '',
    role: '', // ✅ single string value
    password: '',
})

const rules = {
    username: [{ required: true, message: 'Username is required' }],
    email: [{ required: true, message: 'Email is required' }],
    role: [{ required: true, message: 'Role is required' }], // ✅ changed
    ...(formAction.value === 'Add' && {
        password: [{ required: true, message: 'Password is required' }],
    }),
}

// ✅ Hardcoded role options
const roleOptions = ref([
    { label: 'USER', value: 'USER' },
    { label: 'ADMIN', value: 'ADMIN' },
])

const getAuthHeader = () => ({
    headers: {
        Authorization: `Bearer ${localStorage.getItem('access_token')}`,
    },
})

onMounted(async () => {
    const id = route.params.id
    if (id) {
        try {
            const res = await axios.get(`${API_BASE_URL}/admin/api/users/${id}`, getAuthHeader())
            Object.assign(form, res.data)
            //console.log(id)
            formAction.value = 'Update'
        } catch (err) {
            console.error(err)
            message.error('Failed to load user data')
        }
    }
})

const handleSubmit = async () => {
    try {
        const url = `${API_BASE_URL}/admin/api/users${formAction.value === 'Update' ? `/${route.params.id}` : ''}`
        const method = formAction.value === 'Add' ? 'post' : 'put'

        const payload = { ...form }
        if (formAction.value === 'Update') {
            delete payload.password
        }

        const response = await axios[method](url, payload, getAuthHeader())

        console.log('Response from server:', response.data)
        console.log(payload)

        // Safely show response message or fallback to default
        const successMessage = response?.data?.message || `User ${formAction.value === 'Add' ? 'created' : 'updated'} successfully`
        message.success(successMessage)

        router.push('/users') // Uncomment when ready to navigate
    } catch (err) {
        console.error(err)
        const errorMessage = err?.response?.data?.message || 'Failed to save user'
        message.error(errorMessage)
    }
}

const cancelForm = () => {
    router.push('/users')
}
</script>