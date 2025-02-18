# E-Commerce Microservices Project

Bu proje, modern e-ticaret uygulaması için mikroservis mimarisi kullanılarak geliştirilmiş bir örnektir.

## Mimari Yapı

Proje aşağıdaki mikroservisleri içermektedir:

- **Discovery Service** (Port: 8761)
  - Eureka Server
  - Servis kayıt ve keşif

- **Product Service** (Port: 8080)
  - Ürün yönetimi
  - Stok kontrolü
  - Circuit Breaker pattern
  - Kafka entegrasyonu

- **Order Service** (Port: 8081)
  - Sipariş yönetimi
  - Asenkron iletişim (Kafka)

- **User Service** (Port: 8082)
  - Kullanıcı yönetimi
  - JWT tabanlı authentication
  - Role-based authorization

## Teknolojiler

- Java 11
- Spring Boot 2.7.0
- Spring Cloud
- Spring Security
- PostgreSQL
- Apache Kafka
- Docker
- JWT
- Resilience4j
- Maven

## Başlangıç

### Gereksinimler

- Java 11 veya üzeri
- Docker ve Docker Compose
- Maven

### Kurulum

1. Repository'yi klonlayın:
```bash
git clone https://github.com/mcay51/microserviceeticaret.git
cd microserviceeticaret
```

2. Maven ile projeyi derleyin:
```bash
mvn clean package -DskipTests
```

3. Docker containerları başlatın:
```bash
docker-compose up -d
```

### Servis Portları

- Eureka Server: http://localhost:8761
- Product Service: http://localhost:8080
- Order Service: http://localhost:8081
- User Service: http://localhost:8082
- Kafka: localhost:9092
- PostgreSQL Databases:
  - Product DB: localhost:5432
  - Order DB: localhost:5433
  - User DB: localhost:5434

## API Endpoints

### User Service

```
POST /api/auth/login
- Kullanıcı girişi ve JWT token alma

POST /api/auth/register
- Yeni kullanıcı kaydı
```

### Product Service

```
GET /api/products
- Tüm ürünleri listele

GET /api/products/{id}
- Ürün detayı görüntüleme

POST /api/products
- Yeni ürün ekleme

PUT /api/products/{id}
- Ürün güncelleme

DELETE /api/products/{id}
- Ürün silme
```

### Order Service

```
POST /api/orders
- Yeni sipariş oluşturma

GET /api/orders/{id}
- Sipariş detayı görüntüleme

GET /api/orders/user/{userId}
- Kullanıcının siparişlerini listele
```

## Güvenlik

- JWT tabanlı authentication
- Role-based authorization
- Stateless session yönetimi
- CORS configuration
- SSL/TLS desteği

## Mimari Özellikler

1. **Service Discovery**
   - Eureka Server ile dinamik servis keşfi
   - Load balancing

2. **Circuit Breaker**
   - Resilience4j implementasyonu
   - Fallback mekanizmaları
   - Hata toleransı

3. **Event-Driven Architecture**
   - Kafka ile asenkron iletişim
   - Event sourcing
   - Eventual consistency

4. **Database per Service**
   - Her servis için ayrı veritabanı
   - Veri izolasyonu
   - Bağımsız ölçeklendirme

## Monitoring ve Logging

- Actuator endpoints
- Centralized logging
- Health check endpoints
- Kafka monitoring

## Katkıda Bulunma

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır - Detaylar için [LICENSE](LICENSE) dosyasına bakın.

## İletişim

Mustafa Çay - [@mcay51](https://github.com/mcay51)

Proje Linki: [https://github.com/mcay51/microserviceeticaret](https://github.com/mcay51/microserviceeticaret) 