package broker_consumer.throwables

abstract class BrokerConsumerException(name: String) extends Exception(f"Broker consumer exception: $name") {}
