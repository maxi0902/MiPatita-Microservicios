# 🗺️ MAPA DE EXAMEN — Live Coding (ten esto al lado)

Todo se resuelve con **los mismos 4 pasos**:
**1)** edito el archivo → **2)** guardo (Ctrl+S) → **3)** reinicio la terminal de ese servicio → **4)** pruebo en Postman.
Lo único que cambia es *qué archivo* y *qué escribo*. Este mapa te dice eso.

---

## 1. LOS 2 SERVICIOS (la base de todo)

| Servicio | Carpeta (en el código) | Terminal | Puerto | ¿Pide token? | ¿De qué se encarga? |
|---|---|---|---|---|---|
| **MiPatita** | `exampleMiPatita` | **Terminal 2** | **8080** | **SÍ** (menos registro/login) | usuarios, login, **actividades**, **recordatorios**, seguridad, Feign |
| **Mascotas** | `exampleMascotas` | **Terminal 1** | **8081** | NO | solo las **mascotas** (crear, listar, editar, borrar) |

> Regla de oro para saber qué terminal reiniciar:
> **¿el archivo está en la carpeta `exampleMiPatita`? → Terminal 2.**
> **¿está en `exampleMascotas`? → Terminal 1.**
> (Casi todo es MiPatita/Terminal 2. Solo las mascotas puras son Mascotas/Terminal 1.)

---

## 2. 🎯 TABLA MAESTRA: "si te piden X → haz Y"

| Si el profe te pide... | Es un... | Abre este archivo | En la carpeta |
|---|---|---|---|
| "agrega/cambia un **log**" | LOG | el **Service** del tema (`MascotaService`, `ActividadService`, `RecordatorioService`) | `service/` |
| "haz que **X sea obligatorio**" / mínimo / largo | VALIDACIÓN | el **modelo** (`Mascota`, `Actividad`, `Recordatorio`) | `model/` |
| "cambia el **número/código** que devuelve" (201→202...) | CÓDIGO HTTP | el **Controller** del tema | `controller/` |
| "**captura una excepción**" / "devuelve 409 o 422" | EXCEPCIÓN | `GlobalExceptionHandler` + un `throw` en un **Service** | `exception/` + `service/` |
| "cambia la **comunicación entre microservicios**" | FEIGN | `MascotaClient` + usarlo en un **Service** | `webclient/` + `service/` |
| "**agrega un campo**" a una tabla | PERSISTENCIA | el **modelo** + archivo nuevo `V2__...sql` | `model/` + `resources/db/migration/` |

### ¿Y de qué servicio/terminal es cada tema?
| El tema es sobre... | Servicio | Terminal a reiniciar |
|---|---|---|
| **Mascotas** (sus datos: validar, campo, CRUD, código al crear) | Mascotas | **Terminal 1** |
| **Actividades** | MiPatita | **Terminal 2** |
| **Recordatorios** | MiPatita | **Terminal 2** |
| **Usuarios / login / seguridad** | MiPatita | **Terminal 2** |
| **Feign / excepciones globales** | MiPatita | **Terminal 2** |

> 👉 Truco para encontrar cualquier archivo: **Ctrl + P** y escribe el nombre (ej: `ActividadService`).
> Fíjate que la ruta diga la carpeta correcta (`exampleMiPatita` o `exampleMascotas`).

---

## 3. POSTMAN: qué método uso y qué significa cada uno

| Método | ¿Para qué sirve? | ¿Lleva Body? |
|---|---|---|
| **GET** | **Pedir/leer** datos (listar todo, ver uno) | No |
| **POST** | **Crear** algo nuevo | Sí (Body raw → JSON) |
| **PUT** | **Actualizar/modificar** algo que ya existe | Sí (Body raw → JSON) |
| **DELETE** | **Borrar** algo | No |

**Cómo armar la petición en Postman:**
1. Elige el método (GET/POST/...).
2. Escribe la URL. Ej: `http://localhost:8080/api/v1/actividades` (8080 = MiPatita, 8081 = Mascotas).
3. ¿Necesitas token? (todo lo de 8080 menos login/registro) → pestaña **Authorization** → **Bearer Token** → pega el token (lo sacas haciendo POST a `/auth/login`).
4. ¿Es POST o PUT? → pestaña **Body** → **raw** → **JSON** → escribe los datos.
5. **Send**.

**¿Dónde veo el resultado?**
- El **número de estado** (200, 201, 400...): arriba a la derecha del panel de respuesta.
- El **contenido** (lo que devuelve): abajo, en "Body / Pretty".
- ⚠️ Los **LOGS NO salen en Postman** → salen en la **TERMINAL**.

---

## 4. ✍️ CÓMO SE ESCRIBE CADA CÓDIGO (los patrones)

