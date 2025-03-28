package net.artux.pdanetwork.utils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CustomLocaleConverter implements Converter<String, Locale> {
    @Override
    public Locale convert(String source) {
        String[] parts = source.split("_");
        if (parts.length == 2) {
            return Locale.of(parts[0], parts[1]);
        }
        return Locale.of(source);
    }
}