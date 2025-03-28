package net.artux.pdanetwork.configuration

import net.artux.pdanetwork.utils.CustomLocaleConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver
import java.util.Locale

@Configuration
open class InternalizationConfig(
    private val customLocaleConverter: CustomLocaleConverter
) : WebMvcConfigurer {

    @Bean
    open fun localeResolver(): LocaleResolver {
        return AcceptHeaderLocaleResolver().apply {
            setDefaultLocale(Locale.of("ru"))
        }
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(customLocaleConverter)
    }
}
