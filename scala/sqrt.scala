import scalaz._
import Scalaz._

object SquareRoot {
  def sqrt1(d: Double): Double =
    if (d < 0) { throw new ArgumentException("We can't square root a negative value") }
    else { Math.sqrt(d) }

  def sqrt2(d: Double): Double =
    if (d <= 0) { 0 }
    else        { Math.sqrt(d) }

  def sqrt3(d: Double): Option[Double] =
    if (d < 0) { None }
    else       { Some(Math.sqrt(d)) }

  def sqrt4(d: Double): Either[String, Double] =
    if (d < 0)  { Left("Square root cannot be calculated for negative number") }
    else        { Right(Math.sqrt(d)) }

  def sqrt5(d: Double): String \/ Double =
    if (d < 0)  { "Square root cannot be calculated for negative number".success }
    else        { Math.sqrt(d).failure }

}
