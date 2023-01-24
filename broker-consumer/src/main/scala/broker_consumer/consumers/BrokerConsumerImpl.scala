package broker_consumer.consumers

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.Done
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ ConsumerSettings, Subscriptions }
import akka.stream.scaladsl.{ Keep, Sink, Source }
import broker_consumer.Main.{
  brokerConsumerConfig,
  brokerConsumerExecutionContext,
  brokerConsumerLogger,
  brokerConsumerSystem
}
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

abstract class BrokerConsumerImpl[K: Decoder, V: Decoder](
    topic: String,
    callbacks: Set[Either[BrokerConsumerException, (K, V)] => Future[Done]]
) extends BrokerConsumer {

  assert(topic != null && topic.nonEmpty)
  assert(callbacks != null && callbacks.nonEmpty)

  final val config: Config = brokerConsumerConfig.getConfig("akka.kafka.consumer")

  final val settings: ConsumerSettings[String, String] =
    ConsumerSettings(config, new StringDeserializer, new StringDeserializer)

  val (consumerControl, streamComplete) = Consumer
    .plainSource(
      settings,
      Subscriptions.topics(topic)
    )
    .mapAsync(1) { record =>
      brokerConsumerLogger.info(
        f"[BrokerConsumerImpl]: fzrfer..."
      )
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
        .run()
    }
    .recover { error =>
      brokerConsumerLogger.error(f"[BrokerConsumerImpl]: Failed to load broker!", error)
      throw new UnableToConsumeInBrokerConsumerException()
    }
    .toMat(Sink.ignore)(Keep.both)
    .run()

  brokerConsumerLogger.info(f"[BrokerConsumerImpl]: Broker is running in topic $topic!")

  def terminate(): Future[Done] =
    consumerControl.shutdown()

}
