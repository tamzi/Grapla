# Architecture Rules

> **For AI Agents:** Architecture patterns and module dependency rules for Grapla

## Clean Architecture Layers

Grapla follows Clean Architecture with three main layers:

```
UI Layer (Presentation)
       ↓
Domain Layer (Business Logic)
       ↓
Data Layer (Data Sources)
```

### Layer Responsibilities

| Layer | Responsibility | Can Depend On |
|-------|---------------|---------------|
| **UI** | ViewModels, Screens, Navigation | Domain, Model, Common |
| **Domain** | Use Cases, Business Logic | Model, Common |
| **Data** | Repositories, Data Sources | Model, Common, Network, Database |

### Dependency Direction

- **UI → Domain → Data** (allowed)
- **Data → UI** (❌ not allowed)
- **Domain → Data** (allowed)

## Module Structure

### Module Categories

```
grapla/
├── app/                          # Application entry point
├── feature/                      # Feature modules (UI layer)
│   ├── home/
│   ├── article/
│   └── productcatalog/
├── core/                         # Core infrastructure
│   ├── ui/                       # Shared UI components (UI layer)
│   ├── domain/                   # Use cases (Domain layer)
│   ├── data/                     # Repositories (Data layer)
│   ├── network/                  # API clients (Data layer)
│   ├── database/                 # Local storage (Data layer)
│   ├── datastore/                # Preferences (Data layer)
│   ├── model/                    # Data models (All layers)
│   ├── common/                   # Utilities (All layers)
│   ├── analytics/                # Analytics tracking
│   └── testing/                  # Test utilities
├── sync/                         # Background processing
│   └── work/                     # WorkManager sync
├── lint/                         # Custom lint rules
└── gruid/                        # Design system (Grapla UI Design)
```

## Module Dependency Rules

### Feature Module Dependencies

✅ **Allowed Dependencies:**

```
feature:home
    ├── core:domain        (use cases)
    ├── core:ui            (shared components)
    ├── core:model         (data models)
    └── core:common        (utilities)
```

❌ **Forbidden Dependencies:**

```
feature:home
    ├── feature:article    ❌ No feature-to-feature dependencies
    ├── core:data          ❌ Use domain layer instead
    ├── core:network       ❌ Use domain layer instead
    └── app                ❌ Features don't know about app
```

### Core Module Dependencies

#### core:domain (Use Cases)

✅ **Allowed:**

```
core:domain
    ├── core:data          (repositories)
    ├── core:model         (data models)
    └── core:common        (utilities)
```

❌ **Forbidden:**

```
core:domain
    ├── feature:*          ❌ Domain doesn't know about UI
    ├── core:ui            ❌ Domain is pure business logic
    ├── core:network       ❌ Use core:data instead
    └── core:database      ❌ Use core:data instead
```

#### core:data (Repositories)

✅ **Allowed:**

```
core:data
    ├── core:network       (API clients)
    ├── core:database      (Room DAOs)
    ├── core:datastore     (Preferences)
    ├── core:model         (data models)
    └── core:common        (utilities)
```

❌ **Forbidden:**

```
core:data
    ├── feature:*          ❌ Data doesn't know about UI
    ├── core:ui            ❌ Data layer is UI-agnostic
    └── core:domain        ❌ Wrong direction
```

#### core:network (API Clients)

✅ **Allowed:**

```
core:network
    ├── core:model         (data models)
    └── core:common        (utilities)
```

❌ **Forbidden:**

```
core:network
    ├── core:data          ❌ Wrong direction
    ├── core:domain        ❌ Network doesn't know about business logic
    └── feature:*          ❌ Network doesn't know about UI
```

### App Module Dependencies

✅ **Allowed:**

```
app
    ├── feature:*          (all feature modules)
    ├── core:ui            (shared components)
    ├── core:model         (data models)
    ├── core:common        (utilities)
    ├── sync:work          (background sync)
    └── dds                (design system)
```

❌ **Forbidden:**

```
app
    ├── core:data          ❌ App doesn't access data directly
    ├── core:network       ❌ App doesn't access network directly
    └── core:database      ❌ App doesn't access database directly
```

## Data Flow Patterns

### Repository Pattern

```
Feature (UI) → Use Case (Domain) → Repository (Data) → Data Source
```

**Example:**

```kotlin
// UI Layer
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val articles by viewModel.articles.collectAsStateWithLifecycle()
}

// Domain Layer
class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    operator fun invoke(): Flow<List<Article>> = repository.getArticles()
}

// Data Layer
class OfflineFirstArticleRepository @Inject constructor(
    private val network: NetworkDataSource,
    private val database: ArticleDao,
) : ArticleRepository {
    override fun getArticles(): Flow<List<Article>> = database
        .getArticlesFlow()
        .map { it.map { entity -> entity.asDomainModel() } }
}
```

### Offline-First Pattern

1. **Expose database as source of truth**
2. **Fetch from network in background**
3. **Update database with network data**
4. **UI observes database changes**

```kotlin
class OfflineFirstRepository @Inject constructor(
    private val network: NetworkDataSource,
    private val database: Dao,
) : Repository {
    
    // 1. Database is source of truth
    override fun getData(): Flow<List<Item>> = database
        .getItemsFlow()
        .map { it.asDomainModel() }
    
    // 2. Sync with network in background
    override suspend fun sync() {
        val networkData = network.getData()
        database.upsert(networkData.asEntity())
    }
}
```

### Use Case Pattern

```kotlin
// Simple use case
class GetArticlesUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    operator fun invoke(): Flow<List<Article>> = repository.getArticles()
}

// Use case with filtering
class GetArticlesByCategoryUseCase @Inject constructor(
    private val repository: ArticleRepository,
) {
    operator fun invoke(category: String): Flow<List<Article>> = 
        repository.getArticles()
            .map { articles -> articles.filter { it.category == category } }
}

// Use case with multiple repositories
class GetUserFeedUseCase @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val userRepository: UserDataRepository,
) {
    operator fun invoke(): Flow<List<Article>> = combine(
        articleRepository.getArticles(),
        userRepository.getUserData(),
    ) { articles, userData ->
        articles.filter { article ->
            article.topics.any { it.id in userData.followedTopics }
        }
    }
}
```

## Model Mapping

### Layer-Specific Models

```
Network Models (core:network)
       ↓  map to
Domain Models (core:model)
       ↓  map to
UI Models (feature modules, if needed)
```

**Example:**

```kotlin
// Network Model (core:network)
@Serializable
data class NetworkArticle(
    val id: String,
    val title: String,
    @SerializedName("publish_date") val publishDate: String,
)

// Domain Model (core:model)
data class Article(
    val id: String,
    val title: String,
    val publishDate: Instant,
)

// Mapper (in core:network)
fun NetworkArticle.asDomainModel() = Article(
    id = id,
    title = title,
    publishDate = Instant.parse(publishDate),
)
```

### Database Entities

```kotlin
// Entity (core:database)
@Entity(tableName = "articles")
data class ArticleEntity(
    @PrimaryKey val id: String,
    val title: String,
    val publishDate: Long,
)

// Mapper (in core:database)
fun ArticleEntity.asDomainModel() = Article(
    id = id,
    title = title,
    publishDate = Instant.fromEpochMilliseconds(publishDate),
)

fun Article.asEntity() = ArticleEntity(
    id = id,
    title = title,
    publishDate = publishDate.toEpochMilliseconds(),
)
```

## State Management

### ViewModel State Pattern

```kotlin
// UI State
sealed interface ArticleUiState {
    object Loading : ArticleUiState
    data class Success(val articles: List<Article>) : ArticleUiState
    data class Error(val message: String) : ArticleUiState
}

// ViewModel
@HiltViewModel
class ArticleViewModel @Inject constructor(
    getArticlesUseCase: GetArticlesUseCase,
) : ViewModel() {
    
    val uiState: StateFlow<ArticleUiState> = getArticlesUseCase()
        .map { articles -> ArticleUiState.Success(articles) }
        .catch { emit(ArticleUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ArticleUiState.Loading,
        )
}
```

### State Hoisting in Compose

```kotlin
// Route (connects ViewModel to Screen)
@Composable
fun ArticleRoute(
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    ArticleScreen(
        uiState = uiState,
        onArticleClick = viewModel::onArticleClick,
    )
}

// Screen (stateless, pure UI)
@Composable
fun ArticleScreen(
    uiState: ArticleUiState,
    onArticleClick: (String) -> Unit,
) {
    when (uiState) {
        is ArticleUiState.Loading -> LoadingIndicator()
        is ArticleUiState.Success -> ArticleList(uiState.articles, onArticleClick)
        is ArticleUiState.Error -> ErrorMessage(uiState.message)
    }
}
```

## Dependency Injection

### Module-Level Organization

```kotlin
// core:data/di/DataModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindsArticleRepository(
        impl: OfflineFirstArticleRepository
    ): ArticleRepository
}

// core:network/di/NetworkModule.kt
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit = // ...
}
```

### Injection Points

```kotlin
// ViewModel injection
@HiltViewModel
class MyViewModel @Inject constructor(
    private val useCase: UseCase,
) : ViewModel()

// Repository injection
class MyRepository @Inject constructor(
    private val network: NetworkDataSource,
    private val database: Dao,
)

// Use case injection
class MyUseCase @Inject constructor(
    private val repository: Repository,
)
```

## Module Boundaries

### What Belongs Where

**Feature Modules:**

- ViewModels
- UI Screens
- Navigation definitions
- Feature-specific UI state

**core:domain:**

- Use cases
- Business logic
- Domain-specific validation
- Complex data transformations

**core:data:**

- Repository interfaces
- Repository implementations
- Data source coordination
- Caching logic

**core:network:**

- Retrofit interfaces
- Network models
- API client configuration
- Fake data sources (for demo)

**core:database:**

- Room database
- DAOs
- Entity models
- Database migrations

**core:model:**

- Domain models (shared across layers)
- Enums
- Value objects

**core:common:**

- Extension functions
- Utility classes
- Result wrapper
- Dispatcher annotations

## Architecture Violations

### Common Mistakes

❌ **Feature depending on feature:**

```kotlin
// feature:home/build.gradle.kts
dependencies {
    implementation(project(":feature:article"))  // ❌ Not allowed
}
```

❌ **Domain depending on data:**

```kotlin
// core:domain
class UseCase @Inject constructor(
    private val networkDataSource: NetworkDataSource,  // ❌ Wrong layer
)
```

❌ **Network depending on repository:**

```kotlin
// core:network
class FakeNetworkDataSource @Inject constructor(
    private val repository: Repository,  // ❌ Wrong direction
)
```

### Correct Patterns

✅ **Feature depends on domain:**

```kotlin
// feature:home/build.gradle.kts
dependencies {
    implementation(project(":core:domain"))     // ✅ Correct
    implementation(project(":core:ui"))         // ✅ Correct
}
```

✅ **Domain depends on data:**

```kotlin
// core:domain
class UseCase @Inject constructor(
    private val repository: Repository,  // ✅ Correct layer
)
```

✅ **Data depends on network:**

```kotlin
// core:data
class Repository @Inject constructor(
    private val network: NetworkDataSource,  // ✅ Correct direction
)
```

## Best Practices

### Do ✅

- Follow Clean Architecture layers strictly
- Keep domain layer pure (no Android dependencies)
- Use repositories as single source of truth
- Map between layer-specific models
- Inject interfaces, not implementations
- Keep feature modules isolated
- Use use cases for business logic
- Expose data as Flow from repositories
- Handle errors at repository level

### Don't ❌

- Mix concerns across layers
- Create circular dependencies
- Depend on concrete implementations
- Access data sources directly from UI
- Put business logic in ViewModels
- Create feature-to-feature dependencies
- Expose mutable state from repositories
- Use platform types (add explicit nullability)
- Skip the domain layer "to save time"

## Verification

### Check Module Dependencies

```bash
# Verify no circular dependencies
./gradlew buildEnvironment

# Check dependency tree
./gradlew :feature:home:dependencies
```

### Enforce with Lint

Custom lint rules in `lint/` module can enforce:

- Feature modules don't depend on each other
- Domain layer has no Android dependencies
- Correct dependency directions

## See Also

- [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Android Architecture Guide](https://developer.android.com/jetpack/guide)

## Related Documentation

- [Coding Standards](./codingStandards.md)
- [Testing Rules](./testingRules.md)
- [Feature Development Rules](./featureDevelopmentRules.md)

