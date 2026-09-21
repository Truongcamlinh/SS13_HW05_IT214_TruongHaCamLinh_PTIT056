# SS13 HW05 - WebFlux, Circuit Breaker và Micrometer

**Sinh viên:** Trương Hà Cẩm Linh

**Lớp:** IT214

**Mã sinh viên:** PTIT056

## Cấu trúc

- `order-service` chạy port `8080`, gọi Inventory bằng WebClient.
- `inventory-service` chạy port `8081`, cung cấp `/api/inventory/check`.

Luồng hoàn toàn reactive:

```java
webClient.get()
    .uri(...)
    .retrieve()
    .bodyToMono(InventoryDto.class)
    .transformDeferred(CircuitBreakerOperator.of(circuitBreaker))
    .onErrorResume(error -> Mono.just(InventoryDto.fallback(...)));
```

Không sử dụng annotation Circuit Breaker và không gọi đồng bộ để chờ kết quả.

## Metrics

Order Service có các dependency:

```gradle
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'io.github.resilience4j:resilience4j-reactor:2.2.0'
implementation 'io.github.resilience4j:resilience4j-micrometer:2.2.0'
```

Các endpoint được expose:

```text
GET http://localhost:8080/actuator/metrics
GET http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state
GET http://localhost:8080/actuator/circuitbreakers
```

Metric `resilience4j.circuitbreaker.state` có tag `name=inventoryClient`. Khi circuit đóng, series có `state=closed` mang giá trị `1.0`, các trạng thái khác mang giá trị `0.0`.

## Chạy thử

```bash
./gradlew :inventory-service:bootRun
./gradlew :order-service:bootRun
```

Gọi API:

```bash
curl 'http://localhost:8080/api/orders/inventory?productId=2&quantity=5'
```

Dừng Inventory Service rồi gọi lại ít nhất 5 lần để quan sát Circuit Breaker chuyển sang `OPEN` và fallback được trả về.

Kiểm tra metric:

```bash
curl http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state
```

## Build

```bash
./gradlew clean build
```

Test tự động xác nhận Circuit Breaker `inventoryClient` ở trạng thái `CLOSED` và Micrometer đã đăng ký gauge với tag tương ứng.
