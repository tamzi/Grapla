# Testing Rules

> **For AI Agents:** Testing standards and practices for Grapla

## Core Principles

- **Write tests first** or immediately after implementation
- **70%+ coverage** required (enforced by CI)
- **Test behavior, not implementation** - Tests should survive refactoring
- **Keep tests simple** - Complex tests indicate complex code
- **Fast feedback** - Unit tests run in seconds

## Coverage Requirements

| Layer                 | Minimum | Target |
|-----------------------|---------|--------|
| **ViewModels**        | 80%     | 90%+   |
| **Use Cases**         | 90%     | 95%+   |
| **Repositories**      | 80%     | 85%+   |
| **Network/DataStore** | 70%     | 80%+   |
| **UI Screens**        | 60%     | 70%+   |

```bash
./gradlew coverageUnit    # Fast - run after code changes
./gradlew coverageAll     # Full - unit + instrumented
```

## Testing Strategy

This project uses **JUnit 6** (version 6.0.1, released October 2025) for all unit tests.
Instrumented tests use JUnit 4 due to Android limitations.

### Test Types and Frameworks

| Test Type              | Location           | Framework | Import Package                                   | Usage                         |
|------------------------|--------------------|-----------|--------------------------------------------------|-------------------------------|
| **Unit Tests**         | `src/test/`        | JUnit 6   | `org.junit.jupiter.api.*`                        | 90% of tests - fast, isolated |
| **Instrumented Tests** | `src/androidTest/` | JUnit 4   | `org.junit.*` + `@RunWith(AndroidJUnit4::class)` | UI, Android framework only    |

**Why This Strategy?**

- ✅ **JUnit 6 for unit tests** - Modern features, better parameterized tests, nested classes,
  dynamic tests
- ⚠️ **JUnit 4 for instrumented tests** - Android/AndroidX does not officially support JUnit 5/6 yet
- 🎯 **Maximize unit tests** - Faster execution, better test design, full JUnit 6 capabilities
- 🎯 **Minimize instrumented tests** - Use only for UI (Espresso) or true device integration

### Important Notes

1. **Do NOT mix JUnit versions in the same test source set**
    - Unit tests (`src/test/`) → Always use `org.junit.jupiter.api.*`
    - Instrumented tests (`src/androidTest/`) → Always use `org.junit.*` with
      `@RunWith(AndroidJUnit4::class)`

2. **Third-party plugins NOT recommended**
    - The `android-junit5` plugin exists but has limitations
    - Google does not officially support JUnit 5/6 for instrumented tests
    - Keep it simple: JUnit 6 for unit tests, JUnit 4 for instrumented tests

3. **Test Configuration**
    - Unit tests automatically use `useJUnitPlatform()` via convention plugins
    - Instrumented tests use `AndroidJUnitRunner` (configured in `defaultConfig`)

## Testing Stack

**Unit Tests (JVM) - JUnit 6**

- **JUnit 6** - Test framework (`org.junit.jupiter:junit-jupiter:6.0.1`)
- **kotlinx.coroutines.test** - `runTest`, coroutine testing
- **Truth** - Fluent assertions
- **Turbine** - Flow testing
- **Robolectric** - Android framework simulation (when needed)

**Instrumented Tests (Device/Emulator) - JUnit 4**

- **JUnit 4** - Test framework (via `androidx.test.ext:junit`)
- **Compose UI Testing** - UI components
- **Hilt Testing** - `@HiltAndroidTest` for DI
- **AndroidX Test** - Runners and rules
- **Espresso** - UI interactions

## Quick Patterns

### ViewModel Test (JUnit 6)

```kotlin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.RegisterExtension

class HomeViewModelTest {
    @JvmField
    @RegisterExtension
    val mainDispatcherRule = MainDispatcherRule()
    
    private lateinit var fakeArticleRepository: FakeArticleRepository
    private lateinit var viewModel: HomeViewModel
    
    @BeforeEach
    fun setup() {
        fakeArticleRepository = FakeArticleRepository()
        viewModel = HomeViewModel(
            getArticlesUseCase = GetArticlesUseCase(fakeArticleRepository)
        )
    }
    
    @Test
    fun `uiState emits success when articles are loaded`() = runTest {
        // Given
        val articles = listOf(testArticle1, testArticle2)
        fakeArticleRepository.sendArticles(articles)

        // When/Then
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(HomeUiState.Loading)
            assertThat(awaitItem()).isEqualTo(HomeUiState.Success(articles))
        }
    }
}
```

