<template>
  <div class="message-form">
    <h2 class="fr-h2">{{ title }}</h2>
    <div class="form-container">
      <!-- Saisie du titre (optionnelle) -->
      <div v-if="showTitleField" class="form-group">
        <label for="subject-title" class="fr-label">Saisir le titre du sujet</label>
        <input
          id="subject-title"
          class="fr-input"
          type="text"
          placeholder="Titre du sujet"
          v-model="formData.title"
        />
      </div>

      <!-- Zone d'édition avec barre d'outils -->
      <div class="form-group">
        <label for="message-content" class="fr-label">Message</label>
        <div class="editor">
          <div class="toolbar">
            <button class="fr-btn fr-btn--icon-left toolbar-btn" @click="formatText('bold')">
              <i class="fr-icon-bold"></i>
            </button>
            <button class="fr-btn fr-btn--icon-left toolbar-btn" @click="formatText('italic')">
              <i class="fr-icon-italic"></i>
            </button>
            <button class="fr-btn fr-btn--icon-left toolbar-btn" @click="formatText('underline')">
              <i class="fr-icon-highlight"></i>
            </button>
            <button class="fr-btn fr-btn--icon-left toolbar-btn" @click="insertLink">
              <i class="fr-icon-link"></i>
            </button>
            <button class="fr-btn fr-btn--icon-left toolbar-btn" @click="addQuote">
              <i class="fr-icon-quote-line"></i>
            </button>
          </div>
          <textarea
            id="message-content"
            class="fr-textarea dark-textarea"
            rows="10"
            placeholder="Saisissez votre message ici..."
            v-model="formData.message"
            @input="updatePreview"
            @keydown="handleKeyboardShortcuts"
          ></textarea>
        </div>
        <p class="fr-hint-text dark-hint">
          Pour que les discussions restent agréables, nous vous remercions de rester poli en toutes circonstances.
        </p>
      </div>

      <!-- Prévisualisation -->
      <div class="preview">
        <h3 class="fr-h5">Prévisualisation :</h3>
        <div v-html="previewContent" class="preview-content"></div>
      </div>

      <!-- Bouton d'action -->
      <button class="fr-btn fr-btn--primary" @click="submitForm">{{ submitButtonText }}</button>
    </div>
  </div>
</template>

<script>

/**
 * Composant MessageForm
 * Permet la création et la mise en forme d’un message avec aperçu HTML.
 */
