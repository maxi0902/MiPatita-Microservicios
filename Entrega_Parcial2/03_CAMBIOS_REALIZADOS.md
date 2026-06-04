# Cambios realizados al proyecto (y cómo defenderlos)

> **Importante para el equipo:** el código fue revisado y ajustado para que **cumpla todos los
> requisitos del profe** y para corregir un bug que impedía que funcionara una parte. Acá está
> **qué se cambió, en qué archivo y por qué**, para que cualquiera pueda explicarlo en la defensa.
> El sistema quedó **probado y corriendo de punta a punta** (ver sección final).

---

## Resumen rápido

| # | Cambio | Archivo(s) | Por qué / IE que apoya |
|---|--------|-----------|------------------------|
| 1 | Lógica de **login movida del Controller al Service** | `UsuarioService.java`, `AuthController.java` | El profe evalúa que la lógica esté en la capa Service (IE 2.2.4). Antes el login estaba en el Controller. |
| 2 | `Mascota` en MiPatita pasó de **@Entity a DTO** | `model/Mascota.java` (+ se borró `MascotaRepository.java` huérfano) | Refuerza "una base de datos por microservicio". MiPatita NO guarda mascotas, solo las consume por HTTP. |
| 3 | **Manejo de errores centralizado** ampliado | `GlobalExceptionHandler.java`, nuevo `CredencialesInvalidasException.java` | Ahora devuelve 400 (validación), **401** (credenciales) y 500 (genérico) desde un solo lugar (IE 2.3.4). |
| 4 | **Bean Validation agregada a `Recordatorio`** | `model/Recordatorio.java`, `RecordatorioController.java` | No tenía ninguna validación; ahora valida con `@NotNull`/`@NotBlank` + `@Valid` (IE 2.2.5). |
| 5 | **Logs JSON + SLF4J en el microservicio Mascotas** | nuevo `logback-spring.xml`, `MascotaService.java` (Mascotas) | El requisito de logs estructurados solo estaba en MiPatita; ahora ambos servicios loguean en JSON. |
| 6 | **Bug Feign corregido**: faltaba el endpoint `GET /mascotas/{id}` | `MascotaController.java` y `MascotaService.java` (Mascotas) | Sin él, obtener una mascota por id daba 404 y **NO se podían crear recordatorios**. Ahora funciona. |
| 7 | **Secreto JWT y URL de Feign externalizados** (configurables) | `JwtTokenProvider.java`, `MascotaClient.java`, `application.properties` | Buenas prácticas: no quemar valores en el código. Evita que al reiniciar se invaliden los tokens. |
| 8 | Validación en login/registro + logs + `System.out` → logger | `LoginRequest`, `RegistroRequest`, `JwtAuthenticationFilter` | Consistencia y observabilidad. |
| 9 | **Maven wrapper** copiado al micro Mascotas + `.gitignore` | `mvnw`, `.gitignore` | Para que el proyecto **corra en el PC del profe** sin instalar Maven. |

---

## Detalle de cada cambio

### 1. Login en la capa Service (IE 2.2.4 — 10%)
**Antes:** `AuthController.login()` buscaba el usuario en el repositorio, comparaba la contraseña
y generaba el token, todo dentro del Controller.
**Ahora:** toda esa lógica está en `UsuarioService.autenticar(username, password)`. El Controller
solo recibe la petición y delega.
**Cómo defenderlo:** "La capa Controller solo se preocupa de HTTP. La lógica de negocio
(verificar credenciales, generar token) va en el Service, así está aislada, es reutilizable y
fácil de probar."

### 2. `Mascota` como DTO en MiPatita (principio BD-por-microservicio)
**Antes:** `Mascota` en MiPatita era una `@Entity` de JPA, lo que daba a entender que MiPatita
tenía una tabla `mascotas` (no la tiene).
**Ahora:** es un **DTO** (objeto simple para transportar datos por HTTP vía Feign).
**Cómo defenderlo:** "Las mascotas viven en su propio microservicio y su propia base de datos.
En MiPatita la clase `Mascota` es solo un DTO para recibir/enviar datos por la API; por eso no
es una entidad ni tiene repositorio."

