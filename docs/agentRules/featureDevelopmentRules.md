# Feature Development Rules

> **For AI Agents:** Guidelines for building feature modules in Grapla

## Feature Module Structure

### Module Organization

```
feature/{feature-name}/
├── build.gradle.kts
└── src/
    ├── main/
    │   └── kotlin/com/tamzi/grapla/feature/{name}/
    │       ├── {Feature}Screen.kt
    │       ├── {Feature}ViewModel.kt
    │       ├── {Feature}UiState.kt
    │       └── navigation/
    │           └── {Feature}Navigation.kt
    └── test/
        └── kotlin/com/tamzi/grapla/feature/{name}/
            └── {Feature}ViewModelTest.kt
```

### Build Configuration

```kotlin
// feature/{name}/build.gradle.kts
plugins {
    alias(libs.plugins.grapla.android.feature)
}

android {
    namespace = "com.tamzi.grapla.feature.{name}"
}

dependencies {
    // Feature plugin provides:
    // - Compose, Hilt, Navigation, Lifecycle, Testing
    
    // Add feature-specific dependencies here
}
```

## ViewModel Pattern

### Structure

- Use `@HiltViewModel` for dependency injection
- Expose UI state as `StateFlow`
- Use `viewModelScope` for coroutines
- Map domain models to UI state
- Handle user intents/actions

### Template

```kotlin
@HiltViewModel
class {Feature}ViewModel @Inject constructor(
    private val getSomethingUseCase: GetSomethingUseCase,
    private val doSomethingUseCase: DoSomethingUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    
    // Extract navigation arguments
    private val argumentId: String = savedStateHandle["id"] ?: ""
    
    // Expose UI state as StateFlow
    val uiState: StateFlow<{Feature}UiState> = 
        getSomethingUseCase(argumentId)
            .map { data -> {Feature}UiState.Success(data) }
            .catch { emit({Feature}UiState.Error(it.message ?: "Unknown error")) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = {Feature}UiState.Loading,
            )
    
    // Handle user actions
    fun onAction(action: {Feature}Action) {
        viewModelScope.launch {
            when (action) {
                is {Feature}Action.ItemClicked -> handleItemClick(action.id)
                is {Feature}Action.Retry -> retry()
            }
        }
    }
    
    private fun handleItemClick(id: String) {
        // Implementation
    }
}
```

## UI State Management

### State Definition

```kotlin
sealed interface {Feature}UiState {
    data object Loading : {Feature}UiState
    
    data class Success(
        val items: List<Item>,
        val selectedItem: Item? = null,
    ) : {Feature}UiState
    
    data class Error(
        val message: String,
    ) : {Feature}UiState
}

// Optional: Separate user actions
sealed interface {Feature}Action {
    data class ItemClicked(val id: String) : {Feature}Action
    data object Retry : {Feature}Action
}
```

### Screen Implementation

```kotlin
@Composable
internal fun {Feature}Route(
    onNavigateToDetail: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: {Feature}ViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    {Feature}Screen(
        uiState = uiState,
        onItemClick = { id -> viewModel.onAction({Feature}Action.ItemClicked(id)) },
        onBackClick = onBackClick,
        modifier = modifier,
    )
}

@Composable
internal fun {Feature}Screen(
    uiState: {Feature}UiState,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is {Feature}UiState.Loading -> LoadingState()
        is {Feature}UiState.Success -> SuccessContent(
            items = uiState.items,
            onItemClick = onItemClick,
        )
        is {Feature}UiState.Error -> ErrorState(
            message = uiState.message,
            onRetry = { /* retry logic */ },
        )
    }
}
```

## Navigation

### Type-Safe Navigation with Kotlin Serialization

```kotlin
// navigation/{Feature}Navigation.kt

@Serializable
object {Feature}Route

// Or with arguments:
@Serializable
data class {Feature}DetailRoute(val id: String)

fun NavGraphBuilder.{feature}Screen(
    onNavigateToDetail: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    composable<{Feature}Route> {
        {Feature}Route(
            onNavigateToDetail = onNavigateToDetail,
            onBackClick = onBackClick,
        )
    }
}

fun NavGraphBuilder.{feature}DetailScreen(
    onBackClick: () -> Unit,
) {
    composable<{Feature}DetailRoute> {
        {Feature}DetailRoute(
            onBackClick = onBackClick,
        )
    }
}
```

### Navigation in App Module

```kotlin
// app/ui/GraplaNavHost.kt
NavHost(
    navController = navController,
    startDestination = HomeRoute,
) {
    {feature}Screen(
        onNavigateToDetail = { id ->
            navController.navigate({Feature}DetailRoute(id))
        },
        onBackClick = { navController.popBackStack() },
    )
    
    {feature}DetailScreen(
        onBackClick = { navController.popBackStack() },
    )
}
```

## Dependency Injection

### ViewModel Injection

```kotlin
@HiltViewModel
class {Feature}ViewModel @Inject constructor(
    private val useCase: UseCase,
    private val repository: Repository,
    savedStateHandle: SavedStateHandle,
) : ViewModel()
```

### Screen-Level Injection

```kotlin
@Composable
internal fun {Feature}Route(
    viewModel: {Feature}ViewModel = hiltViewModel(),
) {
    // ViewModel automatically injected
}
```

## State Collection

### Use collectAsStateWithLifecycle

```kotlin
@Composable
fun {Feature}Route(
    viewModel: {Feature}ViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    {Feature}Screen(uiState = uiState)
}
```

### Don't Use collectAsState

```kotlin
// ❌ Don't do this - not lifecycle-aware
val uiState by viewModel.uiState.collectAsState()

// ✅ Do this instead
val uiState by viewModel.uiState.collectAsStateWithLifecycle()
```

## Feature Module Dependencies

### What Feature Modules Can Depend On

✅ **Allowed:**

- `core:ui` - Shared UI components
- `core:domain` - Use cases
- `core:model` - Data models
- `core:common` - Utilities
- `dds` - Design system

❌ **Not Allowed:**

- Other feature modules
- `core:data` directly (use `core:domain` instead)
- `core:network` directly
- `app` module

### Dependency Declaration

```kotlin
dependencies {
    // Already provided by grapla.android.feature:
    // - Hilt, Compose, Navigation, Lifecycle, Testing
    
    // Add explicit dependencies:
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    // dds is already included via core:ui
}
```

## Best Practices

### Do ✅

- **Use sealed interfaces for UI state** - Type-safe, exhaustive
- **Separate Route and Screen composables** - Route handles ViewModel
- **Use Hilt for all dependencies** - No manual injection
- **Map domain models to UI models** - Keep domain pure
- **Handle loading and error states** - Always show feedback
- **Use collectAsStateWithLifecycle** - Lifecycle-aware collection
- **Keep ViewModels testable** - Inject use cases, not repositories
- **Use SavedStateHandle for arguments** - Type-safe navigation args
- **Write ViewModel tests** - 80%+ coverage target

### Don't ❌

- **Don't put business logic in ViewModels** - That's for use cases
- **Don't access repositories directly** - Use domain layer
- **Don't depend on other feature modules** - Keep features isolated
- **Don't use Context in ViewModels** - Pass resources via use cases
- **Don't create God ViewModels** - Split into multiple if needed
- **Don't forget error handling** - Every network call can fail
- **Don't use collectAsState** - Use lifecycle-aware version
- **Don't skip testing** - ViewModels are critical to test

## Common Patterns

### Loading → Success → Error Pattern

```kotlin
val uiState: StateFlow<UiState> = flow {
    emit(UiState.Loading)
    try {
        val data = repository.getData()
        emit(UiState.Success(data))
    } catch (e: Exception) {
        emit(UiState.Error(e.message))
    }
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)
```

### Combining Multiple Flows

```kotlin
val uiState: StateFlow<UiState> = combine(
    getArticlesUseCase(),
    getProductsUseCase(),
) { articles, products ->
    UiState.Success(
        articles = articles,
        products = products,
    )
}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Loading)
```

### User Action Handling

```kotlin
fun onBookmarkClick(id: String) {
    viewModelScope.launch {
        bookmarkUseCase(id)
    }
}

fun onRefresh() {
    viewModelScope.launch {
        _isRefreshing.value = true
        try {
            syncUseCase()
        } finally {
            _isRefreshing.value = false
        }
    }
}
```

## File Naming Conventions

| Type | Convention | Example |
|------|-----------|---------|
| **Screen** | `{Feature}Screen.kt` | `HomeScreen.kt` |
| **ViewModel** | `{Feature}ViewModel.kt` | `HomeViewModel.kt` |
| **UI State** | `{Feature}UiState.kt` | `HomeUiState.kt` |
| **Navigation** | `{Feature}Navigation.kt` | `HomeNavigation.kt` |
| **Tests** | `{Feature}ViewModelTest.kt` | `HomeViewModelTest.kt` |

## Quick Reference

### Minimal Feature Module Checklist

- [ ] Created module with `grapla.android.feature` plugin
- [ ] Defined UI state sealed interface
- [ ] Created ViewModel with StateFlow
- [ ] Implemented Screen composable
- [ ] Created Route composable with hiltViewModel()
- [ ] Setup type-safe navigation
- [ ] Added navigation to app module
- [ ] Wrote ViewModel tests
- [ ] Verified coverage > 70%

## Related Documentation

- [Architecture Rules](./architectureRules.md)
- [Testing Rules](./testingRules.md)
- [Coding Standards](./codingStandards.md)
