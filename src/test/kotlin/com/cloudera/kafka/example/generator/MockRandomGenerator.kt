package com.cloudera.kafka.example.generator

import com.cloudera.kafka.example.entities.Product
import com.cloudera.kafka.example.entities.User
import java.util.*

class MockRandomGenerator: RandomGenerator {

  override fun users(): Map<String, User> {
    val uuid = UUID.randomUUID()
    return mutableMapOf(
      uuid.toString() to User(
        uuid.toString(),
        "John Doe",
        "john_doe@fakemail.com",
        "207 Jeffry Ramp, Suite 808, 99725-0772, East Wilburnhaven, Missouri, United States",
        "+1-202-555-0136",
        "john_doe"))
  }

  override fun products(): Map<String, Product> {
    val uuid = UUID.randomUUID()
    return mutableMapOf(
      uuid.toString() to Product(
        uuid.toString(),
        "Apple iPhone 12 Pro Max",
        1099.99))
  }

}