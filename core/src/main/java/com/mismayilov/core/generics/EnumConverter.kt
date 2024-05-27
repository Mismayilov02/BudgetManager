package com.mismayilov.core.generics


inline fun <reified T : Enum<T>> stringToEnum(enumString: String): T? {
    return try {
        enumValueOf<T>(enumString)
    } catch (e: IllegalArgumentException) {
        null
    }
}

inline fun <reified T : Enum<T>> enumToString(enumValue: T): String {
    return enumValue.name
}
