package com.cloudera.kafka.example.generator


import com.cloudera.kafka.example.entities.Product
import com.cloudera.kafka.example.entities.User
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.opencsv.CSVReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration
import java.util.*

class ExampleRandomGenerator(private val apiLimits: Int): RandomGenerator {
  private val apiKey = System.getenv("RANDOMMER_API_KEY")
  private val client = HttpClient.newHttpClient()
  private val jsonMapper = jacksonObjectMapper()
  private val users = mutableMapOf<String, User>()
  private val products = mutableMapOf<String, Product>()
  private val cheapestPriceMatcher = Regex("""\d+(\.\d+)*""")

  init {
    if (apiLimits > 100000) {
      throw IllegalArgumentException("Amount must be less than 100000")
    }
  }

  override fun users(): Map<String, User> {
    if (users.isEmpty()) {
      users.putAll(generateUsers(apiLimits))
    }
    return users
  }

  override fun products(): Map<String, Product> {
    if (products.isEmpty()) {
      products.putAll(loadProducts())
    }
    return products
  }

  private fun generateUsers(amount: Int): Map<String, User> {
    val users = mutableMapOf<String, User>()
    val names = randomNames(amount)
    val addresses = randomAddresses(amount)
    val phones = randomPhoneNumbers(amount)
    for (i in 0 until minOf(names.size, addresses.size, phones.size)) {
      val name = names.elementAt(i)
      val address = addresses.elementAt(i)
      val phone = phones.elementAt(i)
      val username = name.replace(" ", "_").lowercase(Locale.getDefault())
      val email = "$username@fakemail.com"
      users[username] = User(UUID.randomUUID().toString(), name, email, address, phone, username)
    }
    return users
  }

  private fun loadProducts(): Map<String, Product> {
    val products = mutableMapOf<String, Product>()
    val csvReader = CSVReader(object {}.javaClass.getResourceAsStream("/amazon_data.csv")?.bufferedReader())
    csvReader.use { reader ->
      var line: Array<String>?
      reader.readNext()
      while (reader.readNext().also { line = it } != null) {
        val parsedLine = line!!.toList()
        val price = cheapestPriceMatcher.find(parsedLine[7])
        if (price != null) {
          if (!price.range.isEmpty()) {
            products[parsedLine[0]] = Product(parsedLine[0], parsedLine[1], price.value.toDouble())
          }
        }
      }
    }
    return products
  }

  private fun randomAddresses(amount: Int): Set<String> {
    synchronized(this) {
      return randomItems(amount, ::randomAddressApiLimited)
    }
  }

  private fun randomNames(amount: Int): Set<String> {
    synchronized(this) {
      return randomItems(amount, ::randomNamesApiLimited)
    }
  }

  private fun randomPhoneNumbers(amount: Int): Set<String> {
    synchronized(this) {
      return randomItems(amount, ::randomPhoneNumbersApiLimited)
    }
  }

  private fun randomAddressApiLimited(amount: Int): Set<String> {
    synchronized(this) {
      if (amount > 1000) {
        throw IllegalArgumentException("Amount must be less than 1000")
      }
      val request = HttpRequest.newBuilder()
        .uri(URI.create("https://randommer.io/api/Misc/Random-Address?number=$amount&culture=en"))
        .timeout(Duration.ofMinutes(1))
        .header("Accept", "*/*")
        .header("X-Api-Key", apiKey)
        .GET()
        .build()
      val response = client.send(request, HttpResponse.BodyHandlers.ofString())
      return jsonMapper.readValue(response.body(), Set::class.java) as Set<String>
    }
  }

  private fun randomNamesApiLimited(amount: Int): Set<String> {
    synchronized(this) {
      val request = HttpRequest.newBuilder()
        .uri(URI.create("https://randommer.io/api/Name?nameType=fullname&quantity=$amount"))
        .timeout(Duration.ofMinutes(1))
        .header("Accept", "*/*")
        .header("X-Api-Key", apiKey)
        .GET()
        .build()
      val response = client.send(request, HttpResponse.BodyHandlers.ofString())
      try {
        return jsonMapper.readValue(response.body(), Set::class.java) as Set<String>
      } catch (e: Exception) {
        println(response.body())
        throw e
      }
    }
  }

  private fun randomPhoneNumbersApiLimited(amount: Int): Set<String> {
    synchronized(this) {
      val request = HttpRequest.newBuilder()
        .uri(URI.create("https://randommer.io/api/Phone/Generate?CountryCode=US&Quantity=$amount"))
        .timeout(Duration.ofMinutes(1))
        .header("Accept", "*/*")
        .header("X-Api-Key", apiKey)
        .GET()
        .build()
      val response = client.send(request, HttpResponse.BodyHandlers.ofString())
      return jsonMapper.readValue(response.body(), Set::class.java) as Set<String>
    }
  }

  private fun randomItems(amount: Int, generator: (Int) -> Set<String>): Set<String> {
    synchronized(this) {
      val iterations = amount / 1000
      val remainder = amount % 1000
      val names = mutableSetOf<String>()
      for (i in 0 until  iterations) {
        names.addAll(generator(1000))
      }
      if (remainder > 0) {
        names.addAll(generator(remainder))
      }
      return names
    }
  }
}