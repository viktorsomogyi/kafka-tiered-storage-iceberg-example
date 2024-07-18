package com.cloudera.kafka.example.generator

import com.cloudera.kafka.example.entities.Product
import kotlin.test.Test
import kotlin.test.assertEquals

class GeneratorTest {

  @Test
  fun testGenerateUser() {
    val products = ExampleRandomGenerator(1).products()
    val expectedProduct = Product("4c69b61db1fc16e7013b43fc926e502d",
      "DB Longboards CoreFlex Crossbow 41\" Bamboo Fiberglass Longboard Complete", 237.68)
    assertEquals(expectedProduct, products["4c69b61db1fc16e7013b43fc926e502d"])
  }

  @Test
  fun testGeneratePurchase() {
    val purchase = MockRandomGenerator().generatePurchase()
    assertEquals("John Doe", purchase.user.name)
    assertEquals("Apple iPhone 12 Pro Max", purchase.product.name)
  }
}