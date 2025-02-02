<template>
  <div class="home">
    <h1 class="text-3xl font-bold mb-8">Fibonacci Calculator</h1>
    
    <div class="input-section space-y-6">
      <div class="single-calc bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4" id="single-calc-heading">Single Number Calculation</h2>
        <div class="flex gap-4" role="form" aria-labelledby="single-calc-heading">
          <input 
            type="number" 
            v-model="number" 
            placeholder="Enter a number" 
            class="flex-1 px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            :disabled="loading"
            aria-label="Enter a number for Fibonacci calculation"
            min="0"
            max="1000"
            step="1"
          />
          <Button 
            @click="calculateFibonacci" 
            variant="primary"
            :loading="loading"
            :disabled="!number"
          >
            Calculate
          </Button>
        </div>
      </div>

      <div class="batch-calc bg-white p-6 rounded-lg shadow-md">
        <h2 class="text-xl font-semibold mb-4" id="batch-calc-heading">Batch Calculation</h2>
        <div class="flex gap-4" role="form" aria-labelledby="batch-calc-heading">
          <input 
            type="text" 
            v-model="batchNumbers" 
            placeholder="Enter numbers separated by commas (e.g., 1,2,3)" 
            class="flex-1 px-4 py-2 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            :disabled="loading"
            aria-label="Enter comma-separated numbers for batch Fibonacci calculation"
          />
          <Button 
            @click="calculateBatch" 
            variant="secondary"
            :loading="loading"
            :disabled="!batchNumbers"
          >
            Batch Calculate
          </Button>
        </div>
      </div>
    </div>

    <ErrorAlert :message="error" />

    <div v-if="result" class="result mt-6 bg-white p-6 rounded-lg shadow-md" role="region" aria-live="polite">
      <template v-if="typeof result === 'string'">
        <h3 class="text-lg font-semibold mb-2" id="single-result">Result:</h3>
        <div class="text-2xl font-mono" aria-labelledby="single-result">{{ result }}</div>
      </template>
      <template v-else>
        <h3 class="text-lg font-semibold mb-4" id="batch-results">Batch Results:</h3>
        <div class="grid gap-3" role="list" aria-labelledby="batch-results">
          <div v-for="(value, key) in result" :key="key" class="batch-result p-3 bg-gray-50 rounded-md" role="listitem">
            <span class="font-semibold">F({{ key }}) = </span>
            <span class="font-mono">{{ value }}</span>
          </div>
        </div>
      </template>
    </div>

    <History class="history-section mt-8" />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'
import History from './History.vue'
import LoadingSpinner from '../components/LoadingSpinner.vue'
import ErrorAlert from '../components/ErrorAlert.vue'
import Button from '../components/Button.vue'

const number = ref('')
const batchNumbers = ref('')
const result = ref<string | Record<string, string>>('')
const error = ref<string>('')
const loading = ref(false)

const validateInput = (num: number): boolean => {
  if (isNaN(num)) {
    error.value = 'Please enter a valid number'
    return false
  }
  if (!Number.isInteger(num)) {
    error.value = 'Please enter a whole number'
    return false
  }
  if (num < 0) {
    error.value = 'Please enter a non-negative number'
    return false
  }
  if (num > 1000) {
    error.value = 'Number must be less than or equal to 1000'
    return false
  }
  return true
}

const calculateFibonacci = async () => {
  if (!number.value) return
  
  const num = parseInt(number.value)
  if (!validateInput(num)) return
  
  loading.value = true
  error.value = ''
  result.value = ''
  
  try {
    const response = await axios.get(`${import.meta.env.VITE_API_BASE_URL}/api/rest/fibonacci/${num}`)
    result.value = response.data
  } catch (err: any) {
    error.value = err.response?.data || 'Error occurred during calculation'
  } finally {
    loading.value = false
  }
}

const calculateBatch = async () => {
  if (!batchNumbers.value) return
  
  const numbers = batchNumbers.value
    .split(',')
    .map(n => parseInt(n.trim()))
    .filter(n => !isNaN(n))
  
  if (numbers.length === 0) {
    error.value = 'Please enter valid numbers separated by commas'
    return
  }
  
  if (!numbers.every(n => validateInput(n))) return
  
  loading.value = true
  error.value = ''
  result.value = ''
  
  try {
    const response = await axios.post(`${import.meta.env.VITE_API_BASE_URL}/api/rest/fibonacci/batch`, { numbers })
    result.value = response.data.results
  } catch (err: any) {
    error.value = err.response?.data || 'Error occurred during batch calculation'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.home {
  @apply max-w-4xl mx-auto px-4 py-8;
}
</style>
