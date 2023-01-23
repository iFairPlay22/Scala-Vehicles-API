package infra.requests.vehicles

import infra.requests.Request

case class VehicleDictRequest(
    vehicles: List[VehicleRequest]
) extends Request {}
