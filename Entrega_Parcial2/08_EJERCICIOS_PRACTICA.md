# 🏋️ 6 Ejercicios de práctica (uno de cada tipo)

Resuélvelos **mirando tu mapa** (`06_MAPA_EXAMEN.md`), no las soluciones. Intenta primero;
las **soluciones están al final** para autocorregirte. Recuerda los 4 pasos:
editar → guardar → reiniciar la terminal correcta → probar en Postman.

---

## Los retos (como te los diría el profe)

**1. LOG (IE 2.3.4 — 18%)**
> "Agrega un log que avise cuándo se **elimina una actividad**."

**2. VALIDACIÓN (IE 2.2.5 — 15%)**
> "Haz que los **pasos** (`pasos`) de una actividad sean **obligatorios**."
> (Pista: `pasos` es número... ¿@NotBlank o @NotNull?)

**3. CÓDIGO HTTP (IE 2.3.4)**
> "Haz que al **crear una mascota** la API devuelva **202 (Accepted)** en vez de 201."

**4. EXCEPCIÓN (IE 2.3.4)**
> "Si alguien crea un **recordatorio** con `frecuenciaDias` mayor a 365, recházalo con un
> código **422** y un mensaje."

**5. FEIGN (IE 2.4.4 — 15%)**
> "Cuando MiPatita reciba la lista de mascotas del otro microservicio, que escriba en el **log
> el nombre de cada una**."

**6. PERSISTENCIA (IE 2.1.4 — 10%)**
> "Agrega un campo **`notas`** a los recordatorios."

---
---

# ✅ SOLUCIONES (no mires hasta intentar)

### 1. LOG
- **Archivo:** `ActividadService.java` (exampleMiPatita). **Terminal 2.**
- Esa clase NO tiene logger, así que agrégalo (copia de otro Service): los 2 imports
  `org.slf4j.Logger` / `LoggerFactory`, y la línea del logger:
```java
private static final Logger log = LoggerFactory.getLogger(ActividadService.class);
```
- En el método `deleteActividad`, al inicio:
```java
log.info("Eliminando actividad con id {}", idActividad);
```
- **Probar:** DELETE `http://localhost:8080/api/v1/actividades/1` (con token) → mira el log en la **Terminal 2**.

### 2. VALIDACIÓN
- **Archivo:** `Actividad.java` (exampleMiPatita). **Terminal 2.**
- `pasos` es número → se usa **@NotNull** (no @NotBlank):
```java
@NotNull(message = "Los pasos son obligatorios")
private Integer pasos;
```
- **Probar:** POST `http://localhost:8080/api/v1/actividades` (con token) SIN el campo `pasos` → **400**.

### 3. CÓDIGO HTTP
- **Archivo:** `MascotaController.java` (exampleMascotas). **Terminal 1.**
- En `guardarMascota`, cambia `CREATED` por `ACCEPTED`:
```java
return ResponseEntity.status(HttpStatus.ACCEPTED).body(nuevaMascota);
```
- **Probar:** POST `http://localhost:8081/api/v1/mascotas` (sin token) con una mascota completa → **202**.
- (Déjalo de nuevo en `CREATED` al terminar, porque crear corresponde a 201.)

### 4. EXCEPCIÓN (2 pasos)
- **Terminal 2** (todo es de MiPatita).
- **Paso 1** — en `GlobalExceptionHandler.java` (exception/), agrega el atrapador (si no lo tienes ya):
```java
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, String>> manejar(IllegalArgumentException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY); // 422
}
```
- **Paso 2** — en `RecordatorioService.java`, al inicio de `saveRecordatorio`:
```java
if (recordatorio.getFrecuenciaDias() != null && recordatorio.getFrecuenciaDias() > 365) {
    throw new IllegalArgumentException("La frecuencia no puede superar los 365 días");
}
```
- **Probar:** POST `http://localhost:8080/api/v1/recordatorios` (con token) con `"frecuenciaDias": 500` → **422**.

### 5. FEIGN
- **Archivo:** `MascotaService.java` (exampleMiPatita — el que usa `mascotaClient`). **Terminal 2.**
- (Esta clase YA tiene logger.) En `getMascotas()`, después de obtener la lista:
```java
lista.forEach(m -> log.info("Mascota recibida vía Feign: {}", m.getNombre()));
```
- **Probar:** GET `http://localhost:8080/api/v1/mascotas` (con token) → mira los nombres en la **Terminal 2**.

### 6. PERSISTENCIA (2 cosas)
- **Terminal 2.**
- **Cosa 1** — en `Recordatorio.java` (exampleMiPatita), agrega el campo:
```java
private String notas;
```
- **Cosa 2** — en `resources/db/migration/` crea `V2__agregar_notas.sql`:
```sql
ALTER TABLE recordatorios ADD COLUMN notas VARCHAR(255);
```
- **Probar:** POST `http://localhost:8080/api/v1/recordatorios` (con token):
```json
{ "idMascota": 1, "tipoRecordatorio": "Baño", "frecuenciaDias": 15, "fechaProxima": "2026-07-01", "notas": "Shampoo especial" }
```
→ **201** y en la respuesta aparece `notas`.
