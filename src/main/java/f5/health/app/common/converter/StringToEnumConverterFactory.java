package f5.health.app.common.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    @Override
    public <E extends Enum> Converter<String, E> getConverter(Class<E> targetType) {
        return new StringToEnumConverter(getEnumType(targetType));
    }

    private Class<?> getEnumType(Class<?> targetType) {
        Class<?> enumType = targetType;
        while (enumType != null && !enumType.isEnum()) {
            enumType = enumType.getSuperclass();
        }
        Assert.notNull(enumType, () -> "The target type " + targetType.getName() + " does not refer to an enum");
        return enumType;
    }


    private static class StringToEnumConverter<E extends Enum> implements Converter<String, E> {

        private final Class<E> enumType;

        StringToEnumConverter(Class<E> enumType) {
            this.enumType = enumType;
        }

        @Override
        @Nullable
        public E convert(String source) {
            if (source.isEmpty()) {
                // It's an empty enum identifier: reset the enum value to null.
                return null;
            }
            return (E) Enum.valueOf(this.enumType, source.trim().toUpperCase());
        }
    }

}
