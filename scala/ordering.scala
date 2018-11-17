final case class Customer(name: String, id: String)

trait OrderingTypes {
  // First we start off with our "initial algebra" of algebraic data type
  // definitions of our problem space.
  sealed trait Ordering
  object Ordering {
    final case object LessThan extends Ordering
    final case object EqualTo extends Ordering
    final case object GreaterThan extends Ordering

    @inline def lessThan: Ordering    = LessThan
    @inline def equalTo: Ordering     = EqualTo
    @inline def greaterThan: Ordering = GreaterThan

    // convenience method
    def fromInt(i: Int): Ordering =
      if (i < 0)      { LessThan }
      else if (i > 0) { GreaterThan }
      else            { EqualTo }
  }

  // Now this is the trait that defines our "typeclass" in Scala.
  // This is a slight modification from what we see in Haskell since
  // we only need to implement one trait method for this type class and the
  // rest of the definitions of the functions from the Haskell typeclass
  // come for free, I decided to separate them.
  trait Ord[A] {
    def compare(a1: A, a2: A): Ordering
  }

  // Here are all the functions we may want to import into the local
  // scope where we would want to use them.
  object Ord {
    def minimum[A](a1: A, a2: A)(implicit ord: Ord[A]): A =
      if (ord.compare(a1, a2) == Ordering.lessThan) { a1 }
      else { a2 }

    def maximum[A](a1: A, a2: A)(implicit ord: Ord[A]): A =
      if (ord.compare(a1, a2) == Ordering.greaterThan) { a1 }
      else { a2 }

    def minimum[A](xs: Seq[A])(implicit ord: Ord[A]): Option[A] = xs match {
      case x :: rest =>
        Some(rest.foldLeft(x) { (a: A, b: A) =>
          if (minimum(a, b)(ord) == a) { a } else { b } })
      case Nil => None
    }

    def maximum[A](xs: Seq[A])(implicit ord: Ord[A]): Option[A] = xs match {
      case x :: rest =>
        Some(rest.foldLeft(x) { (a: A, b: A) =>
          if (maximum(a, b)(ord) == a) { a } else { b } })
      case Nil => None
    }

    def isMinimum[A](a1: A, a2: A)(implicit ord: Ord[A]): Boolean =
      (ord.compare(a1, a2) == Ordering.lessThan)

    def isMaximum[A](a1: A, a2: A)(implicit ord: Ord[A]): Boolean =
      (ord.compare(a1, a2) == Ordering.greaterThan)

    def ===[A](a1: A, a2: A)(implicit ord: Ord[A]): Boolean =
      ord.compare(a1, a2) == Ordering.equalTo

    def =/=[A](a1: A, a2: A)(implicit ord: Ord[A]): Boolean =
      ord.compare(a1, a2) != Ordering.equalTo
  }
}

trait OrderingInstances extends OrderingTypes {
  implicit val IntOrd = new Ord[Int] {
    def compare(x: Int, y: Int): Ordering =
      if (x < y)      { Ordering.lessThan }
      else if (x > y) { Ordering.greaterThan }
      else            { Ordering.equalTo }
  }

  implicit val DoubleOrd = new Ord[Double] {
    def compare(x: Double, y: Double): Ordering =
      if (x < y)      { Ordering.lessThan }
      else if (x > y) { Ordering.greaterThan }
      else            { Ordering.equalTo }
  }

  implicit val StringOrd = new Ord[String] {
    // delegating to Java's String compareTo here
    def compare(s1: String, s2: String): Ordering =
      Ordering.fromInt(s1.compareTo(s2))
  }

  implicit val CustomerOrd = new Ord[Customer] {
    def compare(c1: Customer, c2: Customer): Ordering =
      StringOrd.compare(c1.name, c2.name) match {
        case Ordering.EqualTo => StringOrd.compare(c1.id.mkString, c2.id.mkString)
        case o                => o
      }
  }
}

trait ListOrderFunctions extends OrderingTypes {
  def sort[A](xs: List[A])(implicit o: Ord[A]): List[A] =
    sortBy[A](o.compare)(xs)

  def sortBy[A](f: (A, A) => Ordering)(xs: List[A]): List[A] =
    sys.error("EXERCISE: Implement this as declaratively as possible.")
    // HINT: Quicksort can be written declaratively
}

object OrderingUsage extends OrderingTypes with OrderingInstances with ListOrderFunctions {
  val unorderedInts: List[Int] =
    5 :: 4 :: 7 :: 9 :: Nil

  val unorderedDoubles: List[Double] =
    6.3 :: 7.0 :: 3.2 :: 3.14159 :: 1.2 :: 95.4 :: Nil

  // TODO: Create list of customer values...
  val unorderedCustomers: List[Customer] =
    Customer("ACME Inc", "1234db6") ::
      Customer("XYZ Corp", "9876543") ::
      Customer("Bay Capital LLC", "5555555555") ::
      Nil

  // Try:
  //scala> import OrderingUsage._
  //import OrderingUsage._

  //scala> Ord.maximum(unorderedInts)
  //res1: Option[Int] = Some(9)

  //scala> Ord.minimum(unorderedInts)
  //res2: Option[Int] = Some(4)

  //scala> Ord.minimum(unorderedDoubles)
  //res3: Option[Double] = Some(1.2)

  //scala> Ord.maximum(unorderedDoubles)
  //res4: Option[Double] = Some(95.4)

  //scala> Ord.maximum(unorderedCustomers)
  //res5: Option[Double] = Some(Customer(XYZ Corp,9876543))

  //scala> Ord.minimum(unorderedCustomers)
  //res6: Option[Double] = Some(Customer(ACME Inc,1234db6))
}
