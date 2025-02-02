<template>
  <div class="home">
    <h1>Fibonacci Calculator</h1>
    <div class="input-section">
      <div class="single-calc">
        <input type="number" v-model="number" placeholder="Enter a number" />
        <button @click="calculateFibonacci">Calculate</button>
      </div>
      <div class="batch-calc">
        <input type="text" v-model="batchNumbers" placeholder="Enter numbers separated by commas" />
        <button @click="calculateBatch">Batch Calculate</button>
      </div>
    </div>
    <div v-if="result" class="result">
      <template v-if="typeof result === 'string'">
        Result: {{ result }}
      </template>
      <template v-else>
        <h3>Batch Results:</h3>
        <div v-for="(value, key) in result" :key="key" class="batch-result">
          Input {{ key }}: {{ value }}
        </div>
      </template>
    </div>
    <History class="history-section" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import History from './History.vue'

const number = ref('')
const batchNumbers = ref('')
const result = ref<string | Record<string, string>>('')

const calculateFibonacci = async () => {
  if (!number.value) return
  try {
    const response = await axios.get(`/api/rest/fibonacci/${number.value}`)
    result.value = response.data
  } catch (error) {
    result.value = 'Error occurred during calculation'
  }
}

const calculateBatch = async () => {
  if (!batchNumbers.value) return
  try {
    const numbers = batchNumbers.value.split(',').map(n => parseInt(n.trim())).filter(n => !isNaN(n))
    const response = await axios.post('/api/rest/fibonacci/batch', { numbers })
    result.value = response.data.results
  } catch (error) {
    result.value = 'Error occurred during batch calculation'
  }
}
</script>

<style scoped>
.home {
  max-width: 800px;
  margin: 0 auto;
}

.input-section {
  margin: 20px 0;
}

.single-calc, .batch-calc {
  margin: 10px 0;
  display: flex;
  gap: 10px;
}

input {
  padding: 8px;
  flex: 1;
}

button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

button:hover {
  background-color: #45a049;
}

.result {
  margin: 20px 0;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.batch-result {
  margin: 5px 0;
  padding: 8px;
  background-color: white;
  border-radius: 2px;
}

.history-section {
  margin-top: 40px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}
</style>
