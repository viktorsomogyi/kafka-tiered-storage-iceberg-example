package com.cloudera.kafka.example.entities

data class User(
  val id: String,
  val name: String,
  val email: String,
  val address: String,
  val phone: String,
  val username: String
) {
  fun toJson(): String {
    return """
      {
        "id": "$id",
        "name": "$name",
        "email": "$email",
        "address": "$address",
        "phone": "$phone",
        "username": "$username"
      }
    """.trimIndent()
  }
}