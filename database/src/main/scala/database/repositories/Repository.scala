package database.repositories

import akka.stream.alpakka.cassandra.scaladsl.CassandraSource
import akka.stream.scaladsl.{ Sink, Source }
import akka.{ Done, NotUsed }
import com.datastax.oss.driver.api.core.cql.Row
import database.Main.{ databaseCassandraSession, databaseExecutionContext, databaseSystem }

import scala.concurrent.Future

trait Repository {

  def createSource(
      stmt: String,
      params: List[Any]
  ): Source[Row, NotUsed] =
    CassandraSource(
      stmt,
      params.map(e => e.asInstanceOf[AnyRef]): _*
    )(databaseCassandraSession)

  def queryToEmptyResult(
      stmt: String,
      params: List[Any]
  ): Future[Done] =
    createSource(stmt, params)
      .runWith(Sink.ignore)

  def queryToSingleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Future[T] =
    createSource(stmt, params)
      .runWith(Sink.head)
      .map(castFunction)

  def queryToOptionalSingleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Future[Option[T]] =
    createSource(stmt, params)
      .runWith(Sink.headOption)
      .map { row =>
        if (row.nonEmpty) {
          Some(castFunction(row.get))
        } else {
          None
        }
      }

  def queryToMultipleResult[T](
      stmt: String,
      params: List[Any],
      castFunction: Row => T
  ): Future[Seq[T]] =
    createSource(stmt, params)
      .runWith(Sink.seq)
      .map(row => row.map(castFunction))
}
