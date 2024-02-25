# Учебный проект: сайт для конструирования и заказа Тако
Проект основан на книге "Spring в действии" - Крейг Уоллс

## Суть проекта
Сделать сайт для заказа Тако

## Зависимости
- [Spring Boot Starter Web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web/3.2.2)
- [Spring Boot Starter Thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf/3.2.2)
- [Spring Boot DevTools](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-devtools/3.2.2)
- [Project Lombok](https://mvnrepository.com/artifact/org.projectlombok/lombok/1.18.30)
- [Spring Boot Starter Test](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test/3.2.2)
- [Spring Boot Starter Validation](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation/3.2.2)
- [Spring Boot Starter Data Cassandra](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-cassandra/3.2.3)

## Версии

### Версия 2.1
#### Что изменилось:
- Сменил БД на Cassandra

### Версия 2.0
#### Что изменилось:
- Данные перенес из мапы в базу данных H2 (In memory)
- Подключение к БД через Spring Boot Starter Data JPA
- Загрузка статичных данных (список ингридиентов) реализована через DataLoaderConfig
- Взаимодействие с БД организовано через репозитории

## Структура БД

![image](https://github.com/bmsalikhov/taco/assets/153372291/148a2622-5f0c-4980-a184-bef299f00f84)


## Пакеты

### controller

- #### DesignTacoController
  Контроллер для составления своего тако

  ##### Поля:
  - private final IngredientRepository ingredientRepository - инжектим репозиторий с ингридиентами

  ##### Аннотации:
  - @Slf4j - логирование
  - @Controller - указываем Spring, что это контроллер 
  - @RequestMapping("/design") - данный контроллер обрабатывает запросы по адресу: .../design
  - @SessionAttributes("tacoOrder") - объект TacoOrder, объявленный в классе чуть ниже, должен поддерживаться на уровне сеанса

  ##### Методы:
  - public void addIngredientsToModel(Model model) - добавление списка ингредиентов в модель для вывода на страницу
  - public TacoOrder order() - создание объекта TacoOrder для размещения в модели
  - public Taco taco() - создание объекта TacoOrder для размещения в модели
  - (@GetMapping) public String showDesignForm() - возвращает "design" для выбора html-шаблона
  - (@PostMapping) public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) - проверяет валидность введенных данных и в случае успеха переводит на страницу с заказом или возвращает на страницу design для корректировки состава тако и имени
  - private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) - собирает ингредиенты в списки по типам (используется в addIngredientsToModel)

- #### OrderController
  Контроллер для оформления заказа

  ##### Поля:
  - private final OrderRepository orderRepository - инжектим репозиторий с заказами

   ##### Аннотации:
  - @Slf4j - логирование
  - @Controller - указываем Spring, что это контроллер 
  - @RequestMapping("/orders") - данный контроллер обрабатывает запросы по адресу: .../orders
  - @SessionAttributes("tacoOrder") - объект TacoOrder, объявленный в классе чуть ниже, должен поддерживаться на уровне сеанса

  ##### Методы:
  - public String orderForm() - возвращает "orderForm" для выбора html-шаблона
  - (@PostMapping) public String processOrder(@Valid TacoOrder order, Errors errors, SessionStatus sessionStatus) - проверяет заказ тако на валидность и в случае успеха закрывает сессию или возвращает на страницу orders с подсказками, где ошибки в ведённых полях

### models

- #### Ingredient
  Класс для описания ингредиентов тако

  ##### Аннотации:
  - @Data - геттеры, сеттеры и пр. от Lombok
  - @AllArgsConstructor - конструктор от Lombok
  - @NoArgsConstructor(access= AccessLevel.PRIVATE, force=true) - конструктор от Lombok
  - @Table("ingredients") - аннотация для Cassandra
    
  ##### Поля:
  - (@PrimaryKey) private String id - id ингредиента, помечен аннотацией org.springframework.data.cassandra.core.mapping.PrimaryKey для работы с БД
  - private String name - название ингредиента
  - private Type type - тип ингредиента
 
  ##### Внутренние классы:
  - public enum Type - обычный enum для хранения типов ингридиентов
 
- #### IngredientUDT
  Класс для хранения списка ингридиентов таков в бд

  ##### Аннотации:
  - @Data - геттеры, сеттеры и пр. от Lombok
  - @RequiredArgsConstructor - конструктор от Lombok
  - @NoArgsConstructor (access = AccessLevel.PRIVATE, force = true) - конструктор от Lombok
  - @UserDefinedType("ingredient") - определяет, как ингридиенты будут храниться в столбце ingridients таблицы tacos
 
  #### Поля:
  - private final String name - название ингридиента
  - private final Ingredient.Type type - тип ингридиента
 
- #### Taco
  Класс для описания тако

  ##### Аннотации:
  - @Data - геттеры, сеттеры и пр. от Lombok
  - @Table("tacos") - аннотация для Cassandra

  ##### Поля:
  - (@PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)) private UUID id = Uuids.timeBased() - UUID каждого тако, аннотация говорит, что свойство id играет роль ключа раздела
  - (@PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)) private Date createdAt = new Date() - дата создания тако, аннотация говорит, что свойство createdAt играет ролю ключа кластеризации + данные в таблице будут храниться в обратном порядке (самые новые тако - выше)
  - private String name - имя сконструированного клиентом тако. Также включает аннотации @NotNull и @Size(min = 5) для валидации введеного клиентом названия (не нулевое значение и минимум 5 знаков)
  - (@Column("ingredients")) private List<IngredientUDT> ingredients - список ингридиентов в придуманном рецепте тако. Также включает аннотации @Size(min = 1) - список должен содержать хотя бы 1 ингридиент + аннотация Cassandra - список хранится в столбце ingredients

  #### Методы:
  - public void addIngredient(Ingredient ingredient) - добавление ингридиента в список ингридиентов
 
- #### TacoOrder
  Класс для описания заказа тако

  ##### Аннотации:
  - @Data - геттеры, сеттеры и пр. от Lombok
  - @Table("orders") - аннотация для Cassandra

  ##### Поля:
  - (@PrimaryKey) rivate UUID id = Uuids.timeBased() - id заказа - первичный ключ
  - private String deliveryName - имя клиента для заказа + аннотация @NotBlank(message = "Delivery name is required") - не пустое
  - private String deliveryStreet - улицу доставки + аннотация @NotBlank(message = "Street is required") - не пустое
  - private String deliveryCity - город доставки + аннотация @NotBlank(message = "City is required") - не пустое
  - private String deliveryState - штат доставки + аннотация @NotBlank(message = "State is required") - не пустое
  - private String deliveryZip - индекс адреса доставки + аннотация @NotBlank(message = "Zip code is required") - не пустое
  - private String ccNumber - номер карты для оплаты + аннотация @CreditCardNumber(message = "Not a valid credit card number") - валидация введенного номера карты от Hibernate Validator
  - private String ccExpiration - срок действия карты оплаты + аннотация  @Pattern(regexp = "^(0[1-9]|1[0-2])/([2-9][0-9])$", message = "Must be formatted MM/YY") - валидация даты по регулярному выражению
  - private String ccCVV - CVV карты + аннотация @Digits(integer=3, fraction=0, message="Invalid CVV") - валидация CVV
  - (@Column("tacos")) List<TacoUDT> tacos - список тако в заказе, хранится в столбце tacos

  ##### Методы:
  - public void addTaco(Taco taco) - добавление тако в список тако
 
### converter

- #### IngredientByIdConverter
  Класс для конвертации строкового представления ингридиента, полученного из POST формы html в конкретный объект Ingridient для добавления его в Taco
  Реализует интерфейс org.springframework.core.convert.converter.Converter<String, IngredientUDT>

  ##### Аннотации:
  - @Component - аннотация для Spring, чтобы сделать bean

  ##### Поля:
  - private final IngredientRepository ingredientRepository - подгружаем БД с ингридиентами

  ##### Методы:
 - (@Override) public IngredientUDT convert(String id) - получает строковый id ингридиента и возвращает экз. IngredientUDT

### config

- #### WebConfig
  Класс конфигурации. Реализует интерфейс org.springframework.web.servlet.config.annotation.WebMvcConfigurer

  ##### Аннотации:
  - @Configuration - аннотация для Spring, чтобы показать, что класс является конфигурацией
 
  ##### Методы:
  - (@Override) public void addViewControllers(ViewControllerRegistry registry) - добавляем контроллер для использования шаблона "home" при переходе на адрес .../
 
- #### DataLoaderConfig
  Класс конфигурации. Реализует интерфейс org.springframework.web.servlet.config.annotation.WebMvcConfigurer

  ##### Аннотации:
  - @Configuration - аннотация для Spring, чтобы показать, что класс является конфигурацией
 
  ##### Методы:
  - (@Bean) public ApplicationRunner dataLoader(IngredientRepository repo) - загружаем в репозиторий ингридиентов ингридиенты
 
### utils

- #### TacoUDRUtils
  Вспомогательный класс, конвертирующий модели в User-Defined Type

  ##### Методы:
  - public static IngredientUDT toIngredientUDT(Ingredient ingredient) - конвертирует Ingredient в IngredientUDT
  - public static TacoUDT toTacoUDT(Taco taco) - конвертирует Taco в TacoUDT


## HTML-шаблоны

### home
Стартовая страница, доступна по адресу: .../
![image](https://github.com/bmsalikhov/taco/assets/153372291/f165f742-b709-4776-9628-bfafee956705)

### design
Страница для создания своего тако, доступна по адресу: .../design
![image](https://github.com/bmsalikhov/taco/assets/153372291/8ca0d61f-40ec-4861-af78-e8e07296caec)

### orderForm
Страница для заведения заказа, доступна по адресу: .../orders
![image](https://github.com/bmsalikhov/taco/assets/153372291/8dcd8ded-47d4-40a3-89b8-f460558d60fd)




  
 
  
