# Controle de Gastos — Android Expense Tracker

A legacy Android expense-tracking application prototype with Google sign-in, Firebase-backed user data and a place-detection notification service.

> **Status:** Historical prototype last updated in 2018. The Android Gradle Plugin 3.1.3 / Gradle 4.4 toolchain and Android support libraries are obsolete; a clean build on a current toolchain has not been verified.

## Features represented in the source

- **Authentication:** Google Sign-In integrated with Firebase Authentication.
- **Expense list:** loads the signed-in user's expenses from Firebase Realtime Database.
- **Expense creation:** captures a description and monetary amount, applies a currency-input mask and saves a timestamped record.
- **User-scoped data path:** expense records are written under `users/{uid}/despesas`.
- **Place detection:** a background service queries the current place and creates a notification.
- **Boot receiver:** the application registers a receiver to start the location/place service after device boot.

Each expense is represented by a timestamp, value and description.

## Technology

- Java and native Android
- Android support libraries / AppCompat
- Google Sign-In
- Firebase Authentication
- Firebase Realtime Database
- Google Play Services Places and Location
- Android location and notification APIs
- Android Gradle Plugin 3.1.3 and Gradle 4.4
- Compile / target SDK 26

## Project layout

```text
app/src/main/java/com/example/iossenac/controlededespesas/
  MainActivity.java
  LoginActivity.java
  cadastrarDespesa.java
  LocationUpdatesService.java
  model/
    Despesa.java
    DespesaAdapter.java
    Mascara.java
    StartService.java
```

## Running the historical project

The app requires a compatible legacy Android toolchain and your own Google/Firebase projects. Replace project-specific configuration with your own restricted settings before running it. Do not commit local SDK paths, private service credentials or unrestricted API keys.

## Security and privacy

The application handles account identity, expense records and location-related data. Before any reuse, rotate and restrict exposed mapping credentials, review Firebase Realtime Database rules and authentication requirements, verify that users can access only their own records, and obtain clear consent for location access and notifications.

This repository is retained as an example of an earlier Android/Firebase implementation, not as a production-ready financial application.
