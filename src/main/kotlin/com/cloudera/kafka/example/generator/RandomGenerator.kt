package com.cloudera.kafka.example.generator

import com.cloudera.kafka.example.entities.Product
import com.cloudera.kafka.example.entities.Purchase
import com.cloudera.kafka.example.entities.User
import java.util.*
import java.util.stream.Stream

interface RandomGenerator {

  fun generatePurchase(): Purchase {
    val user = users().values.random()
    val product = products().values.random()
    val amount = (1..10).random()
    val id = UUID.randomUUID().toString()
    return Purchase(id, user, product, amount.toDouble())
  }

  fun purchaseStream(amount: Int): Stream<Purchase> {
    return Stream.generate { generatePurchase() }.limit(amount.toLong())
  }

  fun purchaseStream(): Stream<Purchase> {
    return Stream.generate { generatePurchase() }
  }

  fun users(): Map<String, User>

  fun products(): Map<String, Product>
}