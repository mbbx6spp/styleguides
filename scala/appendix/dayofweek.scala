import java.util.{Calendar, GregorianCalendar}

sealed trait DayOfWeek
object DayOfWeek {
  case object Monday extends DayOfWeek
  case object Tuesday extends DayOfWeek
  case object Wednesday extends DayOfWeek
  case object Thursday extends DayOfWeek
  case object Friday extends DayOfWeek
  case object Saturday extends DayOfWeek
  case object Sunday extends DayOfWeek

  def apply(dowIndex: Int): Option[DayOfWeek] = dowIndex match {
    case Calendar.SUNDAY    => Some(Sunday)
    case Calendar.MONDAY    => Some(Monday)
    case Calendar.TUESDAY   => Some(Tuesday)
    case Calendar.WEDNESDAY => Some(Wednesday)
    case Calendar.THURSDAY  => Some(Thursday)
    case Calendar.FRIDAY    => Some(Friday)
    case Calendar.SATURDAY  => Some(Saturday)
    case _                  => None
  }

  def daysUntilNext(dow: DayOfWeek, date: Calendar): Int =
    sys.error("EXERCISE: Implement this function when you have time")
}

// Usage
object DayOfWeekMain extends App {
  val today: Calendar = new GregorianCalendar
  println(today)
  val dow: Option[DayOfWeek] = DayOfWeek(today.get(Calendar.DAY_OF_WEEK))
  println(dow)
}
