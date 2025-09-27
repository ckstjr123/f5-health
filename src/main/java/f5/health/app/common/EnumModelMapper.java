package f5.health.app.common;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EnumModelMapper {

    /** key: Enum Class name */
    private final Map<String, List<? extends EnumModel>> factory = new HashMap<>();

    private List<EnumModel> toEnumModels(Class<? extends MappingEnum> e) {
        return Arrays.stream(e.getEnumConstants())
                .map(EnumModel::new)
                .collect(Collectors.toList());
    }

    public void put(Class<? extends MappingEnum> e) {
        factory.put(e.getName(), toEnumModels(e));
    }

    /** for class extends EnumModel */
    public void put(Class<? extends MappingEnum> e, Class<? extends EnumModel> enumModel) {
        List<? extends EnumModel> enumModels = Arrays.stream(e.getEnumConstants())
                .map(mappingEnum -> {
                    try {
                        Constructor<? extends EnumModel> constructor = enumModel.getConstructor(e);
                        return constructor.newInstance(mappingEnum);
                    } catch (ReflectiveOperationException ex) {
                        throw new IllegalArgumentException("Cannot instantiate [" + enumModel.getName() + "] " +
                                "with constructor(" + e.getName() + ")", ex);
                    }
                })
                .toList();
        factory.put(e.getName(), enumModels);
    }

    public List<? extends EnumModel> get(Class<? extends MappingEnum> e) {
        return factory.get(e.getName());
    }

    public Map<String, List<? extends EnumModel>> toEnumModelMapBy(Set<String> enumClassNames) {
        if (enumClassNames == null || enumClassNames.isEmpty()) {
            return new LinkedHashMap<>();
        }

        return enumClassNames.stream().collect(Collectors.toUnmodifiableMap(
                Function.identity(),
                name -> {
                    if (factory.get(name) == null) {
                        throw new IllegalArgumentException("'" + name + "' is not registered in the EnumModel factory.");
                    }
                    return factory.get(name);
                })
        );
    }

}