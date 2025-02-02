<template>
  <button
    :class="[
      'px-6 py-2 rounded-md focus:outline-none focus:ring-2 transition-colors',
      variant === 'primary' ? 'bg-blue-600 text-white hover:bg-blue-700 focus:ring-blue-500' : 'bg-green-600 text-white hover:bg-green-700 focus:ring-green-500',
      'disabled:opacity-50 flex items-center justify-center gap-2'
    ]"
    :disabled="disabled || loading"
    v-bind="$attrs"
  >
    <LoadingSpinner v-if="loading" />
    <slot v-else />
  </button>
</template>

<script setup lang="ts">
import LoadingSpinner from './LoadingSpinner.vue'

defineProps<{
  variant?: 'primary' | 'secondary'
  loading?: boolean
  disabled?: boolean
}>()
</script>
