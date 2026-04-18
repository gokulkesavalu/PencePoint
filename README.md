# PencePoint 🪙

PencePoint is a high-quality, offline-first Point of Sale (POS) sample application. It focuses on **Clean Architecture**, **Financial Precision**, and a modern **Declarative UI**.

## 🚀 Key Highlights

- **Financial Precision**: Money is treated as a first-class citizen using a custom `Money` value object. Amounts are stored as `Long` (pence) to avoid floating-point errors common in financial applications.
- **Offline-First**: Implements a robust caching strategy using Room. The application remains functional without an active internet connection by serving data from a local database with a configurable cache timeout.
- **Tax Calculation**: Implements granular tax logic rounded at the `BasketItem` level (Half-Up) to ensure accuracy in multi-item transactions.
- **Clean Architecture**: Strict separation of concerns between `Domain`, `Data`, and `Presentation` layers to ensure testability and maintainability.
- **Modern Tech Stack**: Leveraging the latest stable Android tools including Kotlin 2.0, Compose, Hilt with KSP, and Room.
- **Type-Safe Navigation**: Uses the modern Compose Navigation (v2.8.5) with Kotlin Serialization for compile-time safety across screens.

## 🛠 Tech Stack

- **UI**: Jetpack Compose (Declarative UI) with Material 3
- **Image Loading**: Coil (Optimized for Compose)
- **Dependency Injection**: Hilt with KSP (Kotlin Symbol Processing)
- **Navigation**: Type-safe Navigation Compose with Kotlin Serialization
- **Networking**: Retrofit & OkHttp with FakeStore API integration
- **Persistence**: Room Database (Offline-first caching strategy)
- **Async**: Kotlin Coroutines & Flow
- **Architecture**: MVI/MVVM with Clean Architecture
- **Build System**: Gradle Kotlin DSL with Version Catalogs (`libs.versions.toml`)

## 📂 Project Structure

```text
co.uk.pencepoint
├── data
│   ├── mapper      # Transformation logic between DTOs, Entities, and Domain models
│   ├── remote      # Retrofit API interfaces and DTOs
│   ├── repository  # Implementations of Domain repositories
│   └── local       # Room Database, DAOs, and Entities (Offline Cache)
├── di              # Hilt Modules (Network, Database, Repository, App)
├── domain
│   ├── model       # Pure Domain models (Product, Money, Category)
│   ├── repository  # Repository interfaces (The "What")
│   └── usecase     # Business logic units
└── ui
    ├── navigation  # Type-safe navigation graph and route definitions
    ├── theme       # PencePoint Design System (Colors, Typography, Shapes)
    └── screens     # Feature-based UI (Stateless/Stateful Composables)
```

## 💰 Financial Implementation Details

### The `Money` Class
Instead of `Double` or `Float`, we use a dedicated `Money` data class:
```kotlin
data class Money(
    val amountInPence: Long,
    val currencyCode: String = "GBP"
)
```
This ensures that `0.1 + 0.2` always equals `0.3`, preventing the precision issues inherent in IEEE 754 floating-point math.

### Tax Logic
Taxes are calculated per category (e.g., Electronics: 15%, Food: 5%) and are provided by a `TaxProvider`. Rounding follows the `HALF_UP` strategy at the item level to maintain financial integrity.

## 🎨 UI Patterns

- **Stateless/Stateful Pattern**: Screens are split into a stateful entry point (handling ViewModel injection) and a stateless content block (handling UI layout). This facilitates easy integration with **Compose Previews** and simplifies unit testing.
- **Responsive Layouts**: Screens like `ProductDetail` are designed with accessibility in mind, featuring fixed primary actions (like "Add to Cart") and scrollable content areas.

## ⚙️ Development Setup

1. **Prerequisites**: Android Studio Ladybug (or newer)
2. **JDK**: Version 21
3. **Build**: Run `./gradlew assembleDebug`
4. **StrictMode**: Note that the app will crash in Debug mode if it detects network or disk operations on the main thread.

## 🗺 Roadmap
- [x] Base Architecture & DI Setup
- [x] Domain Modeling & Financial Logic
- [x] Network Layer (FakeStore API)
- [x] Offline Support (Room Integration & Caching)
- [x] Product List & Details UI
- [ ] Cart/Basket Management & UI
- [ ] Checkout Flow & Transaction History
- [ ] Unit & UI Testing Suite
