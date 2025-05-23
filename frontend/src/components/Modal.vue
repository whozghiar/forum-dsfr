<template>
  <div v-if="visible" class="fr-overlay" @click.self="close">
    <div class="fr-container fr-dialog">
      <slot></slot>
      <button class="fr-btn fr-btn--secondary modal-close-btn" @click="close">Fermer</button>
    </div>
  </div>
</template>

<script>

/**
 * Composant Modal
 * Affiche une boîte de dialogue conditionnelle avec fermeture émise via un événement.
 */
export default {
  // Nom du composant
  name: "Modal",
  // Définition des props pour le composant
  props: {
    
    /**
     * Indique si la boîte de dialogue doit être visible
     * @type {Boolean}
     * @default false
     */
    visible: {
      type: Boolean,
      required: true,
    },
  },
  
  // Méthodes du composant
  methods: {
    /**
     * Ferme la boîte de dialogue
     * Émet un événement "close" pour informer le parent
     */
    close() {
      this.$emit("close");
    },
  },
};
</script>

<style scoped>
.fr-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.fr-dialog {
  background: #1e1e1e;
  padding: 1rem;
  border-radius: 0.5rem;
  max-width: 800px;
  width: 100%;
  box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
}

.modal-close-btn {
  margin-top: 1rem;
}

.modal-close-btn:hover {
  background-color: #e1000f;
  color: #fff;
}
</style>
