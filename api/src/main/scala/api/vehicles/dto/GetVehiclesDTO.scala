package api.vehicles.dto

import commons.data._DTO

case class GetVehiclesDTO(vehicles: Seq[VehicleDTO]) extends _DTO
