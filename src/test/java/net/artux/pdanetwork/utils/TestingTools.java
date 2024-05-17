package net.artux.pdanetwork.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

final public class TestingTools {
    static final ArrayList<Type> simpleTypes = new ArrayList<>(Arrays.asList(
            Boolean.class,
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Character.class,
            String.class));

    static boolean compareSomeType(boolean onlySimpleTypes, Object... objects) {
        if (objects.length < 2) {
            return true;
        }

        // If all object are literally the same
        if (Arrays.stream(objects).allMatch(o -> o == objects[0])) {
            return true;
        }

        // If all object are not the same class
        if (Arrays.stream(objects).map(Object::getClass).map(Class::getName).distinct().count() > 1) {
            return false;
        }
        Field[] classFields = objects[0].getClass().getDeclaredFields();
        for (Field classField : classFields) {
            if (!onlySimpleTypes || simpleTypes.contains(classField.getType())) {
                if (!Arrays.stream(objects).allMatch(classField::canAccess)) {
                    classField.setAccessible(true);
                }

                try {
                    // Just to do not invoke each Field::get() twice
                    Object buffer = classField.get(objects[0]);

                    for (int i = 1; i < objects.length; i++) {
                        Object value = classField.get(objects[i]);
                        if (!Objects.equals(value, buffer)) {
                            return false;
                        }
                        buffer = value;
                    }
                } catch (IllegalAccessException e) {
                    // It can not be reached
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }

    static boolean compareDiffTypes(boolean onlySimpleTypes, boolean strict, Object... objects) {
        class FieldNamePrim {
            final String name;
            final boolean isSimple;

            public FieldNamePrim(String name, boolean isSimple) {
                this.name = name;
                this.isSimple = isSimple;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                FieldNamePrim that = (FieldNamePrim) o;
                return isSimple == that.isSimple && Objects.equals(name, that.name);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, isSimple);
            }
        }

        if (objects.length < 2) {
            return true;
        }

        // Array of {String fieldName; boolean isSimple}
        // It contains all unique fields of all objects
        FieldNamePrim[] fieldNames = Arrays.stream(objects)
                .flatMap(o -> Arrays.stream(o.getClass().getDeclaredFields()))
                .map(f -> new FieldNamePrim(f.getName(), simpleTypes.contains(f.getType())))
                .distinct()
                .toArray(FieldNamePrim[]::new);

        for (FieldNamePrim fieldName : fieldNames) {
            if (!onlySimpleTypes || fieldName.isSimple) {

                // This field of each object (same named)
                // Here is no not-null filter to save index relation between fields and objects
                Field[] fields = Arrays.stream(objects)
                        .map(o -> {
                            try {
                                return o.getClass().getDeclaredField(fieldName.name);
                            } catch (NoSuchFieldException e) {
                                return null;
                            }
                        })
                        .toArray(Field[]::new);

                // If any object lost this field
                if (strict && Arrays.stream(fields).anyMatch(Objects::isNull)) {
                    return false;
                }

                // Make all fields accessible
                for (int i = 0; i < objects.length; i++) {
                    if (fields[i] != null && !fields[i].canAccess(objects[i])) {
                        fields[i].setAccessible(true);
                    }
                }

                // Check equality
                try {
                    // Just to do not invoke each Field::get() twice
                    Object buffer = null;

                    // Search for the first not-null field
                    int i = 0;
                    for (; buffer == null && i < fields.length; i++) {
                        if (fields[i] != null) {
                            buffer = fields[i].get(objects[i]);
                        }
                    }

                    // Iterating through fields, not objects, in case of not-strict check
                    i++;
                    for (; i < fields.length; i++) {
                        if (fields[i] != null) {
                            Object value = fields[i].get(objects[i]);
                            if (!Objects.equals(value, buffer)) {
                                return false;
                            }
                            buffer = value;
                        }
                    }
                } catch (IllegalAccessException e) {
                    // It can not be reached
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }
}
