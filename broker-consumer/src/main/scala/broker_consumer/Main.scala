package broker_consumer
import akka.actor.ActorSystem
import broker_consumer.vehicles.consumers.VehicleBrokerConsumer
import commons.system.actor._ActorSystem

object Main extends _ActorSystem {

  final val consumer: VehicleBrokerConsumer = new VehicleBrokerConsumer()

  def main(args: Array[String]): Unit =
    consumer.startBrokerConsumer()

}
