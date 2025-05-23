<template>
  <div class="fr-container fr-my-6w">
    <p class="fr-breadcrumb">
      <router-link to="/forums">Tous les forums</router-link> /
      <router-link :to="`/forums/${forumId}/sujets`">Forum {{ forumId }}</router-link> /
      <strong>Topic {{ sujetTitre }}</strong>
    </p>

    <h1 class="fr-h4">Sujet : {{ sujetTitre }}</h1>
    
    <!-- Barre d’actions -->
    <SujetActionBar
      :page="page"
      :totalPages="totalPages"
      @changePage="page = $event"
      @repondre="ouvrirModal(false)"
      @liste="pageRetour"
      @nouveau="ouvrirModal(true)"
      @actualiser="chargerMessages"
    />
    
    <!-- Liste des messages -->
    <Message v-for="message in paginatedMessages" :key="message.messageId" :message="message" />
  
    <!-- Réponse au sujet -->
    <Modal :visible="modalNouveauMessageVisible" @close="fermerModal(false)">
      <MessageForm
        :title="'Répondre à ce sujet'"
        :showTitleField="false"
        @submit="handleNouveauMessage"
      />
    </Modal>
    
    <!-- Nouveau sujet -->
    <Modal :visible="modalNouveauSujetVisible" @close="fermerModal(true)">
      <MessageForm
        :title="'Nouveau Sujet'"
        :showTitleField="true"
        submitButtonText="Poster"
        @submit="handleNouveauSujet"
      />
    </Modal>

  </div>
</template>

<script lang="ts">
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import Modal from '@/components/Modal.vue';
import MessageForm from '@/components/Message/MessageForm.vue';
import {getMessagesBySujetId, creerMessage, creerSujet} from '@/services/apiService';
import type { Message } from '@/models/Message';
import SujetActionBar from "@/components/Sujet/SujetBarreAction.vue";
import modal from "@/components/Modal.vue";

export default {
  
  // Nom du composant
  name: 'Sujet',
  computed: {
    modal() {
      return modal
    }
  },
  
  // Composants enfants utilisés dans ce composant
  components: {SujetActionBar, Modal, MessageForm },

  // Fonction setup pour définir la logique du composant
  setup() {
    
    // Importation des routes et du routeur
    const route = useRoute();
    const router = useRouter();
    
    // Identifiants du forum et du sujet extraits des paramètres de la route
    const forumId = Number(route.params.forumId);
    const sujetId = Number(route.params.sujetId);
    
    // Références réactives pour stocker les données du sujet et des messages
    const sujetTitre = ref(''); // Titre du sujet
    const messages = ref<Message[]>([]); // Liste des messages du sujet
    const page = ref(1); // Numéro de la page actuelle
    const pageSize = 10; // Nombre de messages par page
    const modalNouveauSujetVisible = ref(false); // Indicateur de visibilité du formulaire de création d'un nouveau sujet
    const modalNouveauMessageVisible = ref(false); // Indicateur de visibilité du formulaire de création d'un nouveau Message
    
    // Calcul du nombre total de pages pour la pagination
    const totalPages = computed(() => Math.ceil(messages.value.length / pageSize));
    
    // Messages paginés en fonction de la page actuelle
    const paginatedMessages = computed(() => {
      const start = (page.value - 1) * pageSize;
      return messages.value.slice(start, start + pageSize);
    });

    /**
     * Ouvre le formulaire de réponse
     */
    function ouvrirModal(isNouveauSujet: boolean) {
      if (isNouveauSujet) {
        modalNouveauSujetVisible.value = true;
      } else {
        modalNouveauMessageVisible.value = true;
      }
    }
    
    /**
     * Ferme le formulaire de réponse
     */
    function fermerModal(isNouveauSujet: boolean) {
      if (isNouveauSujet) {
        modalNouveauSujetVisible.value = false;
      } else {
        modalNouveauMessageVisible.value = false;
      }
    }

    /**
     * Retourne à la liste des sujets du forum
     */
    function pageRetour() {
      router.push(`/forums/${forumId}/sujets`);
    }
    
    /**
     * Envoie un nouveau message pour le sujet par le biais de l'API.
     * @param formData - Données du formulaire contenant le titre et le message.
     */
    async function handleNouveauMessage(formData: { title: string; message: string }) {
      await creerMessage(forumId, sujetId, {
        contenu: formData.message,
        auteurId: 1,
      });
      await chargerMessages();
      fermerModal(false);
    }

    /**
     * Fonction pour gérer la création d'un nouveau sujet
     * @param formData
     */
    const handleNouveauSujet = async (formData: { title: string; message: string }) => {
      try {
        const newSujet = await creerSujet(forumId, {
          titre: formData.title,
          auteurId: formData.message, // Auteur à remplacer par l'utilisateur authentifié
          message: formData.message,
        })
        fermerModal(true);
        router.push(`/forums/${forumId}/sujets/${newSujet.idSujet}/messages`);
      } catch (e) {
        console.error('Erreur création sujet', e)
      }
    }
    

    /**
     * Charge les messages du sujet depuis l'API.
     */
    async function chargerMessages() {
      const data = await getMessagesBySujetId(forumId, sujetId);
      messages.value = data;
      sujetTitre.value = data.length ? data[0].titre : 'Sujet';
    }
    
    
    // Charge les messages lors du montage du composant
    onMounted(() => chargerMessages());
    
    // Retourne les propriétés et méthodes pour les utiliser dans le template
    return {
      forumId,
      sujetId,
      sujetTitre,
      messages,
      page,
      pageSize,
      modalNouveauMessageVisible,
      modalNouveauSujetVisible,
      totalPages,
      paginatedMessages,
      ouvrirModal,
      fermerModal,
      pageRetour,
      chargerMessages,
      handleNouveauMessage,
      handleNouveauSujet,
    };
  }
};
</script>