### 3. Manejo centralizado de excepciones (IE 2.3.4 — 18%)
**Ahora** `GlobalExceptionHandler` (`@RestControllerAdvice`) atrapa:
- `MethodArgumentNotValidException` → **400** (errores de `@Valid`)
- `CredencialesInvalidasException` → **401** (login fallido)
- `Exception` → **500** (red de seguridad, con log)
**Cómo defenderlo:** "Centralizamos los errores en un solo lugar con `@RestControllerAdvice`, en
vez de poner try/catch en cada controlador. Cada excepción se traduce a un código HTTP semántico."

### 4. Validación en `Recordatorio` (IE 2.2.5 — 15%)
Se agregó `@NotNull` en `idMascota`, `@NotBlank` en `tipoRecordatorio` y `fechaProxima`, y `@Valid`
en el Controller. Probar con Postman: mandar un recordatorio sin tipo → **400**.

### 5. Logs JSON en Mascotas (observabilidad)
Se creó `logback-spring.xml` en Mascotas (mismo formato JSON que MiPatita) y se agregaron
`log.info/log.warn` en su `MascotaService`.

### 6. Bug de comunicación Feign corregido (¡importante!)
El `MascotaClient` de MiPatita llamaba a `GET /api/v1/mascotas/{id}`, **pero ese endpoint no
existía** en el microservicio Mascotas. Consecuencia: obtener una mascota por id siempre daba 404
y, como los recordatorios validan la mascota con ese llamado, **nunca se podía crear un
recordatorio**. Se agregó el endpoint `obtenerMascota` en el Controller y `findById` en el Service.

### 7-9. Configuración, consistencia y portabilidad
- `jwt.secret` y `jwt.expiration-ms` salen de `application.properties` (no del código).
- La URL del microservicio de Mascotas (`mascotas.service.url`) también está en configuración.
- Se copió el Maven wrapper (`mvnw`) al micro Mascotas y se agregó `.gitignore`.

---

## ✅ Prueba de que TODO funciona (verificado de punta a punta)

Se levantaron los dos servicios y se probó el flujo completo. Resultados reales:

| Prueba | Resultado |
|--------|-----------|
| Registro de usuario | **200** |
| Registro con contraseña vacía | **400** (validación) |
| Login correcto | **200** + token JWT |
| Login con contraseña incorrecta | **401** (manejo centralizado) |
| Pedir mascotas sin token | **403** (seguridad) |
| Pedir mascotas con token (vía Feign) | **200** + lista de 5 mascotas |
| Obtener mascota por id (bug arreglado) | **200** |
| Crear mascota vía Feign | **201** |
| Crear paseo (mascota válida) | **201** |
| Crear paseo (mascota inexistente) | **400** (regla cross-service) |
| Crear paseo sin duración | **400** (validación) |
| Crear recordatorio (antes imposible) | **201** |

---

## Cómo levantar el proyecto (para la demo)

Abrir **dos terminales**:

```bash
# Terminal 1 — Microservicio Mascotas (puerto 8081)
cd "MicroservicioMascotas _V2"
./mvnw spring-boot:run

# Terminal 2 — MiPatita (puerto 8080)
cd MiPatita_V3/demo
./mvnw spring-boot:run
```

> En Windows, si `./mvnw` no funciona en PowerShell, usar `.\mvnw.cmd spring-boot:run`.
> Las bases de datos (`*.db`) se crean solas con Flyway al primer arranque, con 5 mascotas de ejemplo.
> **Ojo:** el puerto 8081 debe estar libre (si tienes corriendo otra app como Expo/Metro, ciérrala).
