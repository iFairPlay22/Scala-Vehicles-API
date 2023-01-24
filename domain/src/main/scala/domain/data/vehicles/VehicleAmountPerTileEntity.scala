package domain.data.vehicles

import domain.data.Entity

case class VehicleAmountPerTileEntity(data: Map[String, Int] = Map()) extends Entity {}

object VehicleAmountPerTileEntity {

  def addEntry(entity: VehicleAmountPerTileEntity, tileId: String): VehicleAmountPerTileEntity =
    entity.copy(
      entity.data ++
        Map[String, Int](tileId -> (entity.data.getOrElse(tileId, 0) + 1))
    )
}
