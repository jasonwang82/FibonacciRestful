<template>
  <div class="history">
    <h2>Calculation History</h2>
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

const history = ref<HistoryItem[]>([])

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
})
</script>

<style scoped>
.history {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.history-list {
  margin-top: 20px;
}

.history-item {
  padding: 15px;
  margin-bottom: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.input, .result, .time {
  margin: 5px 0;
}

.time {
  color: #666;
  font-size: 0.9em;
}
</style>
