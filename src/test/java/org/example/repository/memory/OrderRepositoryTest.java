package org.example.repository.memory;

import org.example.domain.car.*;
import org.example.domain.common.Color;
import org.example.domain.common.Money;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.domain.order.Order;
import org.example.domain.order.OrderId;
import org.example.domain.order.OrderType;
import org.example.domain.user.UserId;
import org.example.infrastructure.repository.memory.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrderRepositoryTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new OrderRepository();
        Field field = OrderRepository.class.getDeclaredField("orders");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    private Map<ComponentType, Component> createStandardComponents() {
        Map<ComponentType, Component> components = new HashMap<>();

        Component engine = new Component(
                new ComponentId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")),
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of() // совместимость задаётся отдельно, для тестов пусто
        );
        Component transmission = new Component(
                new ComponentId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")),
                ComponentType.TRANSMISSION,
                "Automatic 8",
                Money.of(3000),
                Set.of()
        );
        components.put(ComponentType.ENGINE, engine);
        components.put(ComponentType.TRANSMISSION, transmission);
        return components;
    }

    private Model createCarModel(String uuidStr, String name, String brand, Money basePrice) {
        ModelId id = new ModelId(UUID.fromString(uuidStr));
        return new Model(
                id,
                name,
                brand,
                basePrice,
                EngineType.GASOLINE,
                Year.of(2023),
                BodyType.SEDAN,
                DriveType.RWD,
                TransmissionType.AUTOMATIC_8,
                5,
                createStandardComponents()
        );
    }

    private Car createCar(String uuidStr, Model model, Color color) {
        CarId id = new CarId(UUID.fromString(uuidStr));
        return new Car(id, model, color);
    }

    private Order createOrder(String orderUuid, OrderType type, String managerUuid, Car car, String clientUuid) {
        OrderId orderId = new OrderId(UUID.fromString(orderUuid));
        UserId managerId = new UserId(UUID.fromString(managerUuid));
        UserId clientId = new UserId(UUID.fromString(clientUuid));
        return new Order(orderId, type, managerId, car, clientId);
    }

    @Test
    void testSaveAndFindById() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model S", "Tesla", Money.of(50000));
        Car car = createCar("22222222-2222-2222-2222-222222222222", model, new Color(0, 0, 0));
        Order order = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.BASIC,
                "33333333-3333-3333-3333-333333333333",
                car,
                "44444444-4444-4444-4444-444444444444"
        );

        repository.save(order);

        OrderId id = new OrderId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Order found = repository.findById(id);
        assertNotNull(found);
        assertEquals(id.value(), found.getId().value());
        assertEquals(OrderType.BASIC, found.getType());
        assertEquals(new UserId(UUID.fromString("33333333-3333-3333-3333-333333333333")), found.getManagerId());
        assertEquals(new UserId(UUID.fromString("44444444-4444-4444-4444-444444444444")), found.getClientId());
        assertEquals(car, found.getCar());
        assertEquals(car.totalPrice(), found.getTotalPrice());
        assertEquals("FORMED", found.getCurrentState().name());
        assertNotNull(found.getCreatedAt());
    }

    @Test
    void testFindAll() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model S", "Tesla", Money.of(50000));
        Car car1 = createCar("22222222-2222-2222-2222-222222222222", model, new Color(0, 0, 0));
        Car car2 = createCar("33333333-3333-3333-3333-333333333333", model, new Color(255, 255, 255));

        Order order1 = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.BASIC,
                "44444444-4444-4444-4444-444444444444",
                car1,
                "55555555-5555-5555-5555-555555555555"
        );
        Order order2 = createOrder(
                "00000000-0000-0000-0000-000000000002",
                OrderType.SPECIFIC_CONFIGURATION,
                "66666666-6666-6666-6666-666666666666",
                car2,
                "77777777-7777-7777-7777-777777777777"
        );

        repository.save(order1);
        repository.save(order2);

        List<Order> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(order1));
        assertTrue(all.contains(order2));
    }

    @Test
    void testUpdateExisting() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model S", "Tesla", Money.of(50000));
        Car car1 = createCar("22222222-2222-2222-2222-222222222222", model, new Color(0, 0, 0));
        Car car2 = createCar("33333333-3333-3333-3333-333333333333", model, new Color(230, 0, 0));

        Order order = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.BASIC,
                "44444444-4444-4444-4444-444444444444",
                car1,
                "55555555-5555-5555-5555-555555555555"
        );
        repository.save(order);

        Order updated = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.SPECIFIC_CONFIGURATION,
                "66666666-6666-6666-6666-666666666666",
                car2,
                "77777777-7777-7777-7777-777777777777"
        );
        repository.update(updated);

        OrderId id = new OrderId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Order found = repository.findById(id);
        assertNotNull(found);
        assertEquals(OrderType.SPECIFIC_CONFIGURATION, found.getType());
        assertEquals(new UserId(UUID.fromString("66666666-6666-6666-6666-666666666666")), found.getManagerId());
        assertEquals(new UserId(UUID.fromString("77777777-7777-7777-7777-777777777777")), found.getClientId());
        assertEquals(car2, found.getCar());
        assertEquals("FORMED", found.getCurrentState().name());
    }

    @Test
    void testUpdateNonExistingThrowsException() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model S", "Tesla", Money.of(50000));
        Car car = createCar("22222222-2222-2222-2222-222222222222", model, new Color(0, 0, 0));
        Order order = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.BASIC,
                "33333333-3333-3333-3333-333333333333",
                car,
                "44444444-4444-4444-4444-444444444444"
        );
        assertThrows(EntityNotFoundException.class, () -> repository.update(order));
    }

    @Test
    void testDeleteExisting() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model S", "Tesla", Money.of(50000));
        Car car = createCar("22222222-2222-2222-2222-222222222222", model, new Color(0, 0, 0));
        Order order = createOrder(
                "00000000-0000-0000-0000-000000000001",
                OrderType.BASIC,
                "33333333-3333-3333-3333-333333333333",
                car,
                "44444444-4444-4444-4444-444444444444"
        );
        repository.save(order);
        OrderId id = new OrderId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.delete(id);

        assertNull(repository.findById(id));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void testDeleteNonExistingThrowsException() {
        OrderId id = new OrderId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.delete(id));
    }
}