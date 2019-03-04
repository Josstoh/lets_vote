package fr.josstoh.letsvote.data.model

data class User(var uid: String = "",
                var name: String = "",
                var mail: String = "",
                var tokens: List<String> = arrayListOf())