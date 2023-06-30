package broker_producer

import broker_producer.vehicles.schedulers.VehicleScheduler
import commons.actor._ActorSystem

object Main extends _ActorSystem {

  final val scheduler: VehicleScheduler = new VehicleScheduler()

  def main(args: Array[String]): Unit =
    scheduler.startScheduler()

}
