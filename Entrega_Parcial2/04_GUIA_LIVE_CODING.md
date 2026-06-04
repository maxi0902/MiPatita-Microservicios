# Guía de Live Coding — paso a paso (para principiantes)

> Esta guía te prepara para la parte de **modificación de código en vivo** de la defensa
> (es el **48%** de tu nota). Está escrita asumiendo que **no sabes nada todavía**.
> Léela con el proyecto abierto y ve haciendo cada paso.

---

# PARTE A — Preparar el entorno (hazlo una vez)

## A1. Abrir el proyecto en VS Code
1. Abre **Visual Studio Code**.
2. Menú **Archivo → Abrir carpeta...** (File → Open Folder).
3. Elige la carpeta: `C:\Users\Maxit\Documents\Fullstack`
4. A la izquierda verás las carpetas `MiPatita_V3`, `MicroservicioMascotas _V2` y `Entrega_Parcial2`.

## A2. Cerrar Expo/Metro (libera el puerto 8081)
El microservicio de Mascotas usa el puerto **8081**. Si tienes corriendo tu app BienHecho
(Expo/Metro), ese puerto está ocupado y Mascotas **no arrancará**.
- Busca la ventana/terminal donde corre Expo y ciérrala (Ctrl + C, o cierra la ventana).

## A3. Abrir DOS terminales en VS Code
- Menú **Terminal → Nueva Terminal** (Terminal → New Terminal). Se abre abajo.
- Vuelve a **Terminal → Nueva Terminal** otra vez para tener **dos**.
- Puedes cambiar entre ellas con la lista desplegable de la esquina del panel de terminal.

> Las terminales en Windows abren **PowerShell**. Los comandos de abajo son para PowerShell.

## A4. Arrancar el microservicio MASCOTAS (Terminal 1)
En la **primera** terminal, escribe (o copia y pega) estas dos líneas, una por una:
```powershell
cd "C:\Users\Maxit\Documents\Fullstack\MicroservicioMascotas _V2"
.\mvnw.cmd spring-boot:run
```
Espera ~15 segundos. Está listo cuando ves una línea con:
`Tomcat started on port 8081` y `Started DemoApplication`.

## A5. Arrancar MIPATITA (Terminal 2)
Cambia a la **segunda** terminal y escribe:
```powershell
cd "C:\Users\Maxit\Documents\Fullstack\MiPatita_V3\demo"
.\mvnw.cmd spring-boot:run
```
Listo cuando ves `Tomcat started on port 8080` y `Started DemoApplication`.

✅ **Ahora tienes los dos microservicios corriendo.** Déjalos así.

## A6. Cómo DETENER y REINICIAR un servicio (¡clave para el live coding!)
Cada vez que cambies código, tienes que **reiniciar** el servicio de esa parte:
1. Haz clic en la terminal del servicio que quieres reiniciar.
2. Presiona **Ctrl + C**. Si pregunta `¿Terminar trabajo por lotes (S/N)?`, escribe **S** y Enter.
3. Presiona la **flecha hacia arriba ↑** (trae el último comando) y **Enter** para arrancarlo de nuevo.

> Regla de oro: si editas un archivo de **MiPatita** → reinicia la **Terminal 2**.
> Si editas un archivo de **Mascotas** → reinicia la **Terminal 1**.

---

# PARTE B — Usar Postman (probar la API)

## B1. Abrir Postman
- Abre la app **Postman**. Si te pide cuenta, puedes hacer clic en "Continuar" / saltar el login.
- Arriba a la izquierda, haz clic en **New** (o el botón **+**) para crear una petición nueva.

## B2. La regla de oro de la seguridad
Casi todas las rutas de MiPatita necesitan un **token**. El orden SIEMPRE es:
**1) Registrarse → 2) Login (te da el token) → 3) Usar el token en las demás peticiones.**

## B3. Paso 1 — Registrar un usuario
1. En la petición: elige el método **POST** (menú desplegable a la izquierda de la URL).
2. URL: `http://localhost:8080/api/v1/auth/registro`
3. Pestaña **Body** → marca **raw** → a la derecha cambia "Text" por **JSON**.
4. Pega esto en el cuadro grande:
```json
{
  "username": "maxi",
  "password": "1234",
  "role": "USER"
}
```
5. Botón azul **Send**. Abajo debe aparecer `Usuario registrado exitosamente` y **Status: 200 OK**.

