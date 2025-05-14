package f5.health.app.constant;

import org.springframework.web.server.ServerErrorException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

//@Component
public class EnumModelMapper {

    /** key: enum class name */
    private final Map<String, List<? extends EnumModel>> factory = new HashMap<>();

    private List<? extends EnumModel> toEnumModels(Class<? extends MappingEnum> e) {
        return Arrays.stream(e.getEnumConstants()).map(EnumModel::new).collect(Collectors.toList());
    }

    public void put(Class<? extends MappingEnum> e) {
        factory.put(e.getName(), this.toEnumModels(e));
    }

    /** for class extends EnumModel */
    public void put(Class<? extends MappingEnum> e, Class<? extends EnumModel> enumModel) {
        List<? extends EnumModel> enumModels = Arrays.stream(e.getEnumConstants())
                .map(mappingEnum -> {
                    try {
                        Constructor<? extends EnumModel> constructor = enumModel.getConstructor(e);
                        return constructor.newInstance(mappingEnum);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                        throw new ServerErrorException("Cannot instantiate [" + enumModel.getName() + "] " +
                                "with constructor(" + e.getName() + ")", ex);
                    }
                })
                .toList();
        factory.put(e.getName(), enumModels);
    }

    public List<? extends EnumModel> get(Class<? extends MappingEnum> e) {
        return factory.get(e.getName());
    }

    public Map<String, List<? extends EnumModel>> get(Set<Class<? extends MappingEnum>> enumClassSet) {
        return enumClassSet.stream().map(Class::getName).collect(Collectors.toMap(Function.identity(), enumClassName -> factory.get(enumClassName)));
    }

    public Map<String, List<? extends EnumModel>> getAll() {
        return factory;
    }
}