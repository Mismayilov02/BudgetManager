package com.mismayilov.core.generics


infix fun String.toEnum(enumClass: Class<out Enum<*>>): Enum<*> {
    return enumClass.enumConstants?.first { it.name == this }
        ?: throw IllegalArgumentException("No enum constant $this in ${enumClass.simpleName}")
}
