package broker_producer.vehicles.schedulers

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import broker_producer.vehicles.producers.VehicleBrokerProducer
import com.typesafe.scalalogging.Logger
import commons.system.actor._ActorSystem
import commons.system.scheduler._SchedulerSystem
import domain.vehicles.VehicleDomain

import java.time.Duration
import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

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
