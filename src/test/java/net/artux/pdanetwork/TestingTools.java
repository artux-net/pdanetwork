package net.artux.pdanetwork;

import java.lang.reflect.Field;
import java.util.Arrays;

final public class TestingTools {
    static boolean compare(boolean onlyByPrimitives, Object... objects) {
        if (objects.length < 2) {
            return true;
        }
        if (Arrays.stream(objects).allMatch(o -> o == objects[0])) {
            return true;
        }
        if (Arrays.stream(objects).map(Object::getClass).map(Class::getName).distinct().count() > 1) {
            return false;
        }
        Field[] classFields = objects[0].getClass().getDeclaredFields();
        Arrays.sort(classFields);
        for (Field classField : classFields) {
            if (!Arrays.stream(objects).allMatch(classField::canAccess)) {
                classField.setAccessible(true);
            }
            if (!onlyByPrimitives || classField.getType().isPrimitive()) {
                if (!Arrays.stream(objects).allMatch(o -> {
                    try {
                        return classField.get(objects[0]).equals(classField.get(o));
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })) {
                    return false;
                }
            }
        }
        return true;
    }
}
