# FX Rates App
**PetSafe Brands Assessment task**

## Table of Contents
- [Features](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop#features)
- [Build](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop#build)
- [Technologies and Architecture](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#technologies-and-architecture)
  - [Overview](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#overview)
  - [Project Structure](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#project-structure)
  - [MVVM Architecture](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#mvvm-architecture)
  - [Networking](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#networking)
  - [Debug Mode](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#debug-mode)
- [Challenges](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#challenges)
- [UI/UX](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#uiux)
  - [General UI State](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#general-ui-state)
  - [FX Rates Screen](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#fx-rates-screen)
  - [Daily FX Rates Screen](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#daily-fx-rates-screen)
  - [Video Demo](https://github.com/AnatoliiChub/PetSafeBrands/tree/develop?tab=readme-ov-file#video-demo)

## Features
- Fetch foreign exchange rates from a remote API.
- Sort exchange rates by date or currency.
- Debug Mode.

## Build
To build the app you need to provide your access key. You should add it to the local.properties file in the root folder.
```API_KEY=YOUR_FIXER_KEY```
Or you can put it directly to app build file, See this field in debug and release build variant:
```      buildConfigField("String", "API_KEY", "\"$apiKey\"")```

Change it to ```buildConfigField("String", "API_KEY", "YOUR_API_KEY")```

## Technologies and Architecture

### Overview
I used a modern stack of technologies: **Jetpack Compose, Kotlin Coroutines, Kotlin Flow, Retrofit and Hilt**.
I prefer to use **Hilt** for dependency injection because it can provide compile-time safety(in comparison with Koin it's an advantage), it supports **Jetpack Compose** and has full integration with Android. Also I have a lot of experience with Dagger 2, since Hilt is based on Dagger 2, it's also an advantage for me. Since I have a **Mock API** in the app it's a good option to set up configuration for different ```BuildConfig```'s.
Despite having a lot of experience with RxJava I used Kotlin coroutines and Flow because in my opinion the RxJava framework has a chance to become deprecated or not maintainable in the next few years.

### Project Structure
- `domain`: Contains the business logic and use cases.
- `data`: Contains repositories and data sources.
- `ui`: Contains UI components and screens.
- `di`: Contains dependency injection modules.
- `navigation` : contains Jetpack Compose navigation implementation. In the latest release it becomes more convenient with Type Safety.

### MVVM Architecture
I've used MVVM architecture for the app. Since Viewmodel does not have any reference to View it will be easy to test it.
I tried to follow 2 key princiiples.
- **State Management**: The ViewModel has only one public field responsible for the state, which is typically a `StateFlow` object(single source of truth).
- **Event Handling**: The ViewModel has only one public method for consuming events, ensuring a unidirectional data flow [Unidirectional data flow](https://developer.android.com/develop/ui/compose/architecture#udf-compose) pattern. This method processes user actions and updates the state accordingly.
I've used use cases to extract and structurize business logic.
I've used Junit 5 for testing and Mockito to mock dependencies. I've written unit tests for a few use cases.

### Networking
The project uses Retrofit and OkHttp for networking.
I've implemented SSL pinning for security reasons. It helps prevent man-in-the-middle attacks.

### Debug Mode
Since the amount of free requests to Fixer API is very limited, I've implemented a ```FakeFxRatesRepository``` which returns json stored in the assets folder.
You can switch to the debug mode by changing MOCK_API field to true in the app gradle file:  
```buildConfigField("Boolean", "MOCK_API", "false")```
```
   debug {
            buildConfigField("String", "API_KEY", "\"$apiKey\"")
            buildConfigField("String", "API_BASE_URL", "\"https://data.fixer.io/api/\"")
            buildConfigField("Boolean", "MOCK_API", "false")   // change to true for debug mode
        }
```
But, **please note** that all the data in this case are hardcoded and can be inconsistent. For example **historical** api always returns  rates for the same day.(See **Challenges** for more information)

## Challenges
Since I've used the free tier of Fixer api, I should say a couple words regarding my solution.
Fixer api has ["timeseries"](https://fixer.io/documentation#timeseries) api which is very suitable for the Daily Fix Rates screen. But Since it's not available in a free package, I was not able to use it.
Instead of this, I've used another one whis is available: [historical](https://fixer.io/documentation#historicalrates) api.
It's not so suitable because it provides a currency rate only for the concrete date, So to receive all the data for 5 days, it should make 5 requests.
I've implemented it asynchronously, so it's not time consuming.
As I understood it was a part of the challenge.

## UI/UX
The project uses Jetpack Compose for the UI layer, which is a modern toolkit for building native Android UI., it allows to make components reusable. I've extracted all ui components to the ```package com.chub.petsafebrands.ui.view```

### General UI State
I've Implemented general ui state for both screens. It includes Loading and Error states.
During fetching the data it displays a loading layout over the screen.
Error layout contains an error message and a "Try again" button.
<div align="center">
<img src="https://github.com/user-attachments/assets/5c0092c5-e5a8-4831-86c3-bb7f5374a5ad" width="300" height="650" />
&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/4e17f210-d0f5-413f-b1d6-256aa6af9f82" width="300" height="650" />
</div>

### FX Rates Screen
Fx Rates screen contains base currency label and amount text input field.
Amount field has validation, it's highlighted by red color when the entered value is invalid.
While the amount is invalid, the amount value for any item in the list below is 0.
Below the base currency information it contains a list of items. Each item is selectable by tap.
When 2 items are selected, a floating action button appears at the bottom of the screen. After clicking on the button it navigates to the Daily FX Rates screen.
Pay attention, if the amount field is invalid, the button does not appear even if 2 items were selected.
<div align="center">
<img src="https://github.com/user-attachments/assets/6f44ec2d-efcd-4c44-bff4-b9f620e1ed2d" width="300" height="650" />
&nbsp;&nbsp;&nbsp;&nbsp;
<img src="https://github.com/user-attachments/assets/d854e7e3-c18b-43d4-ba01-97d96c4766b3" width="300" height="650" />
</div>

### Daily FX Rates Screen
The screen also contains base currency information on the top. Below the information you can see a table.
Since on the mockup each title field contains a drop down icon, I made it sortable. By default iit sorted by date in descending order.
But the user is able to sort by any other column in descending order. To sort it, just click on the column name.
Top bar contains "back" icon to navigate to the previous screen
<div align="center">
<img src="https://github.com/user-attachments/assets/c0714c30-b584-4aaa-844a-13cd98f36ae7" width="300" height="650" />
</div>

### Video Demo

[demo.webm](https://github.com/user-attachments/assets/09beff13-1e65-488f-9481-b186828c309d)

