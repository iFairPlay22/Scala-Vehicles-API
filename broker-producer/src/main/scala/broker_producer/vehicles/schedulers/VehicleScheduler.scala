package broker_producer.vehicles.schedulers

import akka.Done
import akka.actor.ActorSystem
import broker_producer.vehicles.producers.VehicleBrokerProducer
import com.typesafe.scalalogging.Logger
import domain.vehicles.VehicleDomain
import scheduler._SchedulerSystem

import scala.concurrent.Future

class VehicleScheduler(implicit val system: ActorSystem) extends _SchedulerSystem {

  private val logger: Logger = Logger(getClass)

  private final val brokerProducer: VehicleBrokerProducer = new VehicleBrokerProducer()
  override def startScheduler(): Future[Done] =
    brokerProducer
      .startBrokerProducer()
      .andThen(_ => super.startScheduler())

  override def stopScheduler(): Future[Done] =
    super
      .stopScheduler()
      .andThen(_ => brokerProducer.stopBrokerProducer())

  override val action: Unit => Future[Done] = _ => {
    logger.info("Producing vehicle in topic")
    brokerProducer
      .produce(VehicleDomain.randomValid())
      .andThen(_ => logger.info("Successfully produced vehicle in topic!"))
      .map(_ => Done.done())
  }

}
