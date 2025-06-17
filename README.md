
# 💳 Digital Banking Application - Spring Boot & Angular

Ce projet est une application de gestion bancaire digitale développée dans le cadre du cours de M. **Mohamed Youssfi**. Elle implémente une architecture full-stack utilisant **Spring Boot** pour le backend et **Angular** pour le frontend.

---

## 📘 Sommaire

- [📝 Objectif du projet](#📝-objectif-du-projet)
- [🧩 Diagramme de classes](#🧩-diagramme-de-classes)
- [🏛️ Héritage en base de données relationnelle](#🏛️-héritage-en-base-de-données-relationnelle)
- [🛠️ Technologies utilisées](#🛠️-technologies-utilisées)
- [🚀 Lancement du projet](#🚀-lancement-du-projet)

---

## 📝 Objectif du projet

Ce projet a pour but de simuler un système bancaire permettant :

- La gestion des clients,
- La création de comptes courants et comptes épargnes,
- La consultation des opérations (crédit, débit),
- Le suivi des soldes et statuts de comptes.

---

## 🧩 Diagramme de classes

Voici le diagramme de classes principal représentant le cœur métier de l'application :

![Diagramme de classes](https://github.com/user-attachments/assets/61ee1bc0-6b88-44fa-a91e-2639e4c66270)

- Un client peut avoir plusieurs comptes bancaires.
- Un compte bancaire peut être soit un `CurrentAccount` soit un `SavingAccount`.
- Chaque compte est lié à plusieurs opérations (crédit ou débit).
- Les comptes possèdent un statut (`CREATED`, `ACTIVATED`, `SUSPENDED`) et une devise.

---

## 🏛️ Héritage en base de données relationnelle

L'application illustre les différentes stratégies d’héritage entre classes entitées et leur mapping dans une base relationnelle avec **JPA** :

### 1. `SINGLE_TABLE` (Table unique)

- Toutes les classes héritées sont stockées dans une **seule table**.
- Une **colonne discriminante** (souvent appelée `TYPE`) est utilisée pour distinguer le type concret de chaque ligne.
- Cette colonne peut être :
  - **Ordinale** (ex. 0, 1, 2)
  - Ou **chaîne** (ex. `"CURRENT"`, `"SAVING"`)
- Annotée avec `@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)`

> ✅ **Avantage** : Performances élevées en lecture (pas de jointures).
> ❌ **Inconvénient** : Beaucoup de colonnes inutiles (nullable).

### 2. `TABLE_PER_CLASS` (Une table par classe concrète)

- Chaque sous-classe a sa propre table avec toutes les colonnes nécessaires (pas de jointure).
- ❌ **Inconvénient** : Impossible d’écrire des requêtes polies sur la classe parent (union lente).
- ✅ **Utile quand** les classes filles sont très différentes et rarement utilisées ensemble.

### 3. `JOINED` (Table par classe + jointure)

- Chaque entité a sa propre table. Les données sont jointes avec des clés étrangères.
- ✅ **Avantage** : Modèle normalisé, pas de colonnes nulles.
- ❌ **Inconvénient** : Requêtes plus lentes à cause des **jointures coûteuses** (on parle de **coût de cardinalité ou coût de jointure**).

> 🎯 **Recommandation** : Utiliser `JOINED` pour des systèmes complexes et cohérents, `SINGLE_TABLE` pour des lectures fréquentes et simples.

---

## 🛠️ Technologies utilisées

### Backend
- Spring Boot 3.5.0
- Spring Data JPA
- H2 Database (après on passe à MySQL)
- Lombok
- Maven

### Frontend
- Angular 20
- Angular Material / Bootstrap

---
