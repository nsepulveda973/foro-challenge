# Foro Hub API (Spring Boot 3 + JWT + H2)

API REST para el challenge **Foro Hub** de Alura. Incluye CRUD de tópicos, validaciones, persistencia con H2 y autenticación JWT.

## Requisitos
- Java 17+
- Maven 3.9+

## Cómo ejecutar
```bash
# 1) Clonar/subir a tu GitHub, o descargar el .zip y extraerlo
cd foro-hub-api

# 2) Compilar y ejecutar
mvn spring-boot:run
# o
mvn clean package && java -jar target/foro-hub-api-1.0.0.jar
```

La app inicia en `http://localhost:8080`.

## Swagger / OpenAPI
- Documentación interactiva: `http://localhost:8080/swagger-ui.html`
- JSON: `http://localhost:8080/v3/api-docs`

## Autenticación (JWT)
1. Regístrate:
```http
POST /auth/register
Content-Type: application/json

{ "username": "nicolas", "password": "tu_password" }
```
Respuesta:
```json
{ "token": "<JWT>" }
```

2. Login (si ya tienes usuario):
```http
POST /auth/login
Content-Type: application/json

{ "username": "nicolas", "password": "tu_password" }
```

3. Usa el token en las rutas protegidas:
```
Authorization: Bearer <JWT>
```

## Endpoints de Tópicos
- `POST /topics` Crea un tópico (body: `title`, `message`, `course`)
- `GET /topics` Lista paginada de tópicos (parámetros `page`, `size`, `sort`)
- `GET /topics/{id}` Obtiene un tópico por id
- `PUT /topics/{id}` Actualiza un tópico (body: `id`, `title`, `message`, `course`, `status`)
- `DELETE /topics/{id}` Elimina un tópico

**Reglas/validaciones:**
- No se permite crear un tópico con título + mensaje duplicados.
- Solo el autor puede actualizar o eliminar su tópico.
- Campos con `@NotBlank`, longitudes mínimas y máximas, etc.

## Base de datos
- H2 archivo en `./data/forohub`. Consola en `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:file:./data/forohub`, user: `sa`, sin contraseña).
- `data.sql` crea un usuario demo:
  - **usuario:** `demo`
  - **password:** `demo`

## Build para producción
```bash
mvn -DskipTests clean package
# genera target/foro-hub-api-1.0.0.jar
```

## Notas
- Cambia `security.jwt.secret` en `application.properties` para producción.
- Este proyecto evita complejidad extra (no usamos AuthenticationManager ni Flyway para acortar).
- Estructura de paquetes:
  - `user` (auth, usuarios)
  - `topic` (dominio de tópicos)
  - `security` (config y filtro JWT)
  - `common` (errores globales)
