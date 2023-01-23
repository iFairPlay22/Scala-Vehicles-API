package database.repositories

import akka.stream.scaladsl.{ Sink, Source }
import akka.{ Done, NotUsed }
import com.datastax.oss.driver.api.core.cql.Row
import database.constants.DatabaseConstants._
import database.throwables.{
  UnableToInsertOrEditVehicleInDatabaseException,
  UnableToSelectAllVehicleInDatabaseException,
  UnableToSelectOneVehicleInDatabaseException,
  UnableToTruncateTableInDatabaseException
}
import database.Main.{
  databaseCassandraSession,
  databaseConfig,
  databaseExecutionContext,
  databaseLogger,
  databaseSystem
}
import domain.positions.LatLongEntity
import domain.vehicles.VehicleEntity

import scala.collection.immutable
import scala.concurrent.Future

object VehicleRepository extends Repository {

  private def batchQuery(requests: List[String]): String = {

    val str_requests = requests.mkString("\n")

    f"""
       > BEGIN BATCH
       >
       > $str_requests
       >
       > APPLY BATCH;
       > """.stripMargin('>')
  }

  private def truncateTableQuery(): String =
    f"""
    > TRUNCATE $keyspace_universe_name.$table_vehicle_name;
    > """.stripMargin('>')

  private def updateVehicleByIdQuery(): String =
    f"""
    > UPDATE $keyspace_universe_name.$table_vehicle_name
    >     SET 
    >       $table_vehicle_field_name_route_id              = ?,
    >       $table_vehicle_field_name_run_id                = ?,
    >       $table_vehicle_field_name_heading               = ?,
    >       $table_vehicle_field_name_predictable           = ?,
    >       $table_vehicle_field_name_latitude              = ?,
    >       $table_vehicle_field_name_longitude             = ?,
    >       $table_vehicle_field_name_seconds_since_report  = ?
    >   WHERE
    >       $table_vehicle_field_id_id                      = ?;
    > """.stripMargin('>')

  private def selectAllQuery(): String =
    f"""
    > SELECT * FROM $keyspace_universe_name.$table_vehicle_name;
    > """.stripMargin('>')

  private def selectOneQuery(): String =
    f"""
    > SELECT * FROM $keyspace_universe_name.$table_vehicle_name WHERE id = ?;
    > """.stripMargin('>')

  def truncateTable(): Future[Either[UnableToTruncateTableInDatabaseException, Done]] =
    queryToEmptyResult(
      truncateTableQuery(),
      List.empty
    ).map(res => Right(res))
      .recover { _ =>
        Left(new UnableToTruncateTableInDatabaseException())
      }

  def insertOrEdit(vehicle: VehicleEntity): Future[Either[UnableToInsertOrEditVehicleInDatabaseException, Done]] =
    queryToEmptyResult(
      updateVehicleByIdQuery(),
      List(
        vehicle.routeId,
        vehicle.runId,
        vehicle.heading,
        vehicle.predictable,
        vehicle.latLong.latitude,
        vehicle.latLong.longitude,
        vehicle.secondsSinceReport,
        vehicle.id
      )
    ).map(res => Right(res))
      .recover { _ =>
        Left(new UnableToInsertOrEditVehicleInDatabaseException())
      }

  def insertOrEdit(
      vehicles: immutable.Iterable[VehicleEntity]
  ): Future[Either[UnableToInsertOrEditVehicleInDatabaseException, Done]] =
    Source(vehicles)
      .mapAsync(3)(vehicle => insertOrEdit(vehicle))
      .runWith(Sink.ignore)
      .map(res => Right(res))
      .recover { _ =>
        Left(new UnableToInsertOrEditVehicleInDatabaseException())
      }

  def selectAll(): Future[Either[UnableToSelectAllVehicleInDatabaseException, Seq[VehicleEntity]]] =
    queryToMultipleResult(
      selectAllQuery(),
      List(),
      mapRowToEntity
    ).map(res => Right(res))
      .recover { _ =>
        Left(new UnableToSelectAllVehicleInDatabaseException())
      }

  def selectOne(id: Int): Future[Either[UnableToSelectOneVehicleInDatabaseException, Option[VehicleEntity]]] =
    queryToOptionalSingleResult(
      selectOneQuery(),
      List(id),
      mapRowToEntity
    ).map(res => Right(res))
      .recover { _ =>
        Left(new UnableToSelectOneVehicleInDatabaseException())
      }

  // Transform a com.datastax.oss.driver.api.core.cql.Row to a PersonEntity
  private def mapRowToEntity(row: Row): VehicleEntity =
    VehicleEntity(
      row.getInt(table_vehicle_field_id_id),
      row.getInt(table_vehicle_field_name_route_id),
      row.getInt(table_vehicle_field_name_run_id),
      row.getInt(table_vehicle_field_name_heading),
      row.getBoolean(table_vehicle_field_name_predictable),
      LatLongEntity(
        row.getDouble(table_vehicle_field_name_latitude),
        row.getDouble(table_vehicle_field_name_longitude)
      ),
      row.getInt(table_vehicle_field_name_seconds_since_report)
    )

}