### Repository Test (JUnit 6)

```kotlin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class ArticleRepositoryTest {
    private lateinit var fakeNetworkDataSource: FakeNetworkDataSource
    private lateinit var repository: OfflineFirstArticleRepository
    
    @BeforeEach
    fun setup() {
        fakeNetworkDataSource = FakeNetworkDataSource()
        repository = OfflineFirstArticleRepository(fakeNetworkDataSource)
    }
    
    @Test
    fun `getArticles returns mapped domain models`() = runTest {
        // Given
        val networkArticles = listOf(networkArticle1, networkArticle2)
        fakeNetworkDataSource.sendArticles(networkArticles)
        
        // When
        val result = repository.getArticles().first()
        
        // Then
        assertThat(result).hasSize(2)
        assertThat(result[0].id).isEqualTo(networkArticles[0].id)
    }
}
```

### Use Case Test (JUnit 6)

```kotlin
import org.junit.jupiter.api.Test

class GetArticlesUseCaseTest {
    @Test
    fun `filters articles by category when specified`() = runTest {
        // Given
        val allArticles = listOf(
            testArticle(category = "Fashion"),
            testArticle(category = "Tech"),
        )
        val fakeRepository = FakeArticleRepository().apply {
            sendArticles(allArticles)
        }
        val useCase = GetArticlesUseCase(fakeRepository)
        
        // When
        val result = useCase(filterCategory = "Fashion").first()
        
        // Then
        assertThat(result).hasSize(1)
        assertThat(result[0].category).isEqualTo("Fashion")
    }
}
```

### Coroutine Testing

```kotlin
@Test
fun `suspended function completes successfully`() = runTest {
    val result = repository.syncArticles()
    assertThat(result).isEqualTo(Result.Success)
}

@Test
fun `flow emits expected values`() = runTest {
    repository.getArticles().test {
        assertThat(awaitItem()).isNotEmpty()
        cancelAndIgnoreRemainingEvents()
    }
}

@Test
fun `delayed operations advance virtual time`() = runTest {
    val deferred = async {
        delay(1000)
        "result"
    }
    advanceTimeBy(1000)
    assertThat(deferred.await()).isEqualTo("result")
}
```

### Compose UI Test (JUnit 4 - Instrumented)

```kotlin
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    
    @Before
    fun setup() {
        hiltRule.inject()
    }
    
    @Test
    fun homeScreen_displaysArticles() {
        val articles = listOf(testArticle1, testArticle2)
        
        composeTestRule.setContent {
            GraplaTheme {
                HomeScreen(
                    uiState = HomeUiState.Success(articles),
                    onArticleClick = {}
                )
            }
        }
        
        composeTestRule
            .onNodeWithText(articles[0].title)
            .assertIsDisplayed()
    }
}
```

## Database Testing (JUnit 6 with Robolectric)

```kotlin
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach

class ArticleDaoTest {
    private lateinit var database: GraplaDatabase
    private lateinit var articleDao: ArticleDao

    @BeforeEach
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GraplaDatabase::class.java)
            .allowMainThreadQueries()  // Test only
            .build()
        articleDao = database.articleDao()
    }
    
    @AfterEach
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieve() = runTest {
        val article = createTestArticle(id = "1")
        articleDao.insert(article)
        
        val retrieved = articleDao.getById("1")
        assertEquals(article, retrieved)
    }
}
```

**Key Points:**

- Use in-memory database (fast, isolated)
- `.allowMainThreadQueries()` for tests only
- Close database in `@AfterEach` (JUnit 6)
- Test your logic, not Room's
- Use Robolectric for Android framework dependencies in unit tests

## Test Utilities

### Create Fakes (Preferred)

