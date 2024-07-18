package com.cloudera.kafka.example.entities

data class Product(val id: String, val name: String, val price: Double) {
  fun toJson(): String {
    return """
      {
        "id": "$id",
        "name": "$name",
        "price": $price
      }
    """.trimIndent()
  }
}