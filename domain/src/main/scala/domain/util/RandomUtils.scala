package domain.util

import scala.collection.mutable

object RandomUtils {
  val rand = new scala.util.Random

  def randomInt(start: Int = 0, end: Int = Int.MaxValue): Int =
    rand.between(start, end)

  def randomBool(): Boolean = rand.nextBoolean()

  def randomString(length: Int) = {
    val sb = new mutable.StringBuilder
    for (_ <- 1 to length)
      sb.append(rand.nextPrintableChar)
    sb.toString
  }
}
