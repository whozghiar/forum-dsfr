<template>
  <section class="fr-container">
    <h1 class="fr-h2">Liste des forums</h1>
    
    <!-- Chargement des forums -->
    <ChargementSpinner v-if="enChargement"/>
    <ErreurMessage v-if="erreurChargement" message="Une erreur est sruvenue lors du chargement des forums."/>
    
    <div class="fr-grid-row fr-grid-row--gutters fr-mt-5w">
      <div
        v-for="forum in forums"
        :key="forum.idForum"
        class="fr-col-12 fr-col-md-6 fr-col-lg-4"
      >
        <div class="fr-card fr-enlarge-link fr-card--horizontal">
          <div class="fr-card__body">
            <h3 class="fr-card__title">
              <a :href="`/forums/${forum.idForum}/sujets`">{{ forum.titre }}</a>
            </h3>
            <p class="fr-card__desc">{{ forum.description }}</p>
            <p class="fr-card__detail">{{ forum.sujets.length }} sujet(s)</p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from 'vue'
import type { Forum } from '@/models/Forum.ts'
import { getAllForums } from '@/services/apiService.ts'

/**
 * Composant Vue pour afficher la liste des forums.
 * Récupère les données des forums depuis l'API et gère les erreurs de chargement.
 */
export default defineComponent({
  // Nom du composant
  name: 'ForumList',
  setup() {
    /**
     * Liste des forums récupérés depuis l'API.
     * @type {Ref<Forum[]>}
     */
    const forums = ref<Forum[]>([])

    /**
     * Indicateur de chargement des forums.
     * @type {Ref<boolean>}
     */
    const enChargement = ref(true)
    
    /**
     * Indicateur d'erreur lors du chargement des forums.
     * @type {Ref<boolean>}
     */
    const erreurChargement = ref(false)
    
    /**
     * Charge les forums depuis l'API.
     * En cas d'erreur, met à jour l'indicateur d'erreur.
     * @returns {Promise<void>}
     */
    const chargerForums = async () => {
      try {
        forums.value = await getAllForums()
      } catch (error) {
        console.error(error)
        enChargement.value = false
        erreurChargement.value = true
      }finally {
        enChargement.value = false
      }
    }
    
    // Charge les forums lors du montage du composant.
    onMounted(chargerForums)

    return { forums, enChargement, erreurChargement }
  }
})
</script>

<style scoped>
.fr-card {
  height: 100%;
}
</style>
