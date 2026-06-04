# Parcial 2 — Desarrollo Fullstack I (DSY1103)
## ¿Qué es esta evaluación y qué vamos a presentar?

> Documento de orientación para todo el equipo. Léanlo antes de la defensa.
> Equipo: Camila Cornejo · Constanza Sarmiento · Rodrigo Valdivia · Maximiliano Morales
> Proyecto: **MiPatita** — app de cuidado de mascotas (registro de paseos, comidas, vacunas y recordatorios).

---

## 1. En una frase

Teníamos un proyecto semestral (una app de mascotas) y para este hito lo tuvimos que
**partir en microservicios**: en vez de un solo programa que hace todo, ahora son **dos
programas independientes** que se comunican por internet (HTTP), cada uno con **su propia
base de datos**, con **seguridad por token (JWT)**, validaciones, manejo de errores ordenado
y registros (logs) en formato JSON.

## 2. ¿Por qué microservicios? (la idea que hay que entender)

Imaginen un restaurant. Antes teníamos **un solo cocinero** que tomaba el pedido, cocinaba,
cobraba y lavaba los platos. Si se enfermaba, se caía todo.

Ahora tenemos **dos estaciones separadas**:

- **MiPatita** (la "puerta de entrada"): recibe a los clientes, revisa que tengan permiso
  de entrar (seguridad / login) y organiza los pedidos. Maneja los **usuarios**, las
  **actividades** (paseos, comidas) y los **recordatorios** (vacunas).
- **Microservicio Mascotas**: una estación dedicada **solo a los datos de las mascotas**
  (nombre, raza, peso, edad). No sabe nada de usuarios ni de seguridad; solo guarda mascotas.

Cuando MiPatita necesita datos de una mascota, **no entra a la base de datos del otro**:
le hace un **pedido por HTTP** (como pedir por una ventanilla). Eso es estar "desacoplado":
cada uno puede cambiar o caerse sin romper al otro.

## 3. Lo que construimos (arquitectura)

```
                    ┌──────────────────────────────────────┐
   Cliente          │            MiPatita  (8080)           │
  (Postman) ──JWT──▶│  Auth · Usuarios · Actividades ·       │
                    │  Recordatorios                         │
                    │  Base de datos:  mipatita.db           │
                    └───────────────┬───────────────────────┘
                                    │  Feign (HTTP) — "¿existe la mascota 3?"
                                    ▼
                    ┌──────────────────────────────────────┐
                    │      Microservicio Mascotas (8081)     │
                    │  CRUD de mascotas                      │
                    │  Base de datos:  mascotas.db           │
                    └──────────────────────────────────────┘
```

- **Dos proyectos Spring Boot independientes**, cada uno con su base de datos SQLite.
- Se comunican con **Feign** (un cliente HTTP declarativo de Spring Cloud).
- Ejemplo real de comunicación: **antes de registrar un paseo (actividad), MiPatita le
  pregunta al microservicio de Mascotas si esa mascota existe.** Si no existe, no deja
  crear la actividad. Esa es una "regla de negocio entre microservicios".

## 4. Los 5 requisitos obligatorios del profesor y cómo los cumplimos

| Requisito del profe | Cómo lo cumplimos | Dónde mirarlo |
|---|---|---|
| **1. Arquitectura desacoplada** (≥2 microservicios, BD independiente cada uno) | 2 proyectos: MiPatita (`mipatita.db`) y Mascotas (`mascotas.db`). Nunca comparten base de datos. | Las dos carpetas del proyecto |
| **2. Persistencia + ORM** (JPA/Hibernate, **sin** `ddl-auto=update`, migraciones versionadas) | Usamos JPA/Hibernate con `ddl-auto=none`. Las tablas las crea **Flyway** con scripts versionados `V1__...sql`. | `application.properties` y carpeta `db/migration` |
| **3. Comunicación entre servicios** (Feign o WebClient) | **Feign Client** (`MascotaClient`). MiPatita consume la API de Mascotas. | `webclient/MascotaClient.java` |
| **4. Validación + respuestas limpias** (`ResponseEntity` con 201/204/400/404 + Bean Validation) | Todos los endpoints devuelven `ResponseEntity` con el código correcto y usan `@Valid` con `@NotBlank`, `@NotNull`, `@Min`. | Los `Controller` y los `model` |
| **5. Seguridad + observabilidad** (Spring Security + JWT stateless + logs JSON) | Login con JWT, sesiones STATELESS, contraseñas con BCrypt. Logs en **JSON** con SLF4J + Logback en ambos servicios. | carpeta `security/` y `logback-spring.xml` |

## 5. Qué se entrega y qué se presenta

**Entregables (suben a AVA en PDF / archivos):**
1. **Informe técnico** (PDF) — diagrama de arquitectura, modelo de datos, contratos de API,
   estrategia de seguridad y manejo de errores.
2. **El código** de los dos microservicios (repositorio Git).
3. **La presentación** (estas diapositivas / PPT).

**El día de la evaluación:**
- El equipo tiene **15 minutos** para exponer cómo funciona el sistema y su arquitectura.
- **La aplicación debe correr en el computador del profesor.** (Ya está probada y corre: ver
  el documento `03_CAMBIOS_REALIZADOS.md`.)

## 6. ⚠️ Cómo te van a evaluar a TI (esto es lo más importante)

**La nota es INDIVIDUAL.** No basta con que el grupo presente bien. Después de la exposición,
el profesor le hace preguntas a **cada uno** y pide **modificar código en vivo** (live coding).
Tienes que **entender** el proyecto, no solo haberlo presentado.

Ponderaciones de la defensa individual:

| Peso | Qué te van a pedir | Cómo prepararte |
|---|---|---|
| **18%** | Modificar código en vivo: agregar un log, capturar una excepción, devolver un código HTTP distinto | Practica con `MascotaService` / `GlobalExceptionHandler` |
| **15%** | Agregar una validación nueva (Bean Validation) y probarla en Postman | Practica agregando un `@NotBlank` o `@Min` a un campo |
| **15%** | Cambiar/añadir lógica de comunicación entre microservicios (Feign) | Entiende `MascotaClient` y `ActividadService` |
| **13%** | Explicar el flujo de una petición por las capas (Controller→Service→Repository) leyendo los logs | Sigue un log de principio a fin |
| **10%** | Modificar la persistencia (entidad, repositorio o script SQL) y que siga compilando | Practica agregando un campo a una entidad + su migración |
| **10%** | Explicar por qué la lógica de negocio está en la capa Service | Mira `UsuarioService.autenticar()` |
| **8%** | Explicar la comunicación síncrona entre microservicios | Mira el ejemplo de "validar mascota antes de crear actividad" |
| **8%** | Justificar tu aporte con tus **commits de Git** | Asegúrate de tener commits a tu nombre |
| **3%** | Justificar el modelado de tablas (relaciones, normalización) | Mira los scripts `V1__...sql` |

**Conclusión:** cada integrante debe poder abrir el proyecto, encontrar el archivo correcto,
hacer un cambio chico y explicar por qué. El guión (`02_GUION_PRESENTACION.md`) reparte la
exposición, pero las preguntas son para cada uno por separado.
