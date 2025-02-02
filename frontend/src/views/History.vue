<template>
  <div class="max-w-4xl mx-auto px-4 py-8">
    <h2 class="text-2xl font-bold mb-8">Calculation History</h2>
    
    <div class="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8" role="region" aria-label="Performance Statistics">
      <div class="bg-white p-6 rounded-lg shadow-md">
        <h3 class="text-gray-600 text-sm font-medium mb-2" id="total-calc">Total Calculations</h3>
        <div class="text-2xl font-bold text-blue-600" aria-labelledby="total-calc">{{ stats.totalCalculations }}</div>
      </div>
      <div class="bg-white p-6 rounded-lg shadow-md">
        <h3 class="text-gray-600 text-sm font-medium mb-2" id="cache-rate">Cache Hit Rate</h3>
        <div class="text-2xl font-bold text-green-600" aria-labelledby="cache-rate">{{ stats.cacheHitRate }}%</div>
      </div>
      <div class="bg-white p-6 rounded-lg shadow-md">
        <h3 class="text-gray-600 text-sm font-medium mb-2" id="avg-time">Average Response Time</h3>
        <div class="text-2xl font-bold text-purple-600" aria-labelledby="avg-time">{{ stats.avgResponseTime }}ms</div>
      </div>
    </div>

    <div v-if="loading" class="flex justify-center py-8">
      <LoadingSpinner />
    </div>
    
    <ErrorAlert v-else-if="error" :message="error" />
    
    <div v-else class="bg-white rounded-lg shadow-md overflow-hidden" role="region" aria-label="Calculation History">
      <div class="overflow-x-auto">
        <table class="min-w-full divide-y divide-gray-200" role="table">
          <thead class="bg-gray-50">
            <tr>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Time</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Input</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Result</th>
              <th scope="col" class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Execution Time</th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="(item, index) in history" :key="index" class="hover:bg-gray-50">
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ formatDate(item.calculationTime) }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.inputValue }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.result }}</td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{{ item.executionTime }}ms</td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import ErrorAlert from '../components/ErrorAlert.vue'

interface HistoryItem {
  inputValue: number
  result: string
  calculationTime: string
  executionTime: number
}

interface Stats {
  totalCalculations: number
  cacheHitRate: number
  avgResponseTime: number
}

const history = ref<HistoryItem[]>([])
const stats = ref<Stats>({
  totalCalculations: 0,
  cacheHitRate: 0,
  avgResponseTime: 0
})
const loading = ref(false)
const error = ref('')

const fetchData = async () => {
  loading.value = true
  error.value = ''
  
  try {
    const [historyResponse, statsResponse] = await Promise.all([
      axios.get('/api/rest/fibonacci/history'),
      axios.get('/api/rest/fibonacci/stats')
    ])
    
    history.value = historyResponse.data
    stats.value = statsResponse.data
  } catch (err: any) {
    error.value = err.response?.data || 'Failed to load history and statistics'
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString()
}

onMounted(fetchData)
</script>