## B4. Paso 2 — Login (obtener el token)
1. Método **POST**, URL: `http://localhost:8080/api/v1/auth/login`
2. Body → raw → JSON:
```json
{ "username": "maxi", "password": "1234" }
```
3. **Send**. La respuesta será algo como:
```json
{ "token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOi....." }
```
4. **Copia** ese texto largo del token (sin las comillas).

## B5. Paso 3 — Usar el token (pedir las mascotas)
1. Método **GET**, URL: `http://localhost:8080/api/v1/mascotas`
2. Pestaña **Authorization** → en "Type" elige **Bearer Token** → pega el token en el cuadro de la derecha.
3. **Send**. Debe devolver la lista de 5 mascotas y **200 OK**.

> Si pides esto **sin** el token, da **403 Forbidden**. Eso es la seguridad funcionando.

> Atajo: el microservicio de **Mascotas (puerto 8081) NO pide token**. Para probar cosas de
> Mascotas, puedes pegarle directo a `http://localhost:8081/api/v1/mascotas` sin Authorization.

---

# PARTE C — Los 6 ejercicios de práctica

> Practica cada uno hasta que te salga solo. En cada ejercicio dice: qué te pueden pedir,
> qué archivo abrir, qué escribir, cómo reiniciar y cómo probarlo en Postman.

---

## EJERCICIO 1 — Inyectar un LOG  (IE 2.3.4, 18%)
**Te pueden pedir:** "Agrega un log que avise cuándo se listan las actividades."

**Archivo:** `MiPatita_V3/demo/src/main/java/com/exampleMiPatita/demo/service/ActividadService.java`

**Receta para agregar un log a CUALQUIER clase:**
1. Arriba, junto a los otros `import`, agrega estas dos líneas:
```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```
2. Dentro de la clase (justo después de `public class ActividadService {`), agrega el "logger":
```java
    private static final Logger log = LoggerFactory.getLogger(ActividadService.class);
```
3. Ahora ya puedes escribir logs. En el método `getActividades()`, antes del `return`, pon:
```java
        log.info("Se está consultando el listado de actividades");
```

**Reiniciar:** Terminal 2 (MiPatita) → Ctrl+C → ↑ → Enter.

**Probar en Postman:** GET `http://localhost:8080/api/v1/actividades` con el token (Bearer).
**Mira la Terminal 2:** aparecerá tu log en JSON:
`{"timestamp":"...","level":"INFO","service":"mipatita","logger":"...ActividadService","message":"Se está consultando el listado de actividades"}`

**Cómo defenderlo:** "Usé SLF4J. El logger se crea una vez por clase y escribe en formato JSON
gracias a la configuración de Logback. Uso `log.info` para el flujo normal."

---

## EJERCICIO 2 — Agregar una VALIDACIÓN  (IE 2.2.5, 15%)
**Te pueden pedir:** "Haz que la raza de la mascota sea obligatoria."

**Archivo:** `MicroservicioMascotas _V2/src/main/java/com/exampleMascotas/demo/model/Mascota.java`

**Qué hacer:** busca la línea `private String raza;` y justo **encima** agrégale la anotación:
```java
    @NotBlank(message = "La raza es obligatoria")
    private String raza;
```
(En este archivo el `import jakarta.validation.constraints.NotBlank;` ya existe arriba, así que no
hay que agregarlo. Si faltara, se agrega como en el Ejercicio 1.)

**Reiniciar:** Terminal 1 (Mascotas) → Ctrl+C → ↑ → Enter.

**Probar en Postman:** POST `http://localhost:8081/api/v1/mascotas` (Mascotas NO pide token),
Body → raw → JSON, manda una mascota **sin raza**:
```json
{ "nombre": "Toby", "peso": 5.0, "edad": 2 }
```
**Resultado esperado:** **400 Bad Request** con `{"raza":"La raza es obligatoria"}`.

**Otras validaciones que puedes usar:** `@NotNull` (no nulo), `@Min(1)` (número mínimo),
`@Size(min=2, max=20)` (largo de texto), `@NotBlank` (texto no vacío).

