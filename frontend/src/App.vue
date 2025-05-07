<!-- src/App.vue -->
<template>
  <component :is="layout">
    <RouterView />
  </component>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import MainLayout from './layouts/MainLayout.vue'
import AuthLayout from './layouts/AuthLayout.vue'

const route = useRoute()

const layout = computed(() => {
  return route.meta.layout === 'auth' ? AuthLayout : MainLayout
})
</script>
