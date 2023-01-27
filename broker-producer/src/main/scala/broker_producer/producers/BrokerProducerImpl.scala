package broker_producer.producers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.SendProducer
import broker_producer.Main.{
  brokerProducerConfig,
  brokerProducerExecutionContext,
  brokerProducerLogger,
  brokerProducerSystem
}
import broker_producer.throwables.{
  BrokerProducerException,
  UnableToProduceInBrokerProducerException
}
import com.typesafe.config.Config
import io.circe.Encoder
import org.apache.kafka.clients.producer.{ ProducerRecord, RecordMetadata }
import org.apache.kafka.common.serialization.StringSerializer

import scala.concurrent.Future

abstract class BrokerProducerImpl[K: Encoder, V: Encoder](
    topic: String
) extends BrokerProducer {

  assert(topic != null && topic.nonEmpty)

  brokerProducerLogger.info(f"[BrokerConsumerImpl]: Running broker in topic $topic!")

  final val config: Config = brokerProducerConfig.getConfig("app.kafka.producer")

  final val settings: ProducerSettings[String, String] =
    ProducerSettings(config, new StringSerializer, new StringSerializer)

  final val producer: SendProducer[String, String] = SendProducer(settings)

  def produce(
      key: K,
      value: V
  ): Future[Either[BrokerProducerException, RecordMetadata]] = {

    brokerProducerLogger.info(
      f"[BrokerProducerImpl]: Producing in topic $topic in producer..."
    )

    producer
      .send(
        new ProducerRecord(
          topic,
          key.asJson.noSpaces,
          value.asJson.noSpaces
        )
      )
      .map(record => Right(record))
      .recover { error =>
        brokerProducerLogger.error(f"[BrokerProducerImpl]: Failed to produce in broker!", error)
        Left(new UnableToProduceInBrokerProducerException())
      }
  }

  def terminate(): Unit =
    producer.close()
}
