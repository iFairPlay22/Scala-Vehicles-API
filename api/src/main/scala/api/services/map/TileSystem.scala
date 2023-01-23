package api.services.map

import domain.positions.{ LatLongEntity, PixelsEntity, QuadKeyEntity, TilesEntity }

import scala.collection.mutable

object TileSystem {
  final val LevelOfDetail = 3
  final val ScreenDpi     = 96
  final val EarthRadius   = 6378137
  final val MinLatitude   = -85.05112878
  final val MaxLatitude   = 85.05112878
  final val MinLongitude  = -180
  final val MaxLongitude  = 180

  // Clips a number to the specified minimum and maximum values.
  private def clip(n: Double, minValue: Double, maxValue: Double): Double =
    math.min(math.max(n, minValue), maxValue)

  // Determines the map width and height (in pixels)
  def mapSize(): Int =
    256 << LevelOfDetail

  // Determines the ground resolution (in meters per pixel)
  def groundResolution(latitude: Double): Double = {
    val normalized_latitude = clip(latitude, MinLatitude, MaxLatitude);
    math.cos(normalized_latitude * Math.PI / 180) * 2 * Math.PI * EarthRadius / mapSize();
  }

  // Determines the map scale
  def mapScale(latitude: Double): Double =
    groundResolution(latitude) * ScreenDpi / 0.0254;

  // Converts a point from latitude/longitude WGS-84 coordinates (in degrees)
  def latLongToPixelXY(pos: LatLongEntity): PixelsEntity = {
    val normalized_latitude: Double  = clip(pos.latitude, MinLatitude, MaxLatitude);
    val normalized_longitude: Double = clip(pos.longitude, MinLongitude, MaxLongitude);

    val sinLatitude: Double = math.sin(normalized_latitude * Math.PI / 180);
    val mapSizeValue: Int   = mapSize();

    val x = (normalized_longitude + 180) / 360;
    val y = 0.5 - math.log((1 + sinLatitude) / (1 - sinLatitude)) / (4 * Math.PI);

    val pixelX = clip(x * mapSizeValue + 0.5, 0, mapSizeValue - 1);
    val pixelY = clip(y * mapSizeValue + 0.5, 0, mapSizeValue - 1);

    PixelsEntity(pixelX.intValue, pixelY.intValue)
  }

  // Converts a pixel from PixelsEntity XY coordinates at a specified level of detail into latitude/longitude WGS-84 coordinates (in degrees).
  def pixelXYToLatLong(pixels: PixelsEntity): LatLongEntity = {
    val mapSizeValue = mapSize();

    val x = (clip(pixels.pixelX, 0, mapSizeValue - 1) / mapSizeValue) - 0.5;
    val y = 0.5 - (clip(pixels.pixelY, 0, mapSizeValue - 1) / mapSizeValue);

    val latitude: Double  = 90 - 360 * math.atan(math.exp(-y * 2 * Math.PI)) / Math.PI;
    val longitude: Double = 360 * x;

    LatLongEntity(latitude, longitude)
  }

  // Converts pixel XY coordinates into tile XY coordinates of the tile containing the specified pixel.
  def pixelXYToTileXY(pixels: PixelsEntity): TilesEntity =
    TilesEntity(pixels.pixelX / 256, pixels.pixelY / 256)

  // Converts tile XY coordinates into pixel XY coordinates of the upper-left pixel of the specified tile.
  def tileXYToPixelXY(tiles: TilesEntity): PixelsEntity =
    PixelsEntity(tiles.tileX * 256, tiles.tileX * 256)

  // Converts tile XY coordinates into a QuadKey at a specified level of detail.
  def tileXYToQuadKey(tiles: TilesEntity): QuadKeyEntity = {
    val quadKey: mutable.StringBuilder = new mutable.StringBuilder()
    for (i <- LevelOfDetail to 1 by -1) {
      var digit: Int = 0;
      val mask: Int  = 1 << (i - 1);

      if ((tiles.tileX & mask) != 0) {
        digit += 1;
      }
      if ((tiles.tileY & mask) != 0) {
        digit += 1;
        digit += 1;
      }

      quadKey.addOne(digit.toChar)
    }

    QuadKeyEntity(quadKey.result())
  }

  // Converts a QuadKey into tile XY coordinates.
  def quadKeyToTileXY(quadKey: String): TilesEntity = {
    var tileX: Int = 0
    var tileY: Int = 0

    for (i <- LevelOfDetail to 1 by -1) {
      val mask = 1 << (i - 1);
      val el   = quadKey.charAt(LevelOfDetail - i)

      el match {
        case '0' => {}

        case '1' => { tileX |= mask }

        case '2' => { tileY |= mask }

        case '3' => {
          tileX |= mask;
          tileY |= mask;
        }

        case _ =>
          throw new IllegalArgumentException("Invalid QuadKey digit sequence: %s.".format(quadKey));
      }
    }

    TilesEntity(tileX, tileY)
  }

}
