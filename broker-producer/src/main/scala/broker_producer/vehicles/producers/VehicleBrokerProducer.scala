package broker_producer.vehicles.producers

import io.circe.generic.auto._
import akka.actor.ActorSystem
import akka.http.scaladsl.model.DateTime
import commons.system.broker._BrokerProducerSystem
import domain.data.vehicles.VehicleDomain
import org.apache.kafka.clients.producer.RecordMetadata

import scala.concurrent.Future
import scala.util.Try

class VehicleBrokerProducer(implicit val system: ActorSystem)
    extends _BrokerProducerSystem[DateTime, VehicleDomain] {

  override val topic: String = config.getString("broker_producer.topic")

  def produce(vehicle: VehicleDomain): Future[RecordMetadata] =
    super.produce(DateTime.now, vehicle)

}