```kotlin
// core/testing/src/main/kotlin/fake/FakeArticleRepository.kt
class FakeArticleRepository : ArticleRepository {
    private val articlesFlow = MutableStateFlow<List<Article>>(emptyList())
    
    override fun getArticles(): Flow<List<Article>> = articlesFlow
    
    fun sendArticles(articles: List<Article>) {
        articlesFlow.value = articles
    }
}
```

### Test Data Builders

```kotlin
// core/testing/src/main/kotlin/data/TestData.kt
fun testArticle(
    id: String = "test-id",
    title: String = "Test Article",
    category: String = "Tech",
) = Article(
    id = id,
    title = title,
    content = "Test content",
    imageUrl = "https://test.com/image.jpg",
    publishDate = Instant.now(),
    category = category,
)
```

### Test Dispatcher Rule (JUnit 6)

```kotlin
// core/testing/src/main/kotlin/util/TestDispatchers.kt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext

class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : BeforeEachCallback, AfterEachCallback {
    
    override fun beforeEach(context: ExtensionContext?) {
        Dispatchers.setMain(testDispatcher)
    }
    
    override fun afterEach(context: ExtensionContext?) {
        Dispatchers.resetMain()
    }
}

// Usage in tests:
@JvmField
@RegisterExtension
val mainDispatcherRule = MainDispatcherRule()
```

## Naming Conventions

**Files:** `{Name}{Type}Test.kt`

- `HomeViewModelTest.kt` - Unit test (JUnit 6)
- `ArticleRepositoryTest.kt` - Unit test (JUnit 6)
- `GetArticlesUseCaseTest.kt` - Unit test (JUnit 6)
- `HomeScreenTest.kt` - Instrumented test (JUnit 4)

**Test Methods:** Use backticks for readability

```kotlin
// JUnit 6 unit tests
@Test
fun `uiState emits loading then success when data loads`() = runTest { }

// JUnit 4 instrumented tests
@Test
fun homeScreen_displaysArticles() { }
```

## Common Assertions

```kotlin
// Truth
assertThat(value).isEqualTo(expected)
assertThat(value).isNull()
assertThat(list).isEmpty()
assertThat(list).hasSize(2)
assertThat(list).contains(item)
assertThat(boolean).isTrue()

// Turbine (Flow)
flow.test {
    assertThat(awaitItem()).isEqualTo(expected)
    awaitComplete()
    cancelAndIgnoreRemainingEvents()
}
```

## Do's and Don'ts

### Do

- ✅ Use **JUnit 6** (`org.junit.jupiter.api.*`) for unit tests (`src/test/`)
- ✅ Use **JUnit 4** with `@RunWith(AndroidJUnit4::class)` for instrumented tests (
  `src/androidTest/`)
- ✅ Maximize unit tests (fast, modern JUnit 6 features)
- ✅ Minimize instrumented tests (only for UI and true device integration)
- ✅ Use `runTest` for coroutine tests
- ✅ Use fakes over mocks (create in `core:testing`)
- ✅ Use Truth for assertions
- ✅ Use Turbine for Flow testing
- ✅ Test public APIs only
- ✅ Use descriptive test names with backticks
- ✅ Test edge cases (empty, null, errors)
- ✅ Keep tests independent

### Don't

- ❌ Don't mix JUnit versions in the same test source set
- ❌ Don't use JUnit 4 imports in unit tests (`src/test/`)
- ❌ Don't use JUnit 6 in instrumented tests (`src/androidTest/`) - not supported by Android
- ❌ Don't add third-party plugins for JUnit 5/6 instrumented test support
- ❌ Don't use Mockk unless necessary
- ❌ Don't test implementation details
- ❌ Don't write flaky tests
- ❌ Don't skip coverage after changes
- ❌ Don't test framework code (Room, Compose, etc.)
- ❌ Don't use `Thread.sleep()` - use `advanceTimeBy()`
- ❌ Don't ignore test failures

## Pre-Commit Checklist

Before committing:

1. **Build & Test**
   ```bash
   ./gradlew build test coverageUnit
   ```

2. **Review Changes**
   ```bash
   git diff --staged
   ```

3. **Verify:**
    - No duplicated code
    - All imports exist
    - Coverage didn't drop
    - Tests pass

**Related:** [Coding Standards](./codingStandards.md) • [Architecture Rules](./architectureRules.md)
