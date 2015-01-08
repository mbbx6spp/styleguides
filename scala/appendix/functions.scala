object sgfunctions {
  // We can set named values to function definitions
  val f1 = (x: Int) => x*x // <1>
  val f2: Int => Int => Int = (x) => (y) => x*y // <2>
  val f3: Int => Int = f2(2) // <3>

  // Here we are defining methods without parameters the same way
  def m1 = (x: Int) => x*x // <4>
  def m2: Int => Int => Int = (x) => (y) => x*y // <5>
  def m3: Int => Int = m2(2)

  // Q: What is the difference between f1 and m1 or f2 and m2?
  // A: Parameterless methods name expressions that are re-evaluated
  // each time the the parameterless method name is references.
  // A value definition `val x: T = e` defines `x` as a name that results
  // from the evaluation of `e`.

  // There is also a `lazy val`, which gets it's RHS expression evaluated
  // the first time it is referenced in the code.
  lazy val x: Int = 384*2343*999 // only evaluated if/when first referenced

  // Now experiment with method definitions more...this time with named
  // parameters and look at parameter groups too. Oooooh.
  def doubler(x: Int): Int = 2*x
  def prefixer(s: String): String = " >> " + s

  def multiplier(m: Int)(x: Int) = m*x
  // What does this multiple parameter grouping offer us?
  def tripler(x: Int): Int = multiplier(3)(x)
  // Alternatively we could do the following
  def quadrupler = multiplier(4) _
  // Note the trailing `_` on a partially applied function above. It tells
  // the compiler "treat this as a function where not all the parameter groups
  // are applied yet".
  // What is the type signature of quadrupler. Why?

  // We can also have multiple arguments in a group. This means we can't apply
  // arguments one by one to the function at different points in the code.
  def greetUser(name: String, age: Int): Unit =
    println(s"Hi, ${name}. You are ${age}. Awesome.")

  def transformText(t: String => String, s: String): String = t(s)

  def upperCase(s: String): String = s.toUpperCase
  def lowerCase(s: String): String = s.toLowerCase
  def parens(s: String): String = "(" + s + ")"
  def prefix(p: String)(s: String): String = p + s

  // Higher Order Polymorphic Functions
  def g1[A, B](f: A => B)(as: List[A]): List[B] = for (a <- as) yield f(a)
  def g2[A, B](f: A => List[B]): A => List[B] = f(_).reverse

  object KylesMain extends App {
    println("Hi, Kyle!")
    val square = f1(16)
    println("Square of 16: " + square)
  }
}

