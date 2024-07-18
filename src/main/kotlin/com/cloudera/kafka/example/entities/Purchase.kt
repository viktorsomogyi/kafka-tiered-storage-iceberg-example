package com.cloudera.kafka.example.entities

data class Purchase(val id: String, val user: User, val product: Product, val amount: Double) {

  fun total(): Double {
    return product.price * amount
  }

  fun toJson(): String {
    return """
      {
        "id": "$id",
        "user": ${user.toJson()},
        "product": ${product.toJson()},
        "amount": $amount
      }
    """.trimIndent()
  }
}