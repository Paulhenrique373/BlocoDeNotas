package com.example.blocodenotas

data class Note(
    var id: Int = 0,
    var titulo: String,
    var descricao: String,
    var fixada: Int = 0, // 🔥 0 = normal | 1 = fixada
    var cor: String = "#FFE7C2" // 🎨 COR CORRETA
)