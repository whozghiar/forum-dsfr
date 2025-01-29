<template>
  <div class="topics-list-container">
    <div class="header">
      <h1>Liste des sujets</h1>
      <div class="controls">
        <!-- Bouton pour ouvrir la modale -->
        <button
          class="fr-btn fr-btn--primary"
          @click="openNewTopicModal"
        >
          Nouveau sujet
        </button>
        <select
          v-model="filter"
          class="fr-select"
        >
          <option value="">
            Sujet
          </option>
          <option value="recent">
            Les plus récents
          </option>
          <option value="popular">
            Les plus populaires
          </option>
        </select>
        <div class="search-bar">
          <input
            v-model="searchQuery"
            type="search"
            class="fr-input"
            placeholder="Rechercher dans le forum"
          >
          <button
            class="fr-btn"
            @click="searchTopics"
          >
            Rechercher
          </button>
        </div>
        <button
          class="fr-btn fr-btn--secondary"
          @click="refreshTopics"
        >
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
      <tr
        v-for="topic in filteredTopics"
        :key="topic.id"
      >
        <td>
            <span
              v-if="topic.pinned"
              class="fr-icon-pin-fill"
              aria-hidden="true"
            />
          <span
            v-else
            :class="{
                'fr-icon-folder-2-fill': true,
                'red-icon': topic.messageCount > 19,
                'yellow-icon': topic.messageCount <= 19
              }"
            aria-hidden="true"
          ></span>
          {{ topic.title }}
        </td>
        <td :class="{ 'author-highlight': topic.isAuthorHighlighted }">
          {{ topic.author }}
        </td>
        <td>{{ topic.messageCount }}</td>
        <td>{{ topic.lastMessage }}</td>
      </tr>
      </tbody>
    </table>

    <!-- Modale contenant le formulaire -->
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

<script>
import Modal from "./Modal.vue";
import MessageForm from "./MessageForm.vue";

export default {
  name: "TopicsList",
  components: {
    Modal,
    MessageForm,
  },
  data() {
    return {
      topics: [
        {
          id: 1,
          title: "[ALERTE] mon chat a bavé sur mon clavier",
          author: "Magique",
          messageCount: 1,
          lastMessage: "27/01/2025",
          pinned: false,
        },
        {
          id: 2,
          title: "Règles du forum",
          author: "Kamoulox",
          messageCount: 0,
          lastMessage: "08/02/2025",
          pinned: false,
          isAuthorHighlighted: false,
        },
        {
          id: 3,
          title: "[BMG] Le racisme ne passera pas",
          author: "BouleDeGomme",
          messageCount: 256,
          lastMessage: "23:10:37",
        },
      ],
      filter: "",
      searchQuery: "",
      isNewTopicModalVisible: false,
    };
  },
  computed: {
    filteredTopics() {
      let filtered = this.topics;

      if (this.searchQuery) {
        filtered = filtered.filter((topic) =>
          topic.title.toLowerCase().includes(this.searchQuery.toLowerCase())
        );
      }

      if (this.filter === "recent") {
        return filtered.sort(
          (a, b) => new Date(b.lastMessage) - new Date(a.lastMessage)
        );
      } else if (this.filter === "popular") {
        return filtered.sort((a, b) => b.messageCount - a.messageCount);
      }

      return filtered;
    },
  },
  methods: {
    openNewTopicModal() {
      this.isNewTopicModalVisible = true;
    },
    closeNewTopicModal() {
      this.isNewTopicModalVisible = false;
    },
    handleNewTopic(newTopicData) {
      console.log("Nouveau sujet créé :", newTopicData);
      this.closeNewTopicModal();

      // Ajouter le sujet dans la liste
      this.topics.push({
        id: this.topics.length + 1,
        title: newTopicData.title,
        author: "Moi",
        messageCount: 0,
        lastMessage: new Date().toISOString().slice(0, 10),
        pinned: false,
      });
    },
    searchTopics() {
      console.log("Recherche de sujets :", this.searchQuery);
    },
    refreshTopics() {
      console.log("Rafraîchir les sujets");
    },
  },
};
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

.author-highlight {
  color: green;
  font-weight: bold;
}

.red-icon {
  color: #e1000f;
}
.yellow-icon {
  color: #fee943;
}
</style>
