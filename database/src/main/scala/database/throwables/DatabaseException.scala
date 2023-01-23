package database.throwables

abstract class DatabaseException(name: String) extends Exception(f"Database exception: $name") {}
