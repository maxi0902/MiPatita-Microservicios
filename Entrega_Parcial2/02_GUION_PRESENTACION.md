# Guión de la Presentación — Parcial 2 (15 minutos)
## App MiPatita · Arquitectura de Microservicios

**Cómo usar este guión:** está dividido en **5 bloques**, uno por persona. Está escrito para
sonar **natural** (no lo lean palabra por palabra como robot; úsenlo de apoyo y digan las
ideas con sus palabras). Cada bloque dice **qué mostrar en pantalla** y dura **~3 minutos**.
Si exponen menos personas, junten bloques. Antes de empezar: **dejen los dos servicios
corriendo** (Mascotas en 8081 y MiPatita en 8080) y Postman abierto.

> Reparto sugerido (cámbienlo libremente):
> 1) Maximiliano · 2) Camila · 3) Rodrigo · 4) Constanza · 5) (quien quede)

---

### 🎤 BLOQUE 1 — Introducción y arquitectura general
**(Diapositivas: portada + diagrama de arquitectura)**

"Buenas, somos el equipo de **MiPatita**, una aplicación para el cuidado de mascotas: permite
llevar el registro de sus **paseos, comidas y recordatorios de vacunas**.

Para este hito, el desafío no era inventar la app, sino **cambiar su arquitectura**: pasarla de
un sistema único a una **arquitectura de microservicios**. ¿Qué significa eso? Que en vez de
tener un solo programa que hace absolutamente todo, lo dividimos en **dos servicios
independientes**, cada uno con **su propia base de datos**, que se comunican entre sí por HTTP.

Como ven en el diagrama, tenemos dos piezas:
- **MiPatita**, que corre en el puerto 8080. Es la puerta de entrada: maneja los **usuarios**,
  la **seguridad** y la lógica de **actividades y recordatorios**.
- **El microservicio de Mascotas**, en el puerto 8081, que se dedica **solo** a los datos de las
  mascotas.

La gracia es que están **desacoplados**: cada uno tiene su base de datos y no se meten en la
del otro. Si uno se cae o se cambia, el otro sigue funcionando. Ahora mi compañera les va a
mostrar cómo está hecho el microservicio de Mascotas por dentro."

---

### 🎤 BLOQUE 2 — Microservicio Mascotas: capas y persistencia
**(Diapositivas: patrón CSR + persistencia/Flyway. Mostrar el proyecto Mascotas en el editor)**

"Yo les voy a mostrar el **microservicio de Mascotas**. Está organizado con el patrón
**CSR: Controller – Service – Repository**, que es separar el trabajo en tres capas:

- El **Controller** recibe las peticiones HTTP (por ejemplo, 'dame la lista de mascotas').
- El **Service** tiene la lógica del negocio.
- El **Repository** es el que habla con la base de datos.

Cada capa tiene una sola responsabilidad; así el código queda ordenado y fácil de mantener.

Sobre la **base de datos**: usamos **Spring Data JPA con Hibernate** para mapear la clase
`Mascota` a la tabla `mascotas`. Un punto importante que pidió el profe: **no dejamos que
Hibernate cree o modifique las tablas solo** — eso es peligroso en producción. Lo apagamos con
`ddl-auto=none` y en su lugar usamos **Flyway**, que ejecuta **scripts SQL versionados**. Acá se
ve el script `V1__crear_tabla_mascotas.sql`: cada cambio futuro de la base sería un `V2`, un
`V3`, etc. Así el historial de la base queda controlado y es reproducible en cualquier
computador. De hecho, cuando arranca el servicio, Flyway crea la tabla sola. Le paso a mi
compañero para que muestre MiPatita."

---

### 🎤 BLOQUE 3 — MiPatita: usuarios, actividades y la capa Service
**(Diapositivas: CSR de MiPatita. Mostrar UsuarioService y ActividadService)**

"Ahora MiPatita, que es el servicio principal. Tiene la misma estructura en capas, pero acá vive
la **lógica del negocio de la app**: los **usuarios**, las **actividades** (los paseos y comidas)
y los **recordatorios**.

Quiero detenerme en algo que el profe valora harto: **la lógica de negocio va en la capa
Service, no en el Controller**. Por ejemplo, en `UsuarioService` está el método `autenticar`:
ahí buscamos al usuario, comparamos la contraseña encriptada con **BCrypt** y, si está correcta,
generamos el token. El Controller **no hace nada de eso**: solo recibe la petición y le pasa el
trabajo al Service. ¿Por qué? Porque así el Controller se preocupa solo de HTTP, y la lógica
queda en un solo lugar, reutilizable y fácil de testear.

Lo mismo en `ActividadService`: cuando alguien registra un paseo, el servicio primero **valida
que la mascota exista** (y eso lo hace preguntándole al otro microservicio, como van a ver
ahora) y recién ahí guarda la actividad en su repositorio. Te paso para la parte de
comunicación entre servicios."

---

### 🎤 BLOQUE 4 — Comunicación entre microservicios con Feign
**(Diapositivas: Feign. Mostrar MascotaClient y el método saveActividad)**

"Esta es una de las partes claves: **¿cómo se hablan los dos microservicios?** Usamos **Feign
Client**, que es un cliente HTTP declarativo de Spring Cloud. En vez de escribir el código de la
conexión a mano, declaramos una **interfaz** —`MascotaClient`— y le ponemos las anotaciones de
los endpoints del otro servicio. Spring se encarga del resto.

El ejemplo concreto está en `ActividadService`: antes de guardar un paseo, llamamos a
`mascotaClient.verificarMascota(id)`. Eso dispara una **petición HTTP real** desde MiPatita (8080)
hacia el microservicio de Mascotas (8081). Si la mascota existe, seguimos; si no, lanzamos un
error y **no se crea la actividad**. Esto es importante porque MiPatita **no entra a la base de
datos de Mascotas** —no podría, son independientes—, sino que **consume su API**. Así
mantenemos la consistencia de los datos sin compartir base de datos.

Otro detalle: la URL del otro servicio **no está quemada en el código**, está en el archivo de
configuración. Eso hace que sea fácil cambiarla si el servicio se mueve de dirección. Ahora el
cierre con la parte de seguridad y la demo."

---

### 🎤 BLOQUE 5 — Seguridad, manejo de errores, logs y DEMO en vivo
**(Diapositivas: seguridad/errores. Hacer la demo en Postman)**

"Cierro con la **seguridad** y una **demostración en vivo**.

La seguridad es **stateless con JWT**. ¿Qué significa? Que el servidor **no guarda sesiones**:
cuando te logueas, te devolvemos un **token** firmado, y en cada petición tú mandas ese token en
la cabecera. Spring Security tiene una **cadena de filtros** —nuestro `JwtAuthenticationFilter`—
que revisa ese token antes de dejarte pasar a las rutas protegidas. Las contraseñas, además,
nunca se guardan en texto plano: se cifran con **BCrypt**.

Para los errores usamos un manejo **centralizado** con `@RestControllerAdvice`: en un solo lugar
capturamos las excepciones y devolvemos el código HTTP correcto —400 si los datos están mal, 401
si las credenciales fallan, 404 si algo no existe—. Y para la **trazabilidad**, ambos servicios
escriben **logs en formato JSON** con SLF4J, así se pueden leer por máquina y seguir el camino de
una petición.

**[DEMO en Postman]**
1. Primero me **registro** y hago **login** → me devuelve el token. *(mostrar el 200 y el token)*
2. Si pido las mascotas **sin token** → me rechaza con **403**. *(mostrarlo)*
3. Con el token **sí** me deja, y la lista viene **desde el otro microservicio vía Feign**. *(200)*
4. Creo un **paseo** para una mascota que existe → **201 creado**.
5. Intento crear un paseo para una mascota que **no existe** → **400**, porque Feign avisó que no
   está. *(mostrarlo)*

Y eso es MiPatita: dos microservicios independientes, comunicados por Feign, con seguridad JWT,
validaciones, manejo de errores centralizado y logs estructurados. ¿Consultas?"

---

## Tips para la ronda de preguntas individuales
- Si el profe pide **agregar un log**: abre el `Service`, agrega `log.info("...")` y muestra que
  aparece en la consola en JSON.
- Si pide **una validación nueva**: agrega `@NotBlank`/`@Min` en el `model`, recompila y muéstralo
  con un **400** en Postman.
- Si pide **cambiar un código HTTP**: en el `Controller`, cambia el `ResponseEntity.status(...)`.
- Si pregunta **dónde está la lógica**: responde "en la capa **Service**", y muestra
  `UsuarioService` o `ActividadService`.
- Mantén la **calma**: si no sabes algo, explica **dónde lo buscarías** en el proyecto.
