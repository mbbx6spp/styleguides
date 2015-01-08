import java.util.{Calendar, GregorianCalendar}

trait Holiday
object Holiday {
  case object Christmas extends Holiday
  case object Chanukah extends Holiday
  case object Ramadan extends Holiday
  case object Diwali extends Holiday
  case class OtherHoliday(startDate: Calendar, endDate: Calendar, name: String) extends Holiday

  def apply(s: String): Holiday = s.toLowerCase match {
    case "christmas"    => Christmas
    case "xmas"         => Christmas
    case "chanukah"     => Chanukah
    case "hanukah"      => Chanukah // because I suck at spelling, maybe?
    case "ramadan"      => Ramadan // does anyone misspell this?
    case "diwali"       => Diwali
    case "divali"       => Diwali // because I always misspell this.
    // all other cases we assume the holiday is today and use the name given
    case name           => OtherHoliday(new GregorianCalendar, new GregorianCalendar, name)
  }
}

object HolidayMain extends App {

  def getHolidayFromUser: String = { println("What holiday are you celebrating today or soon?"); readLine }
  def getAnythingFromUser: String = { println("Type anything: "); readLine }

  val holiday: Holiday = Holiday(getHolidayFromUser)
  // This is an example of pattern matching
  val startDate2014: Calendar = holiday match {
    case Holiday.Christmas              => new GregorianCalendar(2014, Calendar.DECEMBER, 25)
    case Holiday.Chanukah               => new GregorianCalendar(2014, Calendar.DECEMBER, 16)
    case Holiday.Ramadan                => new GregorianCalendar(2014, Calendar.JUNE, 28)
    case Holiday.Diwali                 => new GregorianCalendar(2014, Calendar.OCTOBER, 23)
    case Holiday.OtherHoliday(s, _, _)  => s
  }
  println("Start date for holiday is: " + startDate2014.toString)

  // Here is another example of pattern matching that discriminates on the type
  // NOTE: I do not recommend doing anything like this in production code. If
  // you find yourself doing this specifically then you should refactor and
  // rethink your types immediately :)
  val s: String = getAnythingFromUser match {
    case s: String  => "String: " + s.mkString
    case x          => x.toString
  }

  println(s)
}
