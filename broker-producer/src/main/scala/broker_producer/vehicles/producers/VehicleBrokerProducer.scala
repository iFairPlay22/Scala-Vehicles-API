package broker_producer.vehicles.producers

import akka.actor.ActorSystem
import broker._BrokerProducerSystem
import domain.vehicles.VehicleDomain
import org.apache.kafka.clients.producer.RecordMetadata
import io.circe.generic.auto._

import java.time.LocalDateTime
import scala.concurrent.Future

class VehicleBrokerProducer(implicit val system: ActorSystem)
    extends _BrokerProducerSystem[LocalDateTime, VehicleDomain] {

  def produce(vehicle: VehicleDomain): Future[RecordMetadata] =
    super.produce(LocalDateTime.now, vehicle)

}
