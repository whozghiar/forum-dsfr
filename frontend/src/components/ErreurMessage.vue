<template>
  <div class="fr-alert fr-alert--error" role="alert">
    {{ message }}
  </div>
</template>

<script setup lang="ts">
defineProps<{
  message?: string
}>()
</script>
