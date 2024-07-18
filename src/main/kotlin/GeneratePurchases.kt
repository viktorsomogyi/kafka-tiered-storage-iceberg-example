import com.cloudera.kafka.example.generator.ExampleRandomGenerator
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.Admin
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer

fun main(args: Array<String>) {

  val purchasesTopicName = "purchases"

  val admin = Admin.create(adminProps())
  admin.listTopics().names().get().contains("purchases").let { topicExists ->
    if (!topicExists) {
      val topic = NewTopic(purchasesTopicName, 1, 1.toShort())
        .configs(
          mapOf(
            "remote.storage.enable" to "true",
            "local.retention.ms" to "10000", // 10 seconds
            "local.retention.bytes" to "1048576") // 10 MiB
        )
      admin.createTopics(listOf(topic)).all().get()
    }
  }

  admin.describeTopics(listOf(purchasesTopicName)).allTopicNames().get().forEach { (topicName, topicDescription) ->
    println("Topic: $topicName, description: $topicDescription")
  }

  val randomGenerator = ExampleRandomGenerator(1000)
  val producer = KafkaProducer<String, String>(producerProps())
  var producedPayloadSize = 0L
  randomGenerator.purchaseStream(5_000_000).forEach { purchase ->
    val record = ProducerRecord(purchasesTopicName, purchase.id, purchase.toJson())
    producer.send(record) { metadata, exception ->
      if (exception != null) {
        exception.printStackTrace()
      } else {
        producedPayloadSize += metadata.serializedValueSize()
        println("${producedPayloadSize / 1048576.0} MiB produced")
      }
    }
  }
}

fun commonProps(): MutableMap<String, String> {
  val commonProps = mutableMapOf<String, String>()
  commonProps[CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG] = "0.0.0.0:9092"
  return commonProps
}

fun producerProps(): Map<String, String> {
  val producerProps = commonProps()
  producerProps[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
  producerProps[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java.name
  producerProps[CommonClientConfigs.CLIENT_ID_CONFIG] = "purchases-producer"
  return producerProps
}

fun adminProps(): Map<String, String> {
  val props = commonProps()
  props[CommonClientConfigs.CLIENT_ID_CONFIG] = "purchases-admin"
  return props
}