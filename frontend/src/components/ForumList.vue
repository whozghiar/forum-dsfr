<template>
  <section class="fr-container">
    <h1 class="fr-h2">Liste des forums</h1>

    <div v-if="erreurChargement" class="fr-alert fr-alert--error">
      Une erreur est survenue lors du chargement des forums.
    </div>

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
import type { Forum } from '@/models/Forum'
import { getAllForums } from '@/services/forumService'

export default defineComponent({
  name: 'ForumList',
  setup() {
    const forums = ref<Forum[]>([])
    const erreurChargement = ref(false)

    const chargerForums = async () => {
      try {
        forums.value = await getAllForums()
      } catch (error) {
        console.error(error)
        erreurChargement.value = true
      }
    }

    onMounted(chargerForums)

    return { forums, erreurChargement }
  }
})
</script>

<style scoped>
.fr-card {
  height: 100%;
}
</style>