**Cómo defenderlo:** "Es Bean Validation. La anotación `@NotBlank` la revisa Spring porque el
controlador tiene `@Valid`. Si falla, el `@RestControllerAdvice` devuelve un 400 con el mensaje."

---

## EJERCICIO 3 — Cambiar un CÓDIGO HTTP  (IE 2.3.4)
**Te pueden pedir:** "Haz que crear una mascota responda 202 en vez de 201."

**Archivo:** `MicroservicioMascotas _V2/src/main/java/com/exampleMascotas/demo/controller/MascotaController.java`

**Qué hacer:** en el método `guardarMascota`, cambia esta línea:
```java
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaMascota);   // 201
```
por:
```java
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(nuevaMascota);  // 202
```

**Reiniciar:** Terminal 1 (Mascotas).
**Probar:** POST `http://localhost:8081/api/v1/mascotas` con una mascota válida →
ahora responde **202 Accepted**.
(Para volver a dejarlo bien, cambia `ACCEPTED` de nuevo por `CREATED`.)

**Códigos que debes conocer:** `OK`=200, `CREATED`=201, `ACCEPTED`=202, `NO_CONTENT`=204,
`BAD_REQUEST`=400, `UNAUTHORIZED`=401, `NOT_FOUND`=404.

**Cómo defenderlo:** "El código HTTP lo controla el `ResponseEntity` en el Controller. 201 es
'creado', 202 es 'aceptado'. Así devuelvo códigos semánticos según la situación."

---

## EJERCICIO 4 — Capturar una EXCEPCIÓN con código personalizado  (IE 2.3.4)
**Te pueden pedir:** "Si la frecuencia de un recordatorio es mayor a 365 días, recházalo
con un código 422 y un mensaje."

**Paso 1 — agregar el manejador.** Archivo:
`MiPatita_V3/demo/src/main/java/com/exampleMiPatita/demo/exception/GlobalExceptionHandler.java`
Dentro de la clase, agrega este método nuevo (debajo de los que ya están):
```java
    // 422 -> cuando un dato rompe una regla de negocio
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> manejarReglaNegocio(IllegalArgumentException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY); // 422
    }
```

**Paso 2 — lanzar la excepción.** Archivo:
`MiPatita_V3/demo/src/main/java/com/exampleMiPatita/demo/service/RecordatorioService.java`
Dentro del método `saveRecordatorio`, al inicio (después de la primera `{`), agrega:
```java
        if (recordatorio.getFrecuenciaDias() != null && recordatorio.getFrecuenciaDias() > 365) {
            throw new IllegalArgumentException("La frecuencia no puede superar los 365 días");
        }
```

**Reiniciar:** Terminal 2 (MiPatita).
**Probar en Postman:** POST `http://localhost:8080/api/v1/recordatorios` con token, Body JSON:
```json
{ "idMascota": 1, "tipoRecordatorio": "Vacuna", "frecuenciaDias": 500, "fechaProxima": "2026-08-01" }
```
**Resultado:** **422** con `{"error":"La frecuencia no puede superar los 365 días"}`.

**Cómo defenderlo:** "El Service lanza la excepción (la lógica de negocio va ahí). El
`@RestControllerAdvice` la captura en un solo lugar y la traduce al código HTTP 422. Así no
repito try/catch en cada controlador."

---

## EJERCICIO 5 — Modificar la COMUNICACIÓN entre microservicios (Feign)  (IE 2.4.4, 15%)
**Te pueden pedir:** "Cuando MiPatita reciba las mascotas del otro servicio, que registre en el
log el nombre de cada una."

**Archivo:** `MiPatita_V3/demo/src/main/java/com/exampleMiPatita/demo/service/MascotaService.java`
(Este servicio NO toca su base de datos: pide los datos al otro microservicio con `mascotaClient`,
que es el **Feign Client**.)

**Qué hacer:** en el método `getMascotas()`, después de la línea
`List<Mascota> lista = mascotaClient.listarMascotas();` agrega:
```java
            lista.forEach(m -> log.info("Mascota recibida vía Feign: {}", m.getNombre()));
```

