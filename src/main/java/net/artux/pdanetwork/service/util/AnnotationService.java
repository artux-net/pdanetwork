package net.artux.pdanetwork.service.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Slf4j
@Service
public class AnnotationService {

    public <T> Annotation getAnnotationFromClassByAnnotationClass(Class<T> clazz, String fieldName, Class<? extends Annotation> annClass) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            log.error("Ошибка извлечения поля " + fieldName + " из класса - " + clazz.getName() + " - " + e.getMessage());
        }
        if (field != null) {
            return field.getAnnotation(annClass);
        }else return null;
    }
}
