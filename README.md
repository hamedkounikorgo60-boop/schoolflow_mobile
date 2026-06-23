# SchoolFlow Mobile - Application de Suivi Scolaire Parent-Enfant

## Sujet

Application mobile Android permettant aux parents d'élèves de suivre la scolarité de leurs enfants dans un établissement primaire (du CP1 au CM2). L'application consomme une API REST (Laravel) et offre une interface intuitive pour consulter les informations académiques, financières et administratives en temps réel.

**Université Joseph Ki-Zerbo - UFR/SEA**
**Enseignant :** Lionel Marcus G. KABORET
**Année universitaire :** 2025-2026

## Membres du groupe

| Nom | Prénom | Rôle | Email |
|-----|--------|------|-------|
| SORE | Abdoulaye | Développeur principal | |
| KOUNIKORGO | Hamed | Développeur | hamedkounikorgo60@gmail.com |

## Fonctionnalités

### 1. Authentification Parent
- Connexion sécurisée par email et mot de passe
- Déconnexion avec confirmation
- Modification du mot de passe (ancien + nouveau + confirmation)
- Gestion de session avec token Bearer (API Laravel Sanctum)

### 2. Tableau de bord de l'élève
- Affichage des informations de base (nom, prénom, photo, classe, matricule)
- Affichage de la moyenne générale
- Affichage du rang dans la classe (ex: 3ème / 45)
- Résumé des dernières notes obtenues avec code couleur
- Sélection de l'enfant si le parent a plusieurs enfants inscrits

### 3. Consultation des notes
- Liste des matières avec coefficient et moyenne
- Filtrage par trimestre (1er, 2ème, 3ème trimestre)
- Affichage des notes détaillées par matière
- Calcul et affichage des moyennes trimestrielles avec mention

### 4. Suivi des paiements
- Résumé financier : scolarité totale, total payé, reste à payer
- Barre de progression du paiement (pourcentage)
- Historique détaillé des versements effectués
- Consultation et téléchargement du reçu de paiement (PDF)

### 5. Suivi des absences
- Liste des absences de l'élève avec date
- Statut : justifiée / non justifiée (avec code couleur)
- Motifs d'absence (si renseignés)
- Nombre d'heures d'absence
- Compteur total d'absences

### 6. Notifications et annonces
- Réception des annonces de l'école
- Notifications typées : examens, réunions, échéances de paiement, annonces générales
- Indicateur de notification non lue
- Marquage automatique comme lue

## Architecture technique

### Technologies utilisées
- **Langage :** Kotlin
- **SDK Android :** API 24 (Android 7.0) à API 34 (Android 14)
- **Architecture :** MVVM (Model-View-ViewModel)
- **Networking :** Retrofit 2 + OkHttp 4
- **Sérialisation :** Gson
- **Navigation :** Jetpack Navigation Component
- **UI :** Material Design 3 + ViewBinding
- **Images :** Glide 4
- **Async :** Kotlin Coroutines + LiveData
- **Stockage local :** SharedPreferences (session)

### Structure du projet

```
app/src/main/java/com/schoolflow/mobile/
├── SchoolFlowApp.kt              # Application class
├── api/
│   ├── SchoolFlowApi.kt          # Interface Retrofit (endpoints)
│   ├── RetrofitClient.kt         # Configuration HTTP client
│   └── SchoolFlowRepository.kt   # Repository pattern
├── models/
│   ├── Eleve.kt                  # Modèle élève
│   ├── Classe.kt                 # Modèle classe
│   ├── Matiere.kt                # Modèle matière
│   ├── Note.kt                   # Modèle note
│   ├── Paiement.kt               # Modèle paiement + suivi
│   ├── Absence.kt                # Modèle absence
│   ├── Notification.kt           # Modèle notification
│   └── ApiResponse.kt            # Réponses API + requêtes
├── ui/
│   ├── MainActivity.kt           # Activité principale + navigation
│   ├── auth/
│   │   ├── LoginActivity.kt      # Écran de connexion
│   │   ├── ChangePasswordActivity.kt # Changement de mot de passe
│   │   └── AuthViewModel.kt      # ViewModel authentification
│   ├── dashboard/
│   │   ├── DashboardFragment.kt  # Tableau de bord
│   │   └── DashboardViewModel.kt
│   ├── notes/
│   │   ├── NotesFragment.kt      # Liste des matières
│   │   ├── NoteDetailActivity.kt # Notes par matière
│   │   └── NotesViewModel.kt
│   ├── payments/
│   │   ├── PaymentsFragment.kt   # Suivi paiements
│   │   ├── PaymentReceiptActivity.kt # Visualisation reçu
│   │   └── PaymentsViewModel.kt
│   ├── absences/
│   │   ├── AbsencesFragment.kt   # Liste absences
│   │   └── AbsencesViewModel.kt
│   └── notifications/
│       ├── NotificationsFragment.kt # Notifications
│       └── NotificationsViewModel.kt
├── adapters/                      # RecyclerView adapters
│   ├── RecentNoteAdapter.kt
│   ├── MatiereAdapter.kt
│   ├── NoteDetailAdapter.kt
│   ├── PaiementAdapter.kt
│   ├── AbsenceAdapter.kt
│   └── NotificationAdapter.kt
└── utils/
    ├── SessionManager.kt          # Gestion de session
    ├── Resource.kt                # Wrapper Success/Error/Loading
    └── Extensions.kt              # Extensions Kotlin utilitaires
```

### Endpoints API attendus

L'application consomme les endpoints suivants (API REST Laravel) :

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/login` | Connexion parent |
| POST | `/api/logout` | Déconnexion |
| POST | `/api/change-password` | Modification mot de passe |
| GET | `/api/eleves/{id}/dashboard` | Tableau de bord élève |
| GET | `/api/eleves/{id}/matieres` | Liste des matières |
| GET | `/api/eleves/{id}/notes` | Notes (filtrage trimestre/matière) |
| GET | `/api/eleves/{id}/moyennes` | Moyennes trimestrielles |
| GET | `/api/eleves/{id}/paiements` | Suivi des paiements |
| GET | `/paiements/{id}/recu` | Reçu de paiement |
| GET | `/api/eleves/{id}/absences` | Liste des absences |
| GET | `/api/notifications` | Notifications |
| PUT | `/api/notifications/{id}/lue` | Marquer comme lue |

## Installation et configuration

### Prérequis
- Android Studio Hedgehog (2023.1.1) ou supérieur
- JDK 17
- SDK Android 34
- Un appareil ou émulateur Android (API 24+)

### Étapes d'installation

1. **Cloner le dépôt**
```bash
git clone https://github.com/hamedkounikorgo60-boop/schoolflow_mobile.git
cd schoolflow_mobile
```

2. **Ouvrir dans Android Studio**
   - File > Open > sélectionner le dossier `schoolflow_mobile`
   - Attendre la synchronisation Gradle

3. **Configurer l'URL de l'API**
   - Modifier `API_BASE_URL` dans `app/build.gradle` :
   ```groovy
   buildConfigField "String", "API_BASE_URL", "\"http://VOTRE_IP:8000/api/\""
   ```
   - Pour l'émulateur Android : `10.0.2.2` remplace `localhost`

4. **Compiler et exécuter**
```bash
./gradlew assembleDebug
```
   Ou directement via Android Studio : Run > Run 'app'

### Backend Laravel (SchoolFlow)

Cette application mobile est conçue pour fonctionner avec le backend Laravel **SchoolFlow** :
- Dépôt : [github.com/hamedkounikorgo60-boop/schoolflow](https://github.com/hamedkounikorgo60-boop/schoolflow)
- L'API REST doit être configurée et accessible
- Authentification via Laravel Sanctum (tokens Bearer)

## Captures d'écran

Les écrans principaux de l'application :

| Écran | Description |
|-------|-------------|
| Login | Connexion sécurisée avec email/mot de passe |
| Dashboard | Vue d'ensemble : info élève, moyenne, rang, dernières notes |
| Notes | Liste matières, moyennes trimestrielles, détail par matière |
| Paiements | Résumé financier, historique, reçus |
| Absences | Liste absences, motifs, justification |
| Notifications | Annonces école, alertes examens/réunions/paiements |

## Licence

Projet universitaire - Université Joseph Ki-Zerbo, Ouagadougou, Burkina Faso.
