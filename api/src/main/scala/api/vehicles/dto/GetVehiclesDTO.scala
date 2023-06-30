package api.vehicles.dto

import http._DTO

case class GetVehiclesDTO(vehicles: Seq[VehicleDTO]) extends _DTO
