import { reactive } from 'vue'

export interface InscriptionForm {
  nom_utilisateur: string
  email: string
  password: string
  confirmPassword: string
  acceptTerms: boolean
}

export interface FormErrors {
  nom_utilisateur?: string
  email?: string
  password?: string
  confirmPassword?: string
  acceptTerms?: string
}

export default function useInscriptionValidation(form: InscriptionForm) {
  const errors = reactive<FormErrors>({
    nom_utilisateur: undefined,
    email: undefined,
    password: undefined,
    confirmPassword: undefined,
    acceptTerms: undefined
  })

  function validateUsername() {
    errors.nom_utilisateur = /^[\w-]{3,}$/.test(form.nom_utilisateur)
      ? undefined
      : 'Pseudonyme invalide (min 3 caractères, lettres chiffres tirets)'
  }

  function validateEmail() {
    errors.email = /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)
      ? undefined
      : 'Format d\u2019email invalide'
  }

  function validatePassword() {
    errors.password = /(?=.{8,})(?=.*[A-Z])(?=.*[a-z])(?=.*\d)/.test(form.password)
      ? undefined
      : 'Mot de passe faible'
  }

  function validateConfirmPassword() {
    errors.confirmPassword = form.confirmPassword === form.password
      ? undefined
      : 'Les mots de passe ne correspondent pas'
  }

  function validateAcceptTerms() {
    errors.acceptTerms = form.acceptTerms
      ? undefined
      : 'Vous devez accepter les CGU'
  }

  function validateAll() {
    validateUsername()
    validateEmail()
    validatePassword()
    validateConfirmPassword()
    validateAcceptTerms()
  }

  function isValid() {
    return Object.values(errors).every(e => e === undefined)
  }

  return {
    errors,
    validateUsername,
    validateEmail,
    validatePassword,
    validateConfirmPassword,
    validateAcceptTerms,
    validateAll,
    isValid
  }
}
