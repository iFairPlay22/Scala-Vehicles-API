package broker_consumer.consumers

import akka.Done
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ ConsumerSettings, Subscriptions }
import akka.stream.scaladsl.Source
import broker_consumer.Main.{ brokerConsumerConfig, brokerConsumerLogger }
import broker_consumer.throwables.{
  BrokerConsumerException,
  UnableToConsumeInBrokerConsumerException,
  UnableToDecodeKeyInBrokerConsumerException,
  UnableToDecodeValueInBrokerConsumerException
}
import com.typesafe.config.Config
import io.circe.Decoder
import io.circe.parser.decode
import org.apache.kafka.common.serialization.StringDeserializer

import scala.concurrent.Future

abstract class BrokerConsumerImpl[K: Decoder, V: Decoder] extends BrokerConsumer {

  val topic: String
  val callbacks: Set[Either[BrokerConsumerException, (K, V)] => Future[Done]]

  final val config: Config = brokerConsumerConfig.getConfig("akka.kafka.consumer")

  final val settings: ConsumerSettings[String, String] =
    ConsumerSettings(config, new StringDeserializer, new StringDeserializer)

  final val consumer: Source[Done, Consumer.Control] = Consumer
    .plainSource(
      settings,
      Subscriptions.topics(topic)
    )
    .flatMapConcat { record =>
      Source(callbacks)
        .mapAsync(3) { callback =>
          brokerConsumerLogger.info(
            f"[BrokerConsumerImpl]: Consuming data in topic $topic in consumer..."
          )

          val eitherErrorOrKey   = decode[K](record.key())
          val eitherErrorOrValue = decode[V](record.value())

          eitherErrorOrKey match {
            case Right(key) =>
              eitherErrorOrValue match {
                case Right(value) =>
                  callback(Right((key, value)))
                case Left(_) =>
                  callback(Left(new UnableToDecodeValueInBrokerConsumerException()))
              }
            case Left(_) => callback(Left(new UnableToDecodeKeyInBrokerConsumerException()))
          }
        }
    }
    .recover { _ =>
      throw new UnableToConsumeInBrokerConsumerException()
    }

  def terminate(): Unit = {}

}
