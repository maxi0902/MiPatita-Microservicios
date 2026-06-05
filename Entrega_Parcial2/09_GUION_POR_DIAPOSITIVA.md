# Guión de la Presentación — MiPatita (15 min)
## Alineado a TUS diapositivas reales · repartido por dificultad

**Reparto por diapositivas (según tu PPT):**
| Diapositivas | Quién | Dificultad | Temas |
|---|---|---|---|
| 1, 2, 3 | **Rodrigo** | 🟢 Fácil | Portada · Contexto (monolito→micro) · Arquitectura |
| 4, 5 | **Camila** | 🟡 Media | MiPatita (CSR) · Persistencia (JPA + Flyway) |
| 6, 7, 8 | **Maxi** | 🔴 Difícil | Feign · Seguridad JWT · Validación y errores |
| 9, 11, 12 | **Constanza** | 🟡 Media | Logs (observabilidad) · Conclusión · Gracias |

> ⚠️ **Diapositiva 10 ("Demostración - Postman"):** como **quitamos la demo**, lo mejor es
> **eliminarla** del PPT (clic derecho → Eliminar diapositiva). Si la dejan, Constanza solo dice
> *"el sistema quedó probado de extremo a extremo"* y pasa de largo.

**Antes de empezar:** dejen los **dos servicios corriendo** (8081 y 8080) y el **proyecto abierto
en el editor**. No lean palabra por palabra; úsenlo de apoyo.

---

## 🟢 RODRIGO — Diapositivas 1, 2 y 3 (~3.5 min)

**Diapo 1 (Portada):**
"Hola, somos el equipo de **MiPatita**, una app para **cuidar mascotas**: anotar sus paseos,
comidas y recordatorios de vacunas. Les contamos cómo la construimos con microservicios."

**Diapo 2 (Contexto - El desafío):**
"El desafío fue cambiar **cómo está armada por dentro**. **Antes** era un *monolito*: un solo
programa que hacía todo, con una sola base de datos — si una parte fallaba, se caía todo. **Ahora**
son **dos servicios independientes**, cada uno con **su propia base de datos**, que se comunican
por internet. Si uno falla, el otro sigue."

**Diapo 3 (Arquitectura del sistema):**
"Acá está la arquitectura. Dos microservicios:
- **MiPatita** (puerto 8080): usuarios, autenticación, actividades y recordatorios.
- **Mascotas** (puerto 8081): el CRUD de las mascotas.

Se comunican con **Feign** y **no comparten base de datos**. Ahora **Camila** explica MiPatita por
dentro."

---

## 🟡 CAMILA — Diapositivas 4 y 5 (~3 min)

**Diapo 4 (Microservicio MiPatita - CSR):**
"Este es **MiPatita**, el servicio principal. Está ordenado con el patrón **CSR**, tres capas:
- **Controller**: recibe la petición y responde.
- **Service**: la lógica de negocio.
- **Repository**: habla con la base de datos.

Tiene su propia base, **mipatita.db**, con usuarios, actividades y recordatorios. Además usa
seguridad, validaciones y logs — que mis compañeros explican en detalle."

**Diapo 5 (Persistencia - JPA + Flyway):**
"Sobre cómo guarda los datos: usamos **JPA**, que conecta las clases de Java con las tablas. Algo
importante que pidió el profe: **no dejamos que el programa cree las tablas solo** (`ddl-auto=none`,
porque es riesgoso). En vez de eso, **Flyway** crea las tablas con **migraciones ordenadas por
versión** (V1, V2...). Así el historial queda controlado y funciona en cualquier computador. Ahora
**Maxi** con la parte más técnica."

---

## 🔴 MAXI — Diapositivas 6, 7 y 8 (~4.5 min)

**Diapo 6 (Feign - Comunicación):**
"Cómo se comunican los dos microservicios: con **Feign**. Declaramos una **interfaz** con los
endpoints del otro servicio y Spring genera la conexión solo. El ejemplo clave: **antes de crear un
paseo**, MiPatita le pregunta a Mascotas *'¿existe esta mascota?'*. Si no existe, responde **400** y
no lo crea. Lo importante: MiPatita **no entra a la base de datos del otro**, solo le **pregunta**
por su API → consistencia **sin compartir datos**."

**Diapo 7 (Seguridad - JWT):**
"La seguridad es **stateless con JWT**. Sin sesiones en el servidor: al hacer **login** te entrega
un **token** firmado. En cada petición mandas ese token en la cabecera (*Authorization: Bearer*) y
un **filtro** lo valida antes de dejarte pasar. Las contraseñas van **cifradas con BCrypt**. El
flujo: *login → token → petición con token → filtro valida → acceso*."

**Diapo 8 (Validación y manejo de errores):**
"Validamos los datos con **Bean Validation** (`@Valid`, `@NotBlank`, `@NotNull`, `@Min`). Y los
errores los manejamos **todos en un solo lugar** con `@RestControllerAdvice`, devolviendo códigos
limpios: **400** datos malos, **401** credenciales, **404** no existe. Ahora **Constanza** cierra."

---

## 🟡 CONSTANZA — Diapositivas 9, 11 y 12 (~3 min)
*(la 10 se salta o se elimina, porque no hay demo)*

**Diapo 9 (Observabilidad - Logs JSON):**
"Para la **trazabilidad**, ambos servicios escriben **logs en formato JSON**: fecha, nivel,
servicio, clase y mensaje. Niveles **INFO** (normal), **WARN** (avisos) y **ERROR** (fallos). Como
incluimos la clase, podemos **seguir una petición** por las capas Controller → Service →
Repository."

**Diapo 11 (Conclusión):**
"En conclusión: migramos MiPatita a una **arquitectura de microservicios real**. Dos servicios
desacoplados, cada uno con su base de datos; comunicación por **Feign**; seguridad con **JWT**,
validaciones y errores centralizados; persistencia con **Flyway** y observabilidad con **logs
JSON**. Un sistema **organizado, seguro y mantenible**."

**Diapo 12 (Gracias):**
"Gracias por su atención. ¿Tienen alguna consulta?"

---

## 🎤 Después: preguntas individuales
El profe le pregunta a **cada uno por separado** y pide **modificar código en vivo**. Cada
integrante debe poder explicar **sus diapositivas** y hacer un cambio chico (un log, una validación,
un código HTTP). La app **debe estar corriendo** en el PC del profe.
Repasen su parte + `05_CHULETA_PATRONES.md` y `06_MAPA_EXAMEN.md`.
