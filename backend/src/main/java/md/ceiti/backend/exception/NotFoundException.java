package md.ceiti.backend.exception;

import java.util.Map;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, String field, Object value) {
        this(entity, Map.of(field, value));
    }

    public NotFoundException(String entity, Map<String, Object> searchedValuesByFields) {
        super(String.format(
                "Could not find '%s' with %s",
                entity,
                String.join(" and ",
                        searchedValuesByFields.keySet()
                                .stream()
                                .map(key -> String.format("[%s = '%s']", key, searchedValuesByFields.get(key)))
                                .toList()
                )
        ));
    }
}
