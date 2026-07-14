# MiPatita — Arquitectura de Microservicios

Proyecto semestral de la asignatura **Desarrollo FullStack I (DSY1103)** — Examen Final Transversal.

## Contexto del proyecto

**MiPatita** es un sistema de gestión del cuidado de mascotas construido con una
arquitectura de microservicios. Permite registrar mascotas y administrar su
información de cuidado: actividades físicas, alimentación, control de peso,
vacunas y recordatorios. Un servicio de notificaciones agrega la información de
varios microservicios para avisar al dueño de las tareas pendientes de cada mascota.

Cada microservicio es independiente, tiene su **propia base de datos** (SQLite +
Flyway) y se comunica con los demás por **REST** (Feign) a través de un servidor
de descubrimiento (**Eureka**) y un **API Gateway** que actúa como punto de
entrada único.

## Integrantes del equipo

> Completar antes de entregar.

- Nombre Apellido — (rol)
- Nombre Apellido — (rol)
- Nombre Apellido — (rol)

## Microservicios implementados (10)

| # | Servicio | Puerto | Descripción |
|---|----------|--------|-------------|
| 1 | `eureka-server` | 8761 | Servidor de descubrimiento de servicios |
| 2 | `api-gateway` | 8080 | Punto de entrada único (Spring Cloud Gateway) |
| 3 | `ms-usuarios` | 8082 | Registro, login y autenticación con JWT |
| 4 | `ms-mascotas` | 8081 | CRUD de mascotas (identidad del dominio) |
| 5 | `ms-actividades` | 8083 | Actividades físicas de las mascotas |
| 6 | `ms-comidas` | 8084 | Alimentación de las mascotas |
| 7 | `ms-control-peso` | 8085 | Seguimiento del peso |
| 8 | `ms-recordatorios` | 8086 | Recordatorios de cuidados |
| 9 | `ms-vacunas` | 8087 | Registro de vacunas |
| 10 | `ms-notificaciones` | 8088 | Agrega recordatorios y vacunas vía Feign |

## Rutas principales del API Gateway

Todo el tráfico entra por el Gateway (`http://localhost:8080`) y se enruta al
microservicio correspondiente mediante Eureka (`lb://`):

| Ruta en el Gateway | Microservicio destino |
|--------------------|-----------------------|
| `/api/v1/auth/**` | ms-usuarios |
| `/api/v1/mascotas/**`, `/api/v2/mascotas/**` | ms-mascotas |
| `/api/v1/actividades/**` | ms-actividades |
| `/api/v1/comidas/**` | ms-comidas |
| `/api/v1/controles-peso/**` | ms-control-peso |
| `/api/v1/recordatorios/**` | ms-recordatorios |
| `/api/v1/vacunas/**` | ms-vacunas |
| `/api/v1/notificaciones/**` | ms-notificaciones |

## Documentación Swagger / OpenAPI

Cada microservicio expone su documentación en `/swagger-ui.html`. Ejemplos (en ejecución local):

- Usuarios: http://localhost:8082/swagger-ui.html
- Mascotas: http://localhost:8081/swagger-ui.html
- Actividades: http://localhost:8083/swagger-ui.html
- Notificaciones: http://localhost:8088/swagger-ui.html

(El resto de los servicios sigue el mismo patrón cambiando el puerto.)

## Autenticación (JWT)

`ms-usuarios` centraliza la autenticación:

1. `POST /api/v1/auth/registro` — crea un usuario (contraseña cifrada con BCrypt).
2. `POST /api/v1/auth/login` — devuelve un token JWT.
3. Endpoints protegidos (ej. `GET /api/v1/auth/perfil`) requieren el header
   `Authorization: Bearer <token>`.

## Ejecución local (IDE)

Requisitos: JDK 21. Cada servicio trae el wrapper de Maven (`mvnw`).

Levantar en este orden (Eureka primero, luego el resto):

```bash
# 1. Servidor de descubrimiento
cd eureka-server && ./mvnw spring-boot:run

# 2. En terminales separadas, cada microservicio:
cd ms-mascotas && ./mvnw spring-boot:run
cd ms-usuarios && ./mvnw spring-boot:run
cd ms-actividades && ./mvnw spring-boot:run
# ... (comidas, control-peso, recordatorios, vacunas, notificaciones)

# 3. Por último el Gateway
cd api-gateway && ./mvnw spring-boot:run
```

En Windows usar `.\mvnw.cmd spring-boot:run`.

Verificar en el panel de Eureka (http://localhost:8761) que los 10 servicios
aparezcan registrados.

## Ejecución con Docker

Requisitos: Docker Desktop. Desde la raíz del proyecto:

```bash
docker compose up --build
```

Esto construye y levanta los 10 contenedores en una red interna donde se
descubren por nombre. El Gateway queda en `http://localhost:8080` y Eureka en
`http://localhost:8761`.

Para detener todo:

```bash
docker compose down
```

## Pruebas unitarias y cobertura

Cada microservicio tiene pruebas unitarias con **JUnit 5 + Mockito** sobre la capa
de servicio (lógica de negocio) y la capa de controlador (`@WebMvcTest`). La
cobertura se mide con **JaCoCo** y supera el 80% en todos los servicios.

Ejecutar las pruebas de un servicio:

```bash
cd ms-actividades && ./mvnw test
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

Cobertura por servicio (instrucciones):

| Servicio | Cobertura |
|----------|-----------|
| ms-mascotas | 100% |
| ms-usuarios | 97% |
| ms-actividades | 92% |
| ms-recordatorios | 92% |
| ms-comidas | 91% |
| ms-control-peso | 91% |
| ms-vacunas | 91% |
| ms-notificaciones | 88% |

## Tecnologías

- Java 21, Spring Boot 4.0.6
- Spring Cloud 2025.1.1 (Eureka, Gateway, OpenFeign)
- Spring Data JPA + Hibernate + SQLite + Flyway
- Spring Security + JWT (jjwt)
- Bean Validation, SLF4J
- springdoc-openapi (Swagger UI)
- JUnit 5, Mockito, JaCoCo