### LOG
```java
log.info("mi mensaje");
log.info("con un dato: {}", variable);   // el {} se reemplaza por la variable
```
- Si la clase ya tiene `log` (casi todos los Service), solo escribes la línea.
- Si NO tiene, copia de otro Service estas 3 cosas: los 2 imports `org.slf4j.Logger` y
  `org.slf4j.LoggerFactory`, y la línea `private static final Logger log = LoggerFactory.getLogger(NombreClase.class);`
- 👁️ El log se ve en la **TERMINAL**.

### VALIDACIÓN (va ENCIMA del campo, en el modelo)
```java
@NotBlank(message = "El nombre es obligatorio")   // para TEXTO (no vacío)
private String nombre;

@NotNull(message = "La edad es obligatoria")       // para NÚMEROS (no nulo)
@Min(value = 0, message = "No puede ser negativa")
private Integer edad;
```
- Texto → `@NotBlank`. Número → `@NotNull` (y `@Min(x)` si quieres un mínimo). Largo de texto → `@Size(min=2, max=20)`.
- Para probar: manda el dato **vacío o sin enviarlo** → debe dar **400** con tu mensaje (se ve en Postman).

### CÓDIGO HTTP (en el Controller)
```java
return ResponseEntity.status(HttpStatus.CREATED).body(objeto);   // cambia CREATED por otro
```
- `OK`=200, `CREATED`=201, `ACCEPTED`=202, `NO_CONTENT`=204, `BAD_REQUEST`=400, `NOT_FOUND`=404.

### EXCEPCIÓN (2 pasos)
1) En el **Service**, donde detectas el problema:
```java
throw new IllegalArgumentException("La frecuencia no puede superar 365 días");
```
2) En **GlobalExceptionHandler**, agregas el "atrapador" (copia uno que ya esté y cambia el tipo y el código):
```java
@ExceptionHandler(IllegalArgumentException.class)
public ResponseEntity<Map<String, String>> manejar(IllegalArgumentException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY); // 422
}
```

### FEIGN (cómo MiPatita le habla a Mascotas)
- La lista de llamadas está en la interfaz **`MascotaClient`** (cada método = un endpoint del otro servicio).
- Para usar una llamada desde un Service: `mascotaClient.verificarMascota(id);`
- Para procesar lo que llega del otro servicio (ejemplo, loguear cada nombre):
```java
lista.forEach(m -> log.info("Mascota recibida: {}", m.getNombre()));
```

### PERSISTENCIA — agregar un campo (SIEMPRE 2 cosas)
1) En el **modelo** (la entidad):
```java
private String notas;
```
2) En `resources/db/migration/` creas un archivo nuevo `V2__agregar_notas.sql`:
```sql
ALTER TABLE recordatorios ADD COLUMN notas VARCHAR(255);
```
⚠️ Si agregas el campo pero NO la migración, falla al guardar (la columna no existe).

---

## 5. LOS NÚMEROS HTTP (qué significa cada uno)
| Número | Significa |
|---|---|
| **200** OK | Todo bien (lectura/actualización) |
| **201** Created | Se creó algo nuevo |
| **202** Accepted | Aceptado |
| **204** No Content | Se hizo (ej: borrar), sin contenido que devolver |
| **400** Bad Request | Datos malos (validación falló) |
| **401** Unauthorized | No tienes credenciales válidas (login malo) |
| **403** Forbidden | No mandaste el token |
| **404** Not Found | No existe lo que pediste |
| **422** Unprocessable | Dato que rompe una regla de negocio |
| **500** Internal Error | Error inesperado del servidor |

---

## 6. PREGUNTAS ORALES (responde con tus palabras)
- **¿Qué son las 3 capas (CSR)?** "Controller recibe la petición HTTP y responde; Service tiene la
  lógica de negocio; Repository habla con la base de datos. Fluye Controller → Service → Repository."
- **¿Por qué la lógica va en el Service?** "Para que el Controller solo vea HTTP y la lógica quede
  en un solo lugar, ordenada y reutilizable."
- **¿Qué es Feign?** "Comunicación entre microservicios por HTTP. Declaro una interfaz y Spring hace
  la llamada. No comparto base de datos, consumo la API del otro."
- **¿Cómo es la seguridad?** "Stateless con JWT: login da un token, cada petición lo manda en la
  cabecera, un filtro lo valida."
- **¿Y los errores?** "Centralizados con @RestControllerAdvice, devolviendo el código HTTP correcto."
- **¿Y la base de datos?** "Hibernate no crea tablas (ddl-auto=none); las controla Flyway con
  migraciones versionadas (V1, V2...)."

---

### ⭐ Si te bloqueas en el examen, di esto en voz alta:
"Esto es un [log / validación / código HTTP / excepción / Feign / persistencia], así que voy al
[archivo según la tabla], lo modifico, guardo, reinicio la [Terminal 1 o 2] y lo pruebo en Postman."
Eso demuestra que entiendes el proceso, aunque te pongas nervioso.
