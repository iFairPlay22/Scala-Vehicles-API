package broker_producer.producers.vehicles

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._
import akka.Done
import broker_producer.Main.{ brokerProducerConfig, brokerProducerLogger }
import broker_producer.producers.BrokerProducerImpl
import broker_producer.throwables.BrokerProducerException
import domain.data.vehicles.VehicleEntity
import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.Future

class VehicleBrokerProducer extends BrokerProducerImpl[Long, List[VehicleEntity]] {

  val topic: String = brokerProducerConfig.getString("broker_producer.topic")

  def produce(
      vehicles: List[VehicleEntity]
  ): Future[Either[BrokerProducerException, RecordMetadata]] = {

    val key: Long                  = System.currentTimeMillis()
    val value: List[VehicleEntity] = vehicles

    super.produce(key, value)
  }

}
