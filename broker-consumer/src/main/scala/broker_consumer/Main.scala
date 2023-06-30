package broker_consumer
import broker_consumer.vehicles.consumers.VehicleBrokerConsumer
import commons.actor._ActorSystem

object Main extends _ActorSystem {

  final val consumer: VehicleBrokerConsumer = new VehicleBrokerConsumer()

  def main(args: Array[String]): Unit =
    consumer.startBrokerConsumer()

}
