# PencePoint 🪙

PencePoint is a high-quality, offline-first Point of Sale (POS) sample application. It focuses on **Clean Architecture** and **Financial Precision**.

## 🚀 Key Highlights

- **Financial Precision**: Money is treated as a first-class citizen using a custom `Money` value object. Amounts are stored as `Long` (pence) to avoid floating-point errors common in financial applications.
- **Tax Calculation**: Implements granular tax logic rounded at the `BasketItem` level (Half-Up) to ensure accuracy in multi-item transactions.
- **Clean Architecture**: Strict separation of concerns between `Domain`, `Data`, and `Presentation` layers to ensure testability and maintainability.
- **Modern Tech Stack**: Leveraging the latest stable Android tools including Kotlin 2.0, Compose, Hilt with KSP, and Room.
- **Proactive Debugging**: Includes `StrictMode` enforcement in debug builds to proactively catch disk/network leaks and threading violations.

## 🛠 Tech Stack

- **UI**: Jetpack Compose (Declarative UI)
- **Dependency Injection**: Hilt with KSP (Kotlin Symbol Processing)
- **Networking**: Retrofit & OkHttp with FakeStore API integration
- **Persistence**: Room Database (Offline-first strategy)
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
│   └── local       # Room Database and DAO (Offline Cache)
├── di              # Hilt Modules (Network, Database, Repository, App)
├── domain
│   ├── model       # Pure Domain models (Product, Money, Basket)
│   ├── repository  # Repository interfaces (The "What")
│   └── usecase     # Business logic units
└── ui              # Presentation layer (Compose screens, ViewModels, Theme)
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
Taxes are calculated per category (e.g., Electronics: 15%, Food: 5%) and are provided by a `TaxProvider`. The rounding happens at the item level to match real-world POS hardware constraints.

## ⚙️ Development Setup

1. **Prerequisites**: Android Studio Ladybug (or newer)
2. **JDK**: Version 21
3. **Build**: Run `./gradlew assembleDebug`
4. **StrictMode**: Note that the app will crash in Debug mode if it detects network or disk operations on the main thread.

## 🗺 Roadmap
- [x] Base Architecture & DI Setup
- [x] Domain Modeling & Financial Logic
- [x] Network Layer (FakeStore API)
- [ ] Offline Support (Room Integration)
- [ ] Cart/Basket UI & Checkout Flow
- [ ] Unit & UI Testing Suite
