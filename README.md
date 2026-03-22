# Inventory Store Microservices

Backend reactivo con Spring Boot, Spring Cloud Gateway, WebFlux, R2DBC y PostgreSQL para una tienda con autenticacion, catalogo, stock y ventas.

## Arquitectura

- `api-gateway`: entrada unica del sistema, valida JWT y enruta peticiones.
- `auth-service`: login y registro de usuarios.
- `catalog-service`: categorias y productos del catalogo.
- `inventory-service`: stock y movimientos de inventario.
- `sales-service`: ordenes de venta.

Puertos locales:

- `api-gateway`: `8080`
- `auth-service`: `8081`
- `catalog-service`: `8082`
- `inventory-service`: `8083`
- `sales-service`: `8086`

## Levantar localmente

1. Compilar:

```powershell
.\mvnw.cmd clean package
```

2. Levantar todo:

```powershell
docker compose up --build
```

Bases de datos expuestas:

- `auth-db`: `localhost:5432`
- `catalog-db`: `localhost:5433`
- `inventory-db`: `localhost:5434`
- `sales-db`: `localhost:5437`

Usuario inicial:

- `username`: `admin`
- `password`: `admin123`

## Endpoints via gateway

Todos se consumen desde `http://localhost:8080`.

Publico:

- `POST /auth/login`
- `POST /auth/register`

Protegidos:

- `GET|POST /api/catalog/categories`
- `GET|POST /api/catalog/products`
- `POST /api/inventory/movements`
- `GET /api/reports/movements`
- `GET|POST /api/sales/orders`

## Flujo rapido de prueba

1. Login:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

2. Crear categoria:

```json
{
  "name": "Office",
  "description": "Office supplies"
}
```

3. Crear producto de catalogo:

```json
{
  "sku": "SKU-CHA-010",
  "name": "Office Chair",
  "description": "Ergonomic chair",
  "categoryId": "UUID_DE_CATEGORIA",
  "unitPrice": 450.00,
  "reorderLevel": 6
}
```

4. Registrar movimiento de inventario:

```json
{
  "productId": "UUID_DEL_PRODUCTO_STOCK",
  "type": "ENTRY",
  "quantity": 10
}
```

5. Crear orden de venta:

```json
{
  "reference": "SO-9001",
  "salesChannel": "STORE",
  "totalAmount": 1290.00
}
```

## Despliegue en Google Cloud

Cada servicio tiene su pipeline de Cloud Build:

- `cloudbuild-auth.yaml`
- `cloudbuild-catalog.yaml`
- `cloudbuild-inventory.yaml`
- `cloudbuild-sales.yaml`
- `cloudbuild-gateway.yaml`

Orden recomendado:

1. Desplegar servicios backend.
2. Guardar las URLs resultantes de Cloud Run.
3. Desplegar `api-gateway` pasando las URLs con sustituciones.

Sustituciones necesarias del gateway:

- `_AUTH_URL`
- `_CATALOG_URL`
- `_INVENTORY_URL`
- `_SALES_URL`
- `_JWT_SECRET`
- `_REGION`
- `_REPO_NAME`

Sustituciones necesarias para cada microservicio con base de datos:

- `_REGION`
- `_REPO_NAME`
- `_DB_IP`
- `_DB_USER`
- `_DB_PASS`

## Notas

- Cada microservicio carga su propio `schema.sql` y `data.sql`.
- El gateway agrega `X-User-Id` y `X-User-Role` a las peticiones autenticadas.
- Para que el gateway pueda comunicarse con Cloud Run sin agregar IAM invoker en esta iteracion, los microservicios quedaron desplegados como accesibles por URL.
