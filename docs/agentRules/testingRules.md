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

## Testing Stack

**Unit Tests (JVM)**

- **JUnit 5** - Test framework
- **kotlinx.coroutines.test** - `runTest`, coroutine testing
- **Truth** - Fluent assertions
- **Turbine** - Flow testing

**Instrumented Tests (Device/Emulator)**

- **Compose UI Testing** - UI components
- **Hilt Testing** - `@HiltAndroidTest` for DI
- **AndroidX Test** - Runners and rules

## Quick Patterns

### ViewModel Test

```kotlin
class HomeViewModelTest {
    @get:Rule
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

### Repository Test

```kotlin
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

### Use Case Test

```kotlin
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

### Compose UI Test

```kotlin
@HiltAndroidTest
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

## Database Testing

```kotlin
@RunWith(AndroidJUnit4::class)
class ArticleDaoTest {
    private lateinit var database: GraplaDatabase
    private lateinit var articleDao: ArticleDao
    
    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, GraplaDatabase::class.java)
            .allowMainThreadQueries()  // Test only
            .build()
        articleDao = database.articleDao()
    }
    
    @After
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
- Close database in `@After`
- Test your logic, not Room's

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

### Test Dispatcher Rule

```kotlin
// core/testing/src/main/kotlin/util/TestDispatchers.kt
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}
```

## Naming Conventions

**Files:** `{Name}{Type}Test.kt`

- `HomeViewModelTest.kt`
- `ArticleRepositoryTest.kt`
- `GetArticlesUseCaseTest.kt`

**Test Methods:** Use backticks for readability
```kotlin
@Test
fun `uiState emits loading then success when data loads`() = runTest { }
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

- Use `runTest` for coroutine tests
- Use fakes over mocks (create in `core:testing`)
- Use Truth for assertions
- Use Turbine for Flow testing
- Test public APIs only
- Use descriptive test names with backticks
- Test edge cases (empty, null, errors)
- Keep tests independent

### Don't

- Don't use Mockk unless necessary
- Don't test implementation details
- Don't write flaky tests
- Don't skip coverage after changes
- Don't test framework code (Room, Compose, etc.)
- Don't use `Thread.sleep()` - use `advanceTimeBy()`
- Don't ignore test failures

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
