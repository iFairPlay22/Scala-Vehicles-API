package api.services.vehicles

import api.services.Service
import domain.data.vehicles.{ VehicleAmountPerTileEntity, VehicleEntity }
import api.Main.{ apiConfig, apiExecutionContext, apiLogger, apiSystem }
import api.controllers.mappers.{
  LatLongResponseMapper,
  TilesResponseMapper,
  VehicleAmountPerTileMapper,
  VehicleResponseMapper
}
import api.controllers.responses.{
  CountAvailableVehiclesPerTileResponse,
  GetAvailableVehiclesInTileResponse,
  GetAvailableVehiclesResponse,
  GetFilledTilesResponse,
  GetLastPositionOfVehicleResponse
}
import api.services.map.MapService
import database.repositories.VehicleRepository
import database.throwables.DatabaseException
import domain.data.positions.{ LatLongEntity, TilesEntity }

import scala.concurrent.Future

object VehicleService extends Service {

  def getAvailableVehiclesResponse()
      : Future[Either[DatabaseException, GetAvailableVehiclesResponse]] =
    for {
      eitherErrorOrVehicles <- VehicleRepository.selectAll()
    } yield {
      eitherErrorOrVehicles match {
        case Right(vehicles) => Right(VehicleResponseMapper.entityToResponse(vehicles))
        case Left(error) => Left(error)
      }
    }

  def getLastPositionOfVehicleResponse(
      vehicle_id: Int
  ): Future[Either[DatabaseException, GetLastPositionOfVehicleResponse]] =
    for {
      eitherErrorOrMaybeVehicle <- VehicleRepository.selectOne(vehicle_id)
    } yield {
      eitherErrorOrMaybeVehicle match {
        case Right(maybeVehicle) =>
          maybeVehicle match {
            case Some(vehicle) => Right(LatLongResponseMapper.entityToResponse(vehicle.latLong))
            case None =>
              throw new NoSuchElementException(
                "Unable to get vehicle last position : No vehicle found!"
              )
          }
        case Left(error) => Left(error)
      }
    }

  def getFilledTiles(): Future[Either[DatabaseException, Seq[TilesEntity]]] =
    for {
      eitherErrorOrVehicles <- VehicleRepository.selectAll()
    } yield {
      eitherErrorOrVehicles match {
        case Right(vehicles) =>
          Right(vehicles.map(vehicle => MapService.getTilesFromLatLong(vehicle.latLong)))
        case Left(error) => Left(error)
      }
    }

  def getFilledTilesResponse(): Future[Either[DatabaseException, GetFilledTilesResponse]] =
    for {
      eitherErrorOrFilledTiles <- getFilledTiles()
    } yield {
      eitherErrorOrFilledTiles match {
        case Right(filledTiles) => Right(TilesResponseMapper.entityToResponse(filledTiles.toSet))
        case Left(error) => Left(error)
      }
    }

  def getAvailableVehiclesInTileResponse(
      tileX: Int,
      tileY: Int
  ): Future[Either[DatabaseException, GetAvailableVehiclesInTileResponse]] = {

    val tileQuadKey = MapService.getPixelsFromTiles(TilesEntity(tileX, tileY))
    for {
      eitherErrorOrVehicles <- VehicleRepository.selectAll()
    } yield {
      eitherErrorOrVehicles match {
        case Right(vehicles) =>
          Right(
            VehicleResponseMapper.entityToResponse2(
              vehicles
                .filter(vehicle => MapService.getPixelsFromLatLong(vehicle.latLong) == tileQuadKey)
            )
          )
        case Left(error) => Left(error)
      }

    }
  }

  def countAvailableVehiclesResponse()
      : Future[Either[DatabaseException, CountAvailableVehiclesPerTileResponse]] =
    for {
      eitherErrorOrFilledTiles <- getFilledTiles()
    } yield {

      eitherErrorOrFilledTiles match {
        case Right(filledTiles) =>
          var dict: VehicleAmountPerTileEntity = VehicleAmountPerTileEntity()
          filledTiles
            .foreach(tiles =>
              dict = VehicleAmountPerTileEntity.addEntry(dict, TilesEntity.tilesToKey(tiles))
            )
          Right(VehicleAmountPerTileMapper.entityToResponse(dict))
        case Left(error) => Left(error)
      }

    }

}
