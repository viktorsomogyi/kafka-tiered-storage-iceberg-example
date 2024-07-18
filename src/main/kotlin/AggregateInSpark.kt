import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession

fun main(args: Array<String>) {
  val spark = SparkSession
    .builder()
    .master("local")
    .appName("JavaStructuredNetworkWordCount")
    .getOrCreate()

  val df: Dataset<Row> = spark
    .readStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "0.0.0.0:9092")
    .option("subscribe", "purchases")
    .load()

  df.selectExpr("CAST(key AS STRING)", "CAST(value AS STRING)").count()
}