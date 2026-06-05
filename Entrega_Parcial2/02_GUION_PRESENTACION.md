# Guión de la Presentación — MiPatita (15 minutos)
## Versión fácil de explicar · 4 partes asignadas por dificultad

**Reparto:**
| Parte | Quién | Dificultad | Tema |
|---|---|---|---|
| 1 | **Rodrigo** | 🟢 Fácil | Introducción + qué es la app + diagrama |
| 2 | **Camila** | 🟡 Media | Microservicio Mascotas + las 3 capas + base de datos |
| 3 | **Constanza** | 🟡 Media | MiPatita + validaciones + seguridad |
| 4 | **Maxi** | 🔴 Difícil | Comunicación (Feign) + manejo de errores + logs |

**Antes de empezar:** dejen los **dos servicios corriendo** (Mascotas 8081 y MiPatita 8080) y el
**proyecto abierto en el editor** para mostrar el código cuando toque.
> No lean palabra por palabra: úsenlo de apoyo y digan las ideas con sus palabras.

---

### 🟢 PARTE 1 — RODRIGO (fácil) · ~3 min
**Diapositivas: portada + diagrama de arquitectura**

"Hola, somos el equipo de **MiPatita**. Nuestra app sirve para **cuidar mascotas**: anotar sus
paseos, sus comidas y los recordatorios de las vacunas.

Para esta entrega el desafío fue cambiar **cómo está armada la app por dentro**. En vez de tener
un solo programa que hace todo, la **dividimos en dos programas** que trabajan juntos. A eso se le
llama **microservicios**.

*(mostrar el diagrama)*

Como ven en la imagen, son dos partes:
- La primera, **MiPatita**, es la puerta de entrada: maneja los **usuarios**, la **seguridad**, y
  las **actividades y recordatorios**.
- La segunda, el **microservicio de Mascotas**, se encarga **solo de los datos de las mascotas**.

Cada una tiene **su propia base de datos** y se comunican entre ellas por internet. La ventaja:
si una falla, la otra sigue funcionando. Ahora **Camila** les cuenta cómo está hecho el
microservicio de Mascotas por dentro."

---

### 🟡 PARTE 2 — CAMILA (media) · ~3.5 min
**Mostrar el proyecto Mascotas en el editor + diapositiva de capas**

"Yo les muestro el **microservicio de Mascotas**. Está ordenado en **tres capas**, que es una
forma de separar el trabajo:
- El **Controller**, que recibe las peticiones (por ejemplo: 'dame la lista de mascotas').
- El **Service**, que tiene la lógica.
- El **Repository**, que es el que habla con la base de datos.

Así cada parte hace **una sola cosa** y el código queda ordenado y fácil de mantener.

Sobre la **base de datos**: usamos una herramienta que se llama **JPA**, que conecta las clases de
Java con las tablas. Y algo importante que pidió el profe: **no dejamos que el programa cree las
tablas solo**, porque eso es riesgoso. En su lugar usamos **Flyway**, que crea las tablas con
**archivos ordenados por versión** (V1, V2, y así). Por eso el historial de la base queda
controlado y se puede repetir en cualquier computador.

Ahora **Constanza** les muestra MiPatita."

---

### 🟡 PARTE 3 — CONSTANZA (media) · ~3.5 min
**Mostrar MiPatita + diapositiva de seguridad**

"Ahora **MiPatita**, el servicio principal. Acá viven los **usuarios**, las **actividades**
(los paseos y comidas) y los **recordatorios**. Usa las mismas tres capas.

Quiero destacar **dos cosas**:

**Primero, las validaciones.** Antes de guardar algo, revisamos que los datos vengan bien. Por
ejemplo, una actividad **no se puede guardar sin la fecha**. Si falta un dato obligatorio, la app
responde con un **error claro** y no guarda datos malos.

**Segundo, la seguridad.** Usamos algo que se llama **JWT**. Funciona así: cuando te **logueas**,
te entregamos un **token**, que es como una **pulsera de acceso**. En cada petición tienes que
mostrar ese token; si no lo tienes, **no entras**. Además, las **contraseñas se guardan
encriptadas**, nunca en texto normal.

Ahora **Maxi** les muestra la parte más interesante: cómo conversan los dos microservicios."

---

### 🔴 PARTE 4 — MAXI (difícil) · ~3.5 min
**Mostrar MascotaClient y el manejo de errores en el editor**

"Cierro yo con la parte más técnica: **cómo se comunican los dos microservicios**.

Usamos **Feign**, que es una forma de que MiPatita le **hable** al microservicio de Mascotas por
internet. El ejemplo concreto: **antes de guardar un paseo**, MiPatita le pregunta al otro:
*'¿existe esta mascota?'*. Si no existe, **no deja crear el paseo**. Lo importante es que MiPatita
**no entra a la base de datos del otro**: solo le **pregunta** por su API. Así no comparten datos
directamente, y se mantienen independientes.

Para los **errores**, los manejamos **todos en un solo lugar**. Según lo que pase, devolvemos el
código correcto: **400** si los datos están malos, **401** si las credenciales fallan, **404** si
algo no existe. Y para revisar qué pasa por dentro, ambos servicios escriben **registros (logs)**
en un formato ordenado, lo que nos permite seguir el recorrido de una petición.

Para cerrar: todo esto funciona en conjunto. Un usuario se loguea y recibe su **token**; con ese
token puede pedir las mascotas (que vienen del otro microservicio) y registrar sus paseos y
recordatorios, siempre **validando los datos**. El sistema está **probado y funcionando**.

Y eso es **MiPatita**: dos microservicios independientes, comunicados por Feign, con seguridad,
validaciones y manejo de errores. ¿Consultas?"

---

## 🎤 Después de la presentación: preguntas individuales
El profe le pregunta a **cada uno por separado** y pide **modificar código en vivo**. Cada
integrante debe poder explicar **su parte** y hacer un cambio chico (un log, una validación, un
código HTTP). La app **debe estar corriendo** en el PC del profe para esta parte.

Repasen su parte + la chuleta (`05_CHULETA_PATRONES.md`) y el mapa (`06_MAPA_EXAMEN.md`).
