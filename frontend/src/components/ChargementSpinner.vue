<!-- src/components/ui/Spinner.vue -->
<template>
  <div class="spinner" role="status" aria-label="Chargement...">
    <svg class="spinner__circle" viewBox="25 25 50 50">
      <circle class="spinner__path" cx="50" cy="50" r="20" fill="none" stroke-width="4" />
    </svg>
  </div>
</template>

<script setup lang="ts">
</script>

<style scoped>
.spinner {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 2rem auto;
  width: 48px;
  height: 48px;
}

.spinner__circle {
  animation: rotate 1s linear infinite;
  width: 100%;
  height: 100%;
}

.spinner__path {
  stroke: #3b87ff;
  stroke-linecap: round;
  animation: dash 1.5s ease-in-out infinite;
}

@keyframes rotate {
  100% {
    transform: rotate(360deg);
  }
}

@keyframes dash {
  0% {
    stroke-dasharray: 1, 150;
    stroke-dashoffset: 0;
  }
  50% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -35;
  }
  100% {
    stroke-dasharray: 90, 150;
    stroke-dashoffset: -124;
  }
}
</style>