export default {
  // Nom du composant
  name: "MessageForm",
  // Définition des props pour le composant
  props: {
    /**
     * Titre affiché au-dessus du formulaire
     * @type {String}
     * @default "Nouveau Sujet"
     */
    title: {
      type: String,
      default: "Nouveau Sujet",
    },
    
    /**
     * Indique si le champ de titre doit être affiché
     * @type {Boolean}
     * @default true
     */
    showTitleField: {
      type: Boolean,
      default: true,
    },
    
    /**
     * Libellé du bouton de soumission
     * @type {String}
     * @default "Poster"
     */
    submitButtonText: {
      type: String,
      default: "Poster",
    },
  },

  /**
   * Données réactives du composant
   * @returns {{formData: {title: string, message: string}, previewContent: string}}
   */
  data() {
    return {
      formData: {
        title: "",
        message: "",
      },
      previewContent: "",
    };
  },
  
  // Méthodes du composant
  methods: {
    /**
     * Soumet le formulaire après validation
     * Émet l’événement `submit` avec les données du formulaire.
     */
    submitForm() {
      if (this.showTitleField && !this.formData.title) {
        alert("Veuillez renseigner un titre pour le sujet.");
        return;
      }
      if (!this.formData.message) {
        alert("Veuillez renseigner un message.");
        return;
      }

      this.$emit("submit", this.formData);

      this.formData.title = "";
      this.formData.message = "";
      this.previewContent = "";
    },

    /**
     * Applique un style Markdown à la sélection (gras, italique, souligné)
     * @param {"bold"|"italic"|"underline"} action Type de formatage à appliquer
     */
    formatText(action) {
      const textarea = document.getElementById("message-content");
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;
      const text = this.formData.message.substring(start, end);

      let formattedText;
      if (action === "bold") {
        formattedText = text.startsWith("**") && text.endsWith("**")
          ? text.slice(2, -2)
          : `**${text}**`;
      } else if (action === "italic") {
        formattedText = text.startsWith("_") && text.endsWith("_")
          ? text.slice(1, -1)
          : `_${text}_`;
      } else if (action === "underline") {
        formattedText = text.startsWith("<u>") && text.endsWith("</u>")
          ? text.slice(3, -4)
          : `<u>${text}</u>`;
      }

      this.formData.message =
        this.formData.message.substring(0, start) +
        formattedText +
        this.formData.message.substring(end);

      this.updatePreview();
    },

    /**
     * Insère un lien Markdown dans le message
     * Utilise un `prompt()` pour récupérer l’URL.
     */
    insertLink() {
      const textarea = document.getElementById("message-content");
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;
      const text = this.formData.message.substring(start, end);
      const url = prompt("Entrez l'URL :", "https://");
      if (url) {
        const linkedText = `[${text || "Lien"}](${url})`;
        this.formData.message =
          this.formData.message.substring(0, start) +
          linkedText +
          this.formData.message.substring(end);
        this.updatePreview();
      }
    },
    
    /**
     * Ajoute une citation Markdown `>` au texte sélectionné
     */
    addQuote() {
      const textarea = document.getElementById("message-content");
      const start = textarea.selectionStart;
      const end = textarea.selectionEnd;
      const text = this.formData.message.substring(start, end);
      const quotedText = text
        .split("\n")
        .map((line) => `> ${line}`)
        .join("\n");

      this.formData.message =
        this.formData.message.substring(0, start) +
        quotedText +
        this.formData.message.substring(end);

      this.updatePreview();
    },

    /**
     * Gère les raccourcis clavier :
     * - Ctrl+B : gras
     * - Ctrl+I : italique
     * - Ctrl+U : lien
     * - Ctrl+Q : citation
     * @param {KeyboardEvent} event
     */
    handleKeyboardShortcuts(event) {
      if (event.ctrlKey) {
        const key = event.key.toLowerCase();
        if (key === "b") {
          event.preventDefault();
          this.formatText("bold");
        } else if (key === "i") {
          event.preventDefault();
          this.formatText("italic");
        } else if (key === "u") {
          event.preventDefault();
          this.insertLink();
        } else if (key === "q") {
          event.preventDefault();
          this.addQuote();
        }
      }
    },

    /**
     * Met à jour l’aperçu HTML du message en transformant le Markdown
     */
    updatePreview() {
      this.previewContent = this.formData.message
        .replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>") // Gras
        .replace(/_(.*?)_/g, "<em>$1</em>") // Italique
        .replace(/<u>(.*?)<\/u>/g, "<u>$1</u>") // Souligné
        .replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>') // Lien
        .replace(
          /^((?:> )+)(.*)$/gm, // Gérer les citations imbriquées
          (_, markers, content) => {
            const level = markers.split('> ').length - 1; // Calcul du niveau de citation
            return `<blockquote style="margin-left: ${level * 10}px; border-left: 4px solid black; padding-left: 10px;">${content.trim()}</blockquote>`;
          }
        );
    }
  },
};
</script>

<style scoped>
  /* Couleurs et styles pour le thème sombre du DSFR */
.message-form {
  padding: 1.5rem;
  background-color: #1e1e1e; /* Couleur de fond sombre DSFR */
  border-radius: 0.5rem; /* Radius conforme au DSFR */
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.2); /* Shadow DSFR */
  color: #f9f8f6; /* Couleur de texte claire DSFR */
}

.form-container {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.editor {
  margin-bottom: 1rem;
}

.toolbar {
  display: flex;
  gap: 0.5rem;
}

.toolbar-btn {
  background-color: #3a3a3a; /* Fond sombre pour les boutons */
  color: #f9f8f6; /* Texte clair */
  border: none;
}

.toolbar-btn:hover {
  background-color: #4a4a4a;
}

.fr-textarea {
  background-color: #2a2a2a; /* Fond sombre pour la zone de texte */
  color: #f9f8f6; /* Texte clair */
  width: 100%;
  padding: 0.5rem;
  margin: 1rem 0;
}

.preview {
  background-color: #333333;
  color: #f9f8f6;
  padding: 1rem;
  border-radius: 0.5rem;
  margin-top: 1rem;
}  
  
.preview blockquote {
  margin: 0;
  padding-left: 10px;
  border-left: 4px solid #000;
  color: #f9f8f6;
  font-style: italic;
}
</style>
