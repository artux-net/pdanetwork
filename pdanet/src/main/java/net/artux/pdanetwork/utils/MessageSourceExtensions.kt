package net.artux.pdanetwork.utils

import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder

/**
 * Resolves a message key for the current request's locale
 * (via [LocaleContextHolder]), so callers don't repeat the
 * MessageSource + LocaleContextHolder boilerplate.
 */
fun MessageSource.localized(code: String, vararg args: Any?): String =
    getMessage(code, args, LocaleContextHolder.getLocale())
