package infra.throwables

abstract class InfraException(cause: String) extends Exception(f"Infra exception: $cause") {}
