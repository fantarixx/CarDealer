# 🚗 CarDealer

Backend-петпроект информационной системы автосалона.

Проект демонстрирует эволюцию backend-системы:
от доменной модели → к REST API → к системе с авторизацией.

---

## 📌 Описание проекта

Система представляет собой backend для мультибрендового автосалона.

Поддерживает:

* покупку автомобилей из наличия
* заказ автомобиля с кастомной комплектацией
* конфигуратор автомобилей
* запись на тест-драйв
* управление складом и каталогом
* разграничение доступа по ролям

---

## 🏗 Архитектура

Проект построен по луковой архитектуре:

* Controller (REST API)
* Service Layer (бизнес-логика)
* Persistence Layer (JPA)

### ⚙️ Технологический стек

* Java + Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Liquibase
* Spring Security
* Keycloak
* Docker
* Testcontainers

---

## ✅ Реализованный функционал

### 🔹 ЛР1 — Доменная модель

* Спроектированы сущности:

  * автомобили
  * запчасти
  * заказы
  * тест-драйвы
* Реализован конфигуратор автомобилей:

  * выбор компонентов
  * проверка совместимости
  * расчёт стоимости
* Добавлены исключения:

  * DomainValidationException
  * IncompatibleComponentException
* Реализован жизненный цикл заказов

---

### 🔹 ЛР2 — Backend + БД

* Интеграция с Spring Boot
* Подключение PostgreSQL
* Реализация REST API
* Использование DTO
* MapStruct / ModelMapper

Реализованы:

* JPA-сущности
* репозитории

Добавлены:

* Liquibase миграции
* Swagger документация

Интеграционные тесты:

* Testcontainers

---

### 🔹 ЛР3 — Безопасность

* Spring Security + Keycloak
* Роли:

  * USER
  * MANAGER
  * WAREHOUSE_ADMIN
  * ADMIN
* Ограничение доступа:

  * по ролям
  * по владельцу (@PreAuthorize)
* Защищены все endpoints

---

## 🔐 Роли пользователей

| Роль            | Возможности         |
| --------------- | ------------------- |
| USER            | свои заказы         |
| MANAGER         | управление заказами |
| WAREHOUSE_ADMIN | склад               |
| ADMIN           | полный доступ       |

---

## 🚀 Запуск

### Docker

```bash
docker-compose up -d
```

### Локально

```bash
./gradlew bootRun
```

---

## 📚 API

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

## 🧪 Тесты

* Unit тесты
* Интеграционные тесты (Testcontainers)

---

## 📦 Сущности

* Car
* Part
* Order
* CustomOrder
* TestDriveRequest

---

## 🔮 To Do

### Общие

* [ ] Redis кэш
* [ ] Pagination + sorting
* [ ] Логирование
* [ ] Метрики (Prometheus)
* [ ] CI/CD
* [ ] Покрытие тестами > 80%

---

### ЛР4 — Микросервисы

#### Архитектура

* [ ] Разделить на OrderService и StorageService
* [ ] Отдельные БД
* [ ] Docker для каждого сервиса

#### StorageService

* [ ] AssemblyOrder (заказ на сборку)
* [ ] CRUD API

#### Messaging

* [ ] Kafka / RabbitMQ
* [ ] События:

  * OrderSentForApproval
  * OrderApproved
  * OrderRejected

#### Взаимодействие

* [ ] Асинхронное взаимодействие сервисов

#### Надёжность

* [ ] Outbox Pattern
* [ ] Идемпотентность
* [ ] Retry

#### Observability

* [ ] traceId
* [ ] логирование
* [ ] tracing

#### Безопасность

* [ ] Ограничение доступа StorageService

#### Тесты

* [ ] Testcontainers (Kafka + БД)

---

## 📈 Итог

Проект покрывает:

* доменную модель
* REST API
* работу с БД
* безопасность
* подготовку к микросервисам

---

## 👨‍💻 Автор

Pet-project для обучения backend-разработке.
