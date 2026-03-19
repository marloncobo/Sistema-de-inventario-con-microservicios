# Sistema de Inventario basado en Microservicios

Backend reactivo con Spring Boot, Spring Cloud Gateway, WebFlux, R2DBC y PostgreSQL.

## Estructura actual

- `api-gateway`: unico punto de entrada. Valida JWT y reenvia peticiones a los demas servicios.
- `users-service`: autenticacion y gestion basica de usuarios.
- `inventory-service`: categorias, productos, movimientos y reporte de movimientos.

Cada modulo tiene una sola implementacion activa. Se eliminaron clases duplicadas, paquetes viejos y una app raiz que ya no tenia funcion.

## Stack

- Java 21
- Spring Boot 3.2.4
- Spring Cloud Gateway
- Spring WebFlux
- Spring Data R2DBC
- PostgreSQL
- Docker Compose

## Levantar con Docker

1. Compila los jars:

```powershell
.\mvnw.cmd clean package
```

2. Construye y levanta los contenedores:

```powershell
docker compose up --build
```

Servicios expuestos:

- `api-gateway`: `http://localhost:8080`
- `users-db`: `localhost:5432`
- `inventory-db`: `localhost:5433`

Usuario inicial:

- `username`: `admin`
- `password`: `admin123`

## Flujo recomendado de pruebas en Postman

Usa siempre el gateway en `http://localhost:8080`.

### 1. Login

`POST /auth/login`

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta esperada:

```json
{
  "token": "..."
}
```

Guarda ese valor en una variable de coleccion, por ejemplo `token`.

### 2. Crear usuario

`POST /api/users`

Header:

- `Authorization: Bearer {{token}}`

Body:

```json
{
  "username": "operario",
  "password": "operario123",
  "role": "USER"
}
```

Respuesta esperada: usuario creado con `id`.

### 3. Consultar categorias

`GET /api/categories`

Header:

- `Authorization: Bearer {{token}}`

### 4. Consultar productos

`GET /api/products`

Header:

- `Authorization: Bearer {{token}}`

Toma el `id` del producto `Laptop` para las siguientes pruebas.

### 5. Registrar entrada de inventario

`POST /api/inventory/movements`

Header:

- `Authorization: Bearer {{token}}`

Body:

```json
{
  "productId": "UUID_DEL_PRODUCTO",
  "type": "ENTRY",
  "quantity": 10
}
```

### 6. Registrar salida de inventario

`POST /api/inventory/movements`

Header:

- `Authorization: Bearer {{token}}`

Body:

```json
{
  "productId": "UUID_DEL_PRODUCTO",
  "type": "EXIT",
  "quantity": 5
}
```

### 7. Ver reporte de movimientos

`GET /api/reports/movements`

Header:

- `Authorization: Bearer {{token}}`

## Sugerencias para Postman

- Crea una variable `baseUrl` con valor `http://localhost:8080`.
- Crea una variable `token`.
- En las peticiones protegidas usa `Authorization: Bearer {{token}}`.
- En el script del login puedes guardar el token automaticamente:

```javascript
const json = pm.response.json();
pm.collectionVariables.set("token", json.token);
```

## Notas

- `users-service` y `inventory-service` cargan `schema.sql` y `data.sql` al iniciar.
- El gateway agrega `X-User-Id` y `X-User-Role` a las peticiones autenticadas.
- Se agregaron pruebas unitarias base para `AuthService` e `InventoryService`.
