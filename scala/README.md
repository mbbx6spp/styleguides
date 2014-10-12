# scala

I prefer to use the [Scala Style Guide] [1] found at scala-lang.org with the
exception of the following overrides.

## Scala Style Guide Overrides

### Packages

Fuck Java package naming conventions unless your Scala library is intended
for that audience. The verbosity of `{com,net,org,io,...}.` prefixes on
package names is so ... 1990s. I'm over it. Like Ace of Base.

[Here] [2] is some Ace of Base to remind you that you shouldn't use Java
conventions (unless you have to) for packaging in Scala.

### Accessors/Mutators

Just don't mutate attributes. Period. Now this section is not really
applicable (except the accessor convention part, which still holds).

See the 'Accessors/Mutators' sections in the style guide [here] [3] for
the parts of this that hold.

### Comprehensions

for-comprehensions which lack a yield clause should never be used. All other
parts of style in this section [here] [4] hold though.

### Files

Ignore this section of the official Scala Style Guide. I will provide a
file and structure convention that I think should be followed for more
functional styles of code.

TODO: Custom file and structure style guide for more functional Scala code
bases.

[1]: http://docs.scala-lang.org/style/ "Scala Style Guide"
[2]: https://www.youtube.com/watch?v=DNPjeIamsck "OMG Stop Already"
[3]: http://docs.scala-lang.org/style/naming-conventions.html#accessorsmutators "Accessors/Mutators"
[4]: http://docs.scala-lang.org/style/control-structures.html#comprehensions "Comprehensions"
