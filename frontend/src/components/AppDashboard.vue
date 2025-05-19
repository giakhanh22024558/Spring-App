<template>
  <div class="p-6 space-y-10">
    <h2 class="text-2xl font-bold text-gray-800">Welcome to the Product Management Dashboard</h2>

    <!-- Products by Category -->
    <div class="bg-white p-4 rounded shadow">
      <h3 class="text-lg font-semibold mb-2">Products by Category</h3>
      <Pie :data="categoryChartData" :options="basicOptions" />
    </div>

    <!-- Low Stock Products -->
    <div class="bg-white p-4 rounded shadow">
      <h3 class="text-lg font-semibold mb-2">Low Stock Products</h3>
      <Bar :data="lowStockData" :options="basicOptions" />
    </div>

    <!-- Monthly Product Additions -->
    <div class="bg-white p-4 rounded shadow">
      <h3 class="text-lg font-semibold mb-2">Monthly Product Additions</h3>
      <Line :data="monthlyAdditionsData" :options="basicOptions" />
    </div>

    <!-- Product Units -->
    <div class="bg-white p-4 rounded shadow">
      <h3 class="text-lg font-semibold mb-2">Product Quantity by Unit</h3>
      <Doughnut :data="unitData" :options="basicOptions" />
    </div>
  </div>
</template>

<script setup>
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
} from 'chart.js'
import { Pie, Bar, Doughnut, Line } from 'vue-chartjs'

// Register ChartJS components
ChartJS.register(
  Title,
  Tooltip,
  Legend,
  ArcElement,
  BarElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement
)

// Sample Chart Data
const categoryChartData = {
  labels: ['Electronics', 'Furniture', 'Groceries'],
  datasets: [
    {
      label: 'Products',
      data: [45, 25, 30],
      backgroundColor: ['#34D399', '#60A5FA', '#F87171'],
    },
  ],
}

const lowStockData = {
  labels: ['Chair', 'Monitor', 'Soap', 'Milk', 'Notebook'],
  datasets: [
    {
      label: 'Quantity',
      data: [4, 2, 5, 3, 1],
      backgroundColor: '#FBBF24',
    },
  ],
}

const monthlyAdditionsData = {
  labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May'],
  datasets: [
    {
      label: 'New Products',
      data: [10, 14, 8, 18, 12],
      borderColor: '#3B82F6',
      backgroundColor: '#BFDBFE',
      tension: 0.4,
    },
  ],
}

const unitData = {
  labels: ['kg', 'pcs', 'liters'],
  datasets: [
    {
      label: 'Quantity',
      data: [120, 300, 90],
      backgroundColor: ['#6EE7B7', '#93C5FD', '#FCA5A5'],
    },
  ],
}

// Chart options
const basicOptions = {
  responsive: true,
  plugins: {
    legend: {
      position: 'top',
    },
  },
}
</script>

<style scoped>
h3 {
  color: #1f2937;
}
</style>
