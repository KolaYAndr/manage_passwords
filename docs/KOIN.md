**Koin — Быстрый старт в этом проекте**
docs is ai generated, carefull, may be some missmatches.

- **Файл:** `docs/KOIN.md`

**Коротко**
- В проект добавлен Koin (версия настроена в `gradle/libs.versions.toml`) и подключён в `app/build.gradle.kts`.
- Инициализация Koin делается в `App` (`app/src/main/java/com/kolayandr/passwordmanager/App.kt`).
- Пример модулей находится в `app/src/main/java/com/kolayandr/passwordmanager/di/KoinModules.kt`.

**Что уже добавлено (в репозитории)**
- `App.kt` — старт Koin:
  startKoin { androidContext(this@App); modules(coreModule) }
- `di/KoinModules.kt` — пример `coreModule` с биндингами:
  - `PasswordEncryptor` -> `SimplePasswordEncryptor`
  - `PasswordRepository` -> `InMemoryPasswordRepository`
- Примеры реализаций: `SimplePasswordEncryptor`, `InMemoryPasswordRepository`.

---

**Как добавить свой модуль**
1. Создайте Kotlin-файл для модуля, например `app/src/main/java/com/kolayandr/passwordmanager/di/StorageModule.kt`.
2. Опишите модуль:

```kotlin
import org.koin.dsl.module

val storageModule = module {
    single<MyDao> { MyDaoImpl(get()) }
    single<MyRepo> { MyRepoImpl(get()) }
    // viewModel { MyViewModel(get()) }  // когда появится ViewModel
}
```

3. Зарегистрируйте модуль в `startKoin` (`App.kt`):

```kotlin
startKoin {
  androidContext(this@App)
  modules(listOf(coreModule, storageModule))
}
```

---

**Как инжектить зависимости**
- В Activity/Fragment (Koin-android):

```kotlin
class MainActivity : ComponentActivity() {
  private val repo: PasswordRepository by inject()
}
```

- В Compose (Koin Compose):

```kotlin
import org.koin.androidx.compose.getViewModel
import org.koin.androidx.compose.get

@Composable
fun MyScreen() {
  // получить ViewModel через Koin
  val vm: MyViewModel = getViewModel()
  // или получить простой объект
  val repo: PasswordRepository = get()
}
```

- Если используете Koin viewModel DSL (koin-androidx-viewmodel), вы можете объявлять `viewModel { MyViewModel(get()) }` в модуле и затем получать его через `getViewModel()` в Compose.

---

**Тестирование**
- Для unit-тестов можно инициализировать Koin с тестовым модулем:

```kotlin
@Before
fun setup() {
  startKoin { modules(testModule) }
}

@After
fun tearDown() {
  stopKoin()
}
```

- Для инструментальных тестов используйте `startKoin { androidContext(targetContext); modules(...) }`.

---

**Миграция/расширение (рекомендации)**
- Когда появится Room, создайте `databaseModule` и прокиньте `Room.databaseBuilder(...)` как `single { provideDatabase(androidContext()) }`, DAO как `single { get<Database>().myDao() }`, репозиторий как `single { RoomRepository(get()) }`.
- Для реального шифрования паролей замените `SimplePasswordEncryptor` на реализацию, использующую Android Keystore (оставьте интерфейс `PasswordEncryptor` нетронутым, чтобы можно было подменять реализацию в модуле).
- Используйте `single {}` для синглтонов, `factory {}` для объектов, которые должны создаваться заново при каждом запросе, и `viewModel {}` (Koin) для ViewModel-классов.

---

**Полезные ссылки**
- Официальный сайт Koin: https://insert-koin.io/
- Документация Koin Compose: https://insert-koin.io/docs/reference/koin-androidx-compose

---

Если хочешь — могу:
- добавить пример `viewModel` и показать его подключение через Koin в `MainActivity`/Compose;
- заменить `InMemoryPasswordRepository` на шаблон Room и показать пример модуля для Room.

Файл создан: `docs/KOIN.md` — открой и скажи, нужно ли расширить или добавить примеры под конкретные классы проекта.
