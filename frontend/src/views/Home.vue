<template>
  <div class="home">
    <h1>Fibonacci Calculator</h1>
    <div class="input-section">
      <input type="number" v-model="number" placeholder="Enter a number" />
      <button @click="calculateFibonacci">Calculate</button>
    </div>
    <div v-if="result" class="result">
      Result: {{ result }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

const number = ref('')
const result = ref('')

const calculateFibonacci = async () => {
  try {
    const response = await axios.get(`/api/v1/rest/fibonacci/${number.value}`)
    result.value = response.data
  } catch (error) {
    result.value = 'Error occurred during calculation'
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

input {
  padding: 8px;
  margin-right: 10px;
}

button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
}

.result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>
