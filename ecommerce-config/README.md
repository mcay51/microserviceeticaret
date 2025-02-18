# E-Commerce Configuration Repository

Bu repository, e-ticaret mikroservis uygulamasının merkezi konfigürasyon yönetimi için kullanılmaktadır.

## Konfigürasyon Dosyaları

- `product-service.yml` - Ürün servisi konfigürasyonları
- `order-service.yml` - Sipariş servisi konfigürasyonları
- `user-service.yml` - Kullanıcı servisi konfigürasyonları

## Kullanım

Config Server bu repository'yi kullanarak mikroservislere konfigürasyon sağlar.

### Config Server Bağlantısı

```yaml
spring:
  cloud:
    config:
      server:
        git:
          uri: https://github.com/mcay51/ecommerce-config
          default-label: main
```

### Mikroservis Konfigürasyonu

```yaml
spring:
  application:
    name: service-name
  cloud:
    config:
      uri: http://config-service:8888
      fail-fast: true
```

## Ortamlar

Her servis için farklı ortam konfigürasyonları:

- `service-name.yml` - Varsayılan konfigürasyon
- `service-name-dev.yml` - Geliştirme ortamı
- `service-name-prod.yml` - Prodüksiyon ortamı 