<template>
  <div class="topics-list-container">
    <div class="header">
      <h1>Liste des sujets</h1>
      <div class="controls">
        <button class="fr-btn fr-btn--primary" @click="openNewTopicModal">
          Nouveau sujet
        </button>

        <select v-model="filter" class="fr-select">
          <option value="">Sujet</option>
          <option value="recent">Les plus récents</option>
          <option value="popular">Les plus populaires</option>
        </select>

        <div class="search-bar">
          <input
            v-model="searchQuery"
            type="search"
            class="fr-input"
            placeholder="Rechercher dans le forum"
          />
          <button class="fr-btn" @click="searchTopics">Rechercher</button>
        </div>

        <button class="fr-btn fr-btn--secondary" @click="refreshTopics">
          Actualiser
        </button>
      </div>
    </div>

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
      <tr v-for="topic in filteredTopics" :key="topic.id">
        <td>
          <span v-if="topic.pinned" class="fr-icon-pin-fill" aria-hidden="true" />
          <span
            v-else
            :class="{
                'fr-icon-folder-2-fill': true,
                'red-icon': topic.nbMessages > 19,
                'yellow-icon': topic.nbMessages <= 19
              }"
            aria-hidden="true"
          ></span>
          {{ topic.titre }}
        </td>
        <td>{{ topic.auteur.pseudo }}</td>
        <td>{{ topic.nbMessages }}</td>
        <td>{{ topic.dateCreation }}</td>
      </tr>
      </tbody>
    </table>

    <Modal :visible="isNewTopicModalVisible" @close="closeNewTopicModal">
      <MessageForm
        title="Nouveau Sujet"
        :showTitleField="true"
        submitButtonText="Poster"
        @submit="handleNewTopic"
      />
    </Modal>
  </div>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue'
import { useRoute } from 'vue-router'
import Modal from './Modal.vue'
import MessageForm from './MessageForm.vue'
import { creerSujet } from '@/services/sujetService'
import { getSujetsByForumId } from '@/services/forumService'
import type { Sujet } from '@/models/Sujet'

export default defineComponent({
  name: 'TopicList',
  components: { Modal, MessageForm },
  setup() {
    const route = useRoute()
    const forumId = Number(route.params.forumId)
    const topics = ref<Sujet[]>([])
    const filter = ref('')
    const searchQuery = ref('')
    const isNewTopicModalVisible = ref(false)

    const loadTopics = async () => {
      try {
        const data = await getSujetsByForumId(forumId)
        topics.value = data.sujets;
      } catch (error) {
        console.error('Erreur lors du chargement des sujets', error)
      }
    }

    const handleNewTopic = async (formData: { title: string; message: string }) => {
      try {
        const newSujet = await creerSujet(forumId, {
          titre: formData.title,
          contenu: formData.message,
          auteurId: 1 // à remplacer par l'utilisateur authentifié
        })
        topics.value.push(newSujet)
        closeNewTopicModal()
      } catch (e) {
        console.error('Erreur création sujet', e)
      }
    }

    const refreshTopics = () => loadTopics()

    const filteredTopics = computed(() => {
      let filtered = topics.value

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

    const openNewTopicModal = () => (isNewTopicModalVisible.value = true)
    const closeNewTopicModal = () => (isNewTopicModalVisible.value = false)

    const searchTopics = () => {
      console.log('Recherche en cours…')
    }

    onMounted(() => loadTopics())

    return {
      topics,
      filter,
      searchQuery,
      isNewTopicModalVisible,
      openNewTopicModal,
      closeNewTopicModal,
      handleNewTopic,
      refreshTopics,
      searchTopics,
      filteredTopics
    }
  }
})
</script>

<style scoped>
.topics-list-container {
  padding: 1.5rem;
}

.header {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.controls {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.search-bar {
  display: flex;
  gap: 0.5rem;
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
</style>