**Reiniciar:** Terminal 2 (MiPatita).
**Probar:** GET `http://localhost:8080/api/v1/mascotas` con token. En la **Terminal 2** verás un
log por cada mascota con su nombre (los datos vienen del microservicio 8081, no de la BD de MiPatita).

**Para AGREGAR un llamado nuevo al otro servicio** (más avanzado): se agrega un método en la
interfaz `MascotaClient` (archivo `webclient/MascotaClient.java`) con la anotación del endpoint
(`@GetMapping(...)`), y ese endpoint debe existir en el controlador del microservicio Mascotas.

**Cómo defenderlo:** "La comunicación es síncrona con Feign: declaro una interfaz y Spring hace la
llamada HTTP. Aquí consumo (mapeo) la información remota —los nombres— sin compartir base de datos."

---

## EJERCICIO 6 — Modificar la PERSISTENCIA: agregar un campo + migración  (IE 2.1.4, 10%)
**Te pueden pedir:** "Agrega un campo 'notas' al recordatorio."

> ⚠️ Regla importante: como usamos `ddl-auto=none`, Hibernate **no** crea la columna solo.
> Hay que agregar el campo en la **entidad** Y crear una **migración de Flyway** para la columna.
> Si solo cambias la entidad, al guardar dará error (la columna no existe).

**Paso 1 — agregar el campo a la entidad.** Archivo:
`MiPatita_V3/demo/src/main/java/com/exampleMiPatita/demo/model/Recordatorio.java`
Antes de la última `}` de la clase, agrega:
```java
    private String notas;
```

**Paso 2 — crear la migración.** En la carpeta
`MiPatita_V3/demo/src/main/resources/db/migration/` crea un archivo nuevo llamado
**`V2__agregar_notas_a_recordatorio.sql`** (clic derecho en la carpeta → New File) con este contenido:
```sql
ALTER TABLE recordatorios ADD COLUMN notas VARCHAR(255);
```

**Reiniciar:** Terminal 2 (MiPatita). Al arrancar verás que Flyway aplica la migración "V2".

**Probar en Postman:** POST `http://localhost:8080/api/v1/recordatorios` con token:
```json
{ "idMascota": 1, "tipoRecordatorio": "Baño", "frecuenciaDias": 15, "fechaProxima": "2026-07-01", "notas": "Usar shampoo especial" }
```
**Resultado:** **201** y en la respuesta aparece el campo `notas`. Si haces GET, también sale.

**Cómo defenderlo:** "Cambié la entidad y, como las tablas las controla Flyway, agregué la
migración versionada V2 con un ALTER TABLE. Así el esquema queda versionado y el sistema sigue
persistiendo correctamente."

---

# PARTE D — Chuleta rápida (para tener al lado en la defensa)

| Si te piden... | Abre este tipo de archivo | Anotación / código clave |
|---|---|---|
| Un **log** | `service/...Service.java` | `log.info("...")` (+ logger arriba) |
| Una **validación** | `model/...java` | `@NotBlank`, `@NotNull`, `@Min`, `@Size` |
| Cambiar **código HTTP** | `controller/...Controller.java` | `ResponseEntity.status(HttpStatus.XXX)` |
| Manejar una **excepción** | `exception/GlobalExceptionHandler.java` | `@ExceptionHandler(...)` |
| **Feign** (entre servicios) | `webclient/MascotaClient.java` o `service/MascotaService.java` | `@FeignClient`, `mascotaClient....` |
| **Persistencia** (campo nuevo) | `model/...java` + `db/migration/V2__...sql` | `private Tipo campo;` + `ALTER TABLE` |

**Las 3 capas (CSR) — pregunta segura del profe:**
- **Controller** → recibe la petición HTTP, valida con `@Valid`, responde con `ResponseEntity`.
- **Service** → la lógica de negocio (aquí va el "pensar"). *Ej: `UsuarioService.autenticar()`.*
- **Repository** → habla con la base de datos (`extends JpaRepository`).
Una petición fluye: **Controller → Service → Repository** (y vuelve).

**Puertos:** MiPatita = **8080** (pide token) · Mascotas = **8081** (no pide token).

**Si algo se rompe:** lee el error rojo en la terminal; suele decir el archivo y la línea.
Si no compila, revisa que no te falte un `;`, una llave `}` o un `import`.
