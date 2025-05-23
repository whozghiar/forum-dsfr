<template xmlns="http://www.w3.org/1999/html">
  <!-- Carte contenant la barre d'action -->
  <div class="fr-card fr-card--no-border fr-p-2w fr-mb-2w"
       style="border-radius: 0.5rem; background-color: #2a2a2a; display: flex; justify-content: space-between;align-items: center;">
    <!-- Boutons à gauche -->
    <div class="fr-btns-group fr-btns-group--inline">
      <DsfrButton class="fr-btn" @click="$emit('repondre')">Répondre</DsfrButton>
      
      <DsfrButton secondary @click="$emit('nouveau')">Nouveau sujet</DsfrButton>
      <DsfrButton secondary @click="$emit('liste')">Liste des sujets</DsfrButton>
      <DsfrButton
        :icon="{ name: 'ri-refresh-line', color: 'green', animation: 'spin-pulse', speed: 'fast'}"
        secondary
        @click="$emit('actualiser')">
        Actualiser
      </DsfrButton>
    </div>
  </div>
  
  
  
  <!-- Pagination sous la carte -->
  <DsfrPagination
    :pages="Array.from({ length: totalPages }, (_, i) => i + 1)"
    :current-page="page"
    @update:current-page="$emit('changePage', $event)"
  />

</template>

<script lang="ts">
export default {
  name: 'SujetActionBar',
  props: {
    page: {
      type: Number,
      required: true
    },
    totalPages: {
      type: Number,
      required: true
    }
  }
}
</script>
