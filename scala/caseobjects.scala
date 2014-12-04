object sgcaseobjects {
  trait Direction
  case object North extends Direction
  case object East extends Direction
  case object South extends Direction
  case object West extends Direction
  case class OtherDirection(degree: Int) extends Direction

  object DirectionMain extends App {
    println("North.toString: " + North)
    println("South.hashCode: " + South.hashCode)
    println("Is West East? " + West.equals(East))
    println("Is East East? " + East.equals(East))
    println("What is this thing? " + OtherDirection(55))
  }
}
