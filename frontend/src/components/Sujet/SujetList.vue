<template>
  <div class="sujets-list-container">
    <div class="header">
      <h1>Liste des sujets</h1>
      
      <div class="controls">
        <DsfrButton
          label="Nouveau sujet"
          @click="openNewsujetModal"/>
        <DsfrSelect
          v-model="filter"
          :options="[
            {text:'Les plus populaires',value:'popular'}, 
            {text:'Les plus récents',value:'recent'}
           ]"
        />
      
        <DsfrSearchBar
          v-model="searchQuery"
          :placeholder="'Rechercher dans le forum'"/>
        
        <DsfrButton
          :icon="{ name: 'ri-refresh-line', color: 'green', animation: 'spin-pulse', speed: 'fast'}"
          label="Actualiser"
          :secondary = "true"
          @click="refreshSujets"/>
      </div>
    </div>

    <!-- Tableau style DSFR (mais personnalisé) -->
    <table class="fr-table">
      <thead>
      <tr>
        <th>Sujet</th>
        <th>Auteur</th>
        <th>NB</th>
        <th>Dernier MSG</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="sujet in filteredsujets" :key="sujet.idSujet">
        <td>
          <span v-if="sujet.estEpingle" class="fr-icon-alarm-warning-fill green-icon" aria-hidden="true"/>
          <span
            v-else
            :class="{
                'fr-icon-folder-2-fill': true,
                'red-icon': sujet.nbMessages > 19,
                'yellow-icon': sujet.nbMessages <= 19
              }"
            aria-hidden="true"
          ></span>
          <a :href="`/forums/${forumId}/sujets/${sujet.idSujet}/messages`">{{ sujet.titre }}</a>
        </td>
        <td>{{ sujet.auteur.pseudo }}</td>
        <td>{{ sujet.nbMessages }}</td>
        <td>{{ sujet.dateCreation }}</td>
      </tr>
      </tbody>
    </table>

    <Modal :visible="isNewSujetModalVisible" @close="closeNewsujetModal">
      <MessageForm
        title="Nouveau Sujet"
        :showTitleField="true"
        submitButtonText="Poster"
        @submit="handleNewsujet"
      />
    </Modal>
  </div>
</template>

<script lang="ts">
import {computed, defineComponent, onMounted, ref} from 'vue'
import {useRoute} from 'vue-router'
import Modal from '../Modal.vue'
import MessageForm from '../Message/MessageForm.vue'
import {creerSujet, getSujetsByForumId} from '@/services/apiService'
import type {Sujet} from '@/models/Sujet'

export default defineComponent({
    // Nom du composant
    name: 'SujetList',
    components: {Modal, MessageForm},
    /**
     * Fonction setup pour initialiser le composant
     * @returns {Object} - Propriétés et méthodes du composant
     */
    setup() {
      
      // Récupération de l'ID du forum depuis la route
      const route = useRoute()
      const forumId = Number(route.params.forumId)
      
      /**
       * Liste des sujets du forum
       * @type {Ref<Sujet[]>}
       */
      const sujets = ref<Sujet[]>([])
      
      /**
       * Filtre pour trier les sujets
       * @type {Ref<string>}
       */
      const filter = ref('')
      /**
       * Requête de recherche
       * @type {Ref<string>}
       */
      const searchQuery = ref('')
      
      /**
       * Indicateur de visibilité de la pop-up Modal pour créer un nouveau sujet
       * @type {Ref<boolean>}
       */
      const isNewSujetModalVisible = ref(false)
      
      /**
       * Fonction pour charger les sujets du forum
       * @returns {Promise<void>}
       */
      const chargerSujets = async () => {
        try {
          sujets.value = (await getSujetsByForumId(forumId)).sujets;
        } catch (error) {
          console.error('Erreur lors du chargement des sujets', error)
        }
      }

      /**
       * Fonction pour gérer la création d'un nouveau sujet
       * @param formData
       */
      
      const handleNouveauSujet = async (formData: { title: string; message: string }) => {
        try {
          const newSujet = await creerSujet(forumId, {
            titre: formData.title,
            message: formData.message,
            auteurId: "1" // TODO à remplacer par l'utilisateur authentifié
          })
          sujets.value.push(newSujet)
          closeNewsujetModal()
        } catch (e) {
          console.error('Erreur création sujet', e)
        }
      }

      /**
       * Fonction pour rafraîchir la liste des sujets
       */
      const refreshSujets = () => chargerSujets()

      /**
       * Fonction pour filtrer les sujets en fonction de la recherche et du filtre
       * @returns {Sujet[]}
       */
      const filteredsujets = computed(() => {
        let filtered = sujets.value

        if (searchQuery.value) {
          filtered = filtered.filter((t) =>
            t.titre.toLowerCase().includes(searchQuery.value.toLowerCase())
          )
        }

        if (filter.value === 'recent') {
          return [...filtered].sort(
            (a, b) => new Date(b.dateCreation).getTime() - new Date(a.dateCreation).getTime()
          )
        } else if (filter.value === 'popular') {
          return [...filtered].sort((a, b) => b.nbMessages - a.nbMessages)
        }

        return filtered
      })
      
      /**
       * Fonction pour ouvrir la pop-up Modal
       */
      const openNewsujetModal = () => (isNewSujetModalVisible.value = true)
      /**
       * Fonction pour fermer la pop-up Modal
       */
      const closeNewsujetModal = () => (isNewSujetModalVisible.value = false)
    
      /**
       * Fonction pour rechercher des sujets
       */
      const searchSujets = () => {
        console.log('Recherche en cours…')
      }
      
      // Charger les sujets lors du montage du composant
      onMounted(() => chargerSujets())

      return {
        forumId,
        sujets,
        filter,
        searchQuery,
        isNewSujetModalVisible,
        openNewsujetModal,
        closeNewsujetModal,
        handleNewsujet: handleNouveauSujet,
        refreshSujets,
        searchSujets,
        filteredsujets
      }
    }
  })
</script>

<style scoped>

  .sujets-list-container {
    padding: 1.5rem;
  }

  .header .controls {
    display: flex;
    gap: 1rem;
    align-items: center;
    flex-wrap: wrap;
    margin-top: 1rem;
  }
  .header .fr-select-group,
  .header .fr-input-group {
    margin-bottom: 0.5rem !important;
  }



  .fr-table {
    width: 100%;
    border-collapse: collapse;
  }

  .red-icon {
    color: #e1000f;
  }

  .yellow-icon {
    color: #fee943;
  }
  
  .green-icon{
    color: #68a532;
  }
</style>
