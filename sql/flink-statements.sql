CREATE TABLE user_purchases_source (
   event_id STRING,
   user_id STRING,
   amount DOUBLE
) WITH (
  'connector' = 'kafka',
  'topic' = 'purchases-by-user',
  'properties.bootstrap.servers' = '0.0.0.0:9092',
  'format' = 'json',
  'scan.startup.mode' = 'earliest-offset'
);

CREATE TABLE total_purchases_sink (
  user_id STRING,
  total_amount DOUBLE
) WITH (
  'connector' = 'iceberg',
  'catalog-name' = 'my_catalog',
  'catalog-type' = 'hadoop',
  'warehouse' = 's3a://iceberg-warehouse/',
  'fs.s3a.endpoint' = 'http://0.0.0.0:9000',
  'fs.s3a.access.key' = 'minioadmin',
  'fs.s3a.secret.key' = 'minioadmin',
  'fs.s3a.path.style.access' = 'true'
);

INSERT INTO total_purchases_sink
SELECT
    user_id,
    SUM(amount) AS total_amount
FROM user_purchases_source
GROUP BY user_id;