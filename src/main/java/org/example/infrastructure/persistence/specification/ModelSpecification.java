package org.example.infrastructure.persistence.specification;

import org.example.infrastructure.persistence.entity.ModelJpaEntity;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.util.Set;
import java.util.UUID;

public class ModelSpecification {
    public static Specification<ModelJpaEntity> byBrand(String brand) {
        return (root, query, cb) ->
                brand == null ? null : cb.equal(root.get("brand"), brand);
    }

    public static Specification<ModelJpaEntity> hasAnyComponent(Set<UUID> componentIds) {
        if (componentIds == null || componentIds.isEmpty()) {
            return null;
        }
        return (root, query, cb) -> {
            MapJoin<ModelJpaEntity, Object, UUID> join = root.joinMap("standardComponentIds");
            return join.get("value").in(componentIds);
        };
    }
}