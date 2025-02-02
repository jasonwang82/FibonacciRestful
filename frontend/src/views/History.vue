<template>
  <div class="history">
    <h2>Calculation History</h2>
    <div class="dashboard">
      <div class="stat-card">
        <h3>Total Calculations</h3>
        <div class="stat-value">{{ stats.totalCalculations }}</div>
      </div>
      <div class="stat-card">
        <h3>Cache Hit Rate</h3>
        <div class="stat-value">{{ stats.cacheHitRate }}%</div>
      </div>
      <div class="stat-card">
        <h3>Average Response Time</h3>
        <div class="stat-value">{{ stats.avgResponseTime }}ms</div>
      </div>
    </div>
    <div class="history-list">
      <div v-for="(item, index) in history" :key="index" class="history-item">
        <div class="input">Input: {{ item.inputValue }}</div>
        <div class="result">Result: {{ item.result }}</div>
        <div class="time">Time: {{ formatDate(item.calculationTime) }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface HistoryItem {
  inputValue: number
  result: string
  calculationTime: string
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

const fetchStats = async () => {
  try {
    const response = await axios.get('/api/rest/fibonacci/stats')
    stats.value = response.data
  } catch (error) {
    console.error('Error fetching stats:', error)
  }
}

const fetchHistory = async () => {
  try {
    const response = await axios.get('/api/rest/fibonacci/history')
    history.value = response.data
  } catch (error) {
    console.error('Error fetching history:', error)
  }
}

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleString()
}

onMounted(() => {
  fetchHistory()
  fetchStats()
})
</script>

<style scoped>
.history {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.dashboard {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-card h3 {
  margin: 0 0 10px;
  font-size: 1em;
  color: #666;
}

.stat-value {
  font-size: 1.8em;
  font-weight: bold;
  color: #42b983;
}

.history-list {
  margin-top: 20px;
}

.history-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  transition: transform 0.2s;
}

.history-item:hover {
  transform: translateY(-2px);
}

.input, .result, .time {
  margin: 5px 0;
}

.time {
  color: #666;
  font-size: 0.9em;
}
</style>
