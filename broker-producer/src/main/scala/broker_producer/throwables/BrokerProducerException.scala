package broker_producer.throwables

abstract class BrokerProducerException(name: String) extends Exception(f"Broker producer exception: $name") {}
