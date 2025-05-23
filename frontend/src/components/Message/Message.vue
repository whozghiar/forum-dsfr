<template>
  <div class="fr-card fr-card--no-border fr-mb-3w fr-px-3w fr-py-2w" style="background-color: #2a2a2a; border-radius: 8px;">
    <div class="fr-grid-row fr-grid-row--gutters">

      <!-- Avatar -->
      <div class="fr-col-auto">
        <img
          :src="avatarUrl"
          alt="Avatar"
          class="fr-responsive-img"
          style="border-radius: 50%; width: 48px; height: 48px;"
        />
      </div>

      <!-- Infos + contenu -->
      <div class="fr-col">
        <div class="fr-grid-row fr-grid-row--middle fr-mb-1w">
          <div class="fr-col">
            <div class="fr-text--bold fr-text-sm" style="color: #fff;">{{ message.auteurPseudo }}</div>
            <div class="fr-text--xs" style="color: #aaa;">{{ formatDate(message.dateCreation) }}</div>
          </div>

          <!-- Boutons d'action à droite -->
          <div class="fr-col-auto fr-text--right">
            <button class="fr-btn--icon fr-icon-error-warning-line" title="Signaler" />
          </div>
        </div>

        <!-- Barre de séparation -->
        <hr style="border: none; border-top: 1px solid #444; margin: 0.5rem 0;" />



        <!-- Contenu -->
        <div class="fr-text--sm fr-mt-1w" style="color: #ddd;" v-html="formattedContent" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Message',
  props: {
    message: {
      type: Object,
      required: true
    }
  },
  computed: {
    formattedContent() {
      const contenu = this.message.contenu || ''
      return contenu
        .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
        .replace(/_(.*?)_/g, '<em>$1</em>')
        .replace(/<u>(.*?)<\/u>/g, '<u>$1</u>')
        .replace(/\[(.*?)\]\((.*?)\)/g, '<a href="$2" target="_blank">$1</a>')
        .replace(/^> (.*)$/gm, '<blockquote>$1</blockquote>')
    },
    avatarUrl() {
      return `https://api.dicebear.com/7.x/thumbs/svg?seed=${this.message.auteurPseudo}`
    }
  },
  methods: {
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString('fr-FR', {
        day: '2-digit',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    },
  }
}
</script>
