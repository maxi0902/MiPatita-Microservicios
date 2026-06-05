# Chuleta de patrones — para APRENDER, no para pegar

> La idea: no memorices código. Aprende 6 "formas" cortitas y recuerda que **el proyecto ya
> tiene un ejemplo vivo de cada una**. En el examen, abre el ejemplo parecido y cópiale la forma
> cambiando los nombres. Eso es 100% válido y es lo que hacen los programadores.

---

## 🗺️ Tu chuleta REAL en el examen: dónde está cada ejemplo en el proyecto

| Si te piden... | Abre este archivo y copia la forma |
|---|---|
| Un **log** | `MiPatita .../service/MascotaService.java` (mira los `log.info`) |
| Una **validación** | `Mascotas .../model/Mascota.java` (mira `@NotBlank` en `nombre`) |
| Cambiar **código HTTP** | cualquier `.../controller/...Controller.java` (mira `ResponseEntity`) |
| Capturar una **excepción** | `MiPatita .../exception/GlobalExceptionHandler.java` (mira los `@ExceptionHandler`) |
| **Feign** | `MiPatita .../webclient/MascotaClient.java` (mira los métodos) |
| **Persistencia** | cualquier `model` + `MiPatita .../resources/db/migration/V1...sql` |

**Truco de oro:** en VS Code, **Ctrl + clic** sobre un nombre te lleva a su definición. Y
**Ctrl + Shift + F** busca una palabra en TODO el proyecto (ej: busca `@ExceptionHandler` y verás
todos los ejemplos).

---

## Los 6 patrones (la "forma" mínima)

### 1) LOG  →  va a la TERMINAL, no a Postman
- Si la clase ya tiene `log` (casi todos los Service lo tienen), solo escribe:
  `log.info("mi mensaje");`  o con un dato:  `log.info("valor: {}", variable);`
- Si la clase NO tiene logger, cópialo de otro Service (son 3 cosas): los 2 imports
  `org.slf4j.Logger` y `org.slf4j.LoggerFactory`, y la línea
  `private static final Logger log = LoggerFactory.getLogger(NombreDeLaClase.class);`

### 2) VALIDACIÓN  →  se ve en Postman como 400
- Pon UNA anotación **encima del campo** en el `model`:
  `@NotBlank` (texto obligatorio), `@NotNull` (no nulo), `@Min(1)` (número mínimo),
  `@Size(min=2, max=20)` (largo de texto).
- El controller ya tiene `@Valid`, así que solo agregas la anotación. Imita cómo está `nombre`.

### 3) CÓDIGO HTTP  →  se ve arriba a la derecha en Postman
- En el `controller`: `return ResponseEntity.status(HttpStatus.XXX).body(objeto);`
- XXX = `OK`(200) `CREATED`(201) `ACCEPTED`(202) `NO_CONTENT`(204) `BAD_REQUEST`(400) `NOT_FOUND`(404)
- Atajos: `ResponseEntity.ok(obj)` · `ResponseEntity.notFound().build()` · `ResponseEntity.noContent().build()`

### 4) EXCEPCIÓN  →  2 pasos
- En el `Service`, donde detectas el problema:  `throw new IllegalArgumentException("mensaje");`
- En `GlobalExceptionHandler`, copia un `@ExceptionHandler` que ya esté y cámbiale el tipo y el
  código HTTP (`HttpStatus.XXX`). La forma siempre es la misma.

### 5) FEIGN  →  hablar con el otro microservicio
- La "lista de llamadas" al otro servicio está en la interfaz `MascotaClient`. Cada método es un
  endpoint del microservicio Mascotas.
- Para usarlo desde un Service:  `mascotaClient.nombreDelMetodo(...)`
- Para agregar una llamada nueva: copia un método de la interfaz y cámbiale la ruta/nombre
  (ese endpoint debe existir en el `MascotaController` de Mascotas).

### 6) PERSISTENCIA  →  agregar un campo (SIEMPRE 2 cosas)
- En el `model` (entidad):  `private String miCampo;`
- En `db/migration` crea `V2__algo.sql` con:  `ALTER TABLE tabla ADD COLUMN mi_campo VARCHAR(255);`
- ⚠️ Si agregas el campo pero NO la migración, falla al guardar (la columna no existe).

---

## Lo que SIEMPRE debes saber hacer (independiente de lo que pidan)

1. **Reiniciar el servicio** tras cualquier cambio: clic en la terminal → **Ctrl + C** (→ S +
   Enter) → flecha **↑** + Enter.
2. **Saber qué editaste**: ¿MiPatita (Terminal 2, puerto 8080, pide token) o Mascotas
   (Terminal 1, puerto 8081, sin token)? Reinicia esa terminal.
3. **Dónde se ve el resultado**: el **log** va a la **terminal**; los **códigos y validaciones**
   se ven en **Postman**.

---

## Frases para la defensa oral (memoriza estas ideas, no palabra por palabra)

- **CSR:** "El Controller recibe la petición y responde; el Service tiene la lógica de negocio;
  el Repository habla con la base de datos. Una petición fluye Controller → Service → Repository."
- **Por qué la lógica va en el Service:** "Para que el Controller solo se preocupe de HTTP y la
  lógica quede en un solo lugar, reutilizable y fácil de probar."
- **Feign:** "Es comunicación síncrona entre microservicios. Declaro una interfaz y Spring hace la
  llamada HTTP. No comparto base de datos: consumo la API del otro servicio."
- **Seguridad:** "Es stateless con JWT: el login da un token firmado y cada petición lo manda en la
  cabecera; un filtro lo valida antes de dejar pasar."
- **Errores:** "Los centralizo con @RestControllerAdvice y devuelvo códigos HTTP semánticos."
- **Persistencia:** "Hibernate no crea tablas (ddl-auto=none); las controla Flyway con migraciones
  versionadas."
