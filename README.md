# PetShop Sales Service

Microservicio de ventas y punto de venta (POS).

## Información Técnica

| Propiedad | Valor |
|-----------|-------|
| Puerto | 8083 |
| Esquema BD | `sales` |
| Base de datos | PostgreSQL (petshop) |
| Framework | Spring Boot 3.3.0 |
| Java | 21 |
| Service Discovery | Eureka |

## Endpoints

### Ventas

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/sales` | Listar todas las ventas |
| GET | `/api/sales/{id}` | Obtener venta por ID |
| POST | `/api/sales` | Crear venta (con items) |
| GET | `/api/sales/customer/{customerId}` | Ventas por cliente |
| GET | `/api/sales/invoice/{invoiceNumber}` | Venta por número de factura |

## DTOs

### SaleRequest
```json
{
  "customerId": 1,
  "totalAmount": 150000,
  "taxAmount": 28500,
  "discountAmount": 0,
  "finalAmount": 178500,
  "paymentMethod": "CASH | CREDIT_CARD | DEBIT_CARD | TRANSFER",
  "items": [
    {
      "productSku": "SKU-001",
      "productName": "Alimento Royal Canin",
      "quantity": 2,
      "unitPrice": 75000
    }
  ]
}
```

### SaleResponse
```json
{
  "id": 1,
  "invoiceNumber": "INV-20260718-0001",
  "customerId": 1,
  "totalAmount": 150000,
  "taxAmount": 28500,
  "discountAmount": 0,
  "finalAmount": 178500,
  "paymentMethod": "CASH",
  "status": "COMPLETED",
  "items": [
    {
      "id": 1,
      "saleId": 1,
      "productSku": "SKU-001",
      "productName": "Alimento Royal Canin",
      "quantity": 2,
      "unitPrice": 75000,
      "subtotal": 150000
    }
  ],
  "active": true,
  "createdAt": "2026-07-18T17:00:00",
  "updatedAt": "2026-07-18T17:00:00"
}
```

## Ejecución

```bash
mvn spring-boot:run
```

## Docker

```bash
docker build -t petshop-sales .
docker run -p 8083:8083 petshop-sales
```
