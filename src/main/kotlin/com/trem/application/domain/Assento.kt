package com.trem.application.domain

// com a palavra reservada data class o Kotlin já entende que é uma classe para guardar dados, 
// então gera os getters e setters, constructores, equals, hashCode e toString automaticamente
data class Assento(
    val numero: String, // isso é um value, é imutável, não pode ser alterado depois de criado
    var ocupado: Boolean = false // isso é um var, pode ser mudado
)