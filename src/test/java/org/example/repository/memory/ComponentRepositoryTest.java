package org.example.repository.memory;

import org.example.domain.car.*;
import org.example.domain.common.Money;
import org.example.domain.common.exception.EntityNotFoundException;
import org.example.infrastructure.repository.memory.ComponentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRepositoryTest {

    private ComponentRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        repository = new ComponentRepository();
        Field field = ComponentRepository.class.getDeclaredField("components");
        field.setAccessible(true);
        Map<?, ?> map = (Map<?, ?>) field.get(null);
        map.clear();
    }

    private Map<ComponentType, Component> createStandardComponents() {
        Map<ComponentType, Component> components = new HashMap<>();
        Component engine = new Component(
                new ComponentId(UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")),
                ComponentType.ENGINE,
                "Standard Engine",
                Money.of(5000),
                new HashSet<>()
        );
        Component transmission = new Component(
                new ComponentId(UUID.fromString("bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb")),
                ComponentType.TRANSMISSION,
                "Standard Transmission",
                Money.of(3000),
                new HashSet<>()
        );
        components.put(ComponentType.ENGINE, engine);
        components.put(ComponentType.TRANSMISSION, transmission);
        return components;
    }

    private Model createCarModel(String uuidStr, String name, String brand) {
        ModelId id = new ModelId(UUID.fromString(uuidStr));
        return new Model(
                id,
                name,
                brand,
                Money.of(30000),
                EngineType.GASOLINE,
                Year.of(2023),
                BodyType.SEDAN,
                DriveType.RWD,
                TransmissionType.AUTOMATIC_8,
                5,
                createStandardComponents()
        );
    }

    private Component createComponent(String uuidStr, ComponentType type, String name, Money price, Set<ModelId> compatibleModels) {
        ComponentId id = new ComponentId(UUID.fromString(uuidStr));
        return new Component(id, type, name, price, compatibleModels);
    }

    @Test
    void testSaveAndFindById() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        repository.save(component);

        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Component found = repository.findById(id);
        assertNotNull(found);
        assertEquals(ComponentType.ENGINE, found.type());
        assertEquals("V8 Engine", found.name());
        assertEquals(Money.of(10000), found.price());
        assertTrue(found.compatibleModels().isEmpty());
    }

    @Test
    void testFindAll() {
        Component c1 = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        Component c2 = createComponent(
                "00000000-0000-0000-0000-000000000002",
                ComponentType.WHEELS,
                "Alloy Wheels",
                Money.of(2000),
                Set.of()
        );
        repository.save(c1);
        repository.save(c2);

        List<Component> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.contains(c1));
        assertTrue(all.contains(c2));
    }

    @Test
    void testFindByName() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        repository.save(component);

        Component found = repository.findByName("V8 Engine");
        assertNotNull(found);
        assertEquals("V8 Engine", found.name());

        assertNull(repository.findByName("Unknown"));
    }

    @Test
    void testFindCompatible() {
        Model modelX = createCarModel("11111111-1111-1111-1111-111111111111", "Model X", "Tesla");
        Model modelY = createCarModel("22222222-2222-2222-2222-222222222222", "Model Y", "Tesla");

        Component c1 = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "Performance Engine",
                Money.of(15000),
                Set.of(modelX.id(), modelY.id())
        );
        Component c2 = createComponent(
                "00000000-0000-0000-0000-000000000002",
                ComponentType.WHEELS,
                "Sport Wheels",
                Money.of(3000),
                Set.of(modelX.id())
        );

        repository.save(c1);
        repository.save(c2);

        List<Component> compatibleWithX = repository.findCompatible(modelX);
        assertEquals(2, compatibleWithX.size());

        List<Component> compatibleWithY = repository.findCompatible(modelY);
        assertEquals(1, compatibleWithY.size());
        assertEquals("Performance Engine", compatibleWithY.getFirst().name());
    }

    @Test
    void testUpdateExisting() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        repository.save(component);

        Component updated = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Supercharged Engine",
                Money.of(12000),
                Set.of()
        );
        repository.update(updated);

        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        Component found = repository.findById(id);
        assertEquals("V8 Supercharged Engine", found.name());
        assertEquals(Money.of(12000), found.price());
    }

    @Test
    void testUpdateNonExistingThrowsException() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        assertThrows(EntityNotFoundException.class, () -> repository.update(component));
    }

    @Test
    void testDeleteExisting() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                Set.of()
        );
        repository.save(component);
        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.delete(id);

        assertNull(repository.findById(id));
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void testDeleteNonExistingThrowsException() {
        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.delete(id));
    }

    @Test
    void testAddCompatibility() {
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                new HashSet<>()
        );
        repository.save(component);

        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model X", "Tesla");
        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.addCompatibility(id, model);

        Component updated = repository.findById(id);
        assertTrue(updated.compatibleModels().contains(model.id()));
    }

    @Test
    void testAddCompatibilityToNonExistingThrowsException() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model X", "Tesla");
        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.addCompatibility(id, model));
    }

    @Test
    void testRemoveCompatibility() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model X", "Tesla");
        Set<ModelId> compatible = new HashSet<>();
        compatible.add(model.id());
        Component component = createComponent(
                "00000000-0000-0000-0000-000000000001",
                ComponentType.ENGINE,
                "V8 Engine",
                Money.of(10000),
                compatible
        );
        repository.save(component);

        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        repository.removeCompatibility(id, model);

        Component updated = repository.findById(id);
        assertFalse(updated.compatibleModels().contains(model.id()));
    }

    @Test
    void testRemoveCompatibilityFromNonExistingThrowsException() {
        Model model = createCarModel("11111111-1111-1111-1111-111111111111", "Model X", "Tesla");
        ComponentId id = new ComponentId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        assertThrows(EntityNotFoundException.class, () -> repository.removeCompatibility(id, model));
    }
}