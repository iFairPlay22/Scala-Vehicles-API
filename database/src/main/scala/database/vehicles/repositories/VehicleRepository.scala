package database.vehicles.repositories

import akka.Done
import akka.actor.ActorSystem
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import com.datastax.oss.driver.api.core.cql.Row
import commons.system.database._CassandraRepositorySystem
import database.vehicles.keyspace.Keyspace._
import domain.vehicles.VehicleDomain

import java.time.{Instant, LocalDate}
import java.util.Date
import scala.concurrent.Future

class VehicleRepository(
    implicit val system: ActorSystem,
    implicit val cassandraSession: CassandraSession)
    extends _CassandraRepositorySystem {

  import VehicleRepository._

  def insertOrEdit(vehicle: VehicleDomain): Future[Done] =
    queryToEmptyResult(insertOrEditQuery, List(LocalDate.now(), vehicle.id, vehicle.name))

  def selectAllByDate(date: LocalDate): Future[Seq[VehicleDomain]] = {
    queryToMultipleResult(selectAllByDateQuery, List(date), mapRowToEntity)
  }

  private def mapRowToEntity(row: Row): VehicleDomain =
    VehicleDomain(row.getInt(table_vehicle_field_id), row.getString(table_vehicle_field_name))
}

object VehicleRepository {

  private val insertOrEditQuery: String =
    f"""
       > INSERT INTO $table_vehicle_name
       > ($table_vehicle_field_date_bucket, $table_vehicle_field_id, $table_vehicle_field_name)
       > VALUES (?, ?, ?);
       > """.stripMargin('>')

  private val selectAllByDateQuery: String =
    f"""
       > SELECT * FROM $table_vehicle_name WHERE $table_vehicle_field_date_bucket = ?;
       > """.stripMargin('>')
}
