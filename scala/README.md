# scala

I prefer to use the [Scala Style Guide] [1] found at scala-lang.org with the
exception of the following overrides.

## Scala Style Guide Overrides

### Packages

Forget Java package naming conventions unless your Scala library is intended
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

## Dependencies

### Versions

As of the time of writing new projects should not use a version of Scala less
than 2.11.3 (the latest stable). Existing projects should keep up to date with
the latest minor release of the same major (or beyond) as much as possible.

### Build Tool

Scala projects that are primarily Scala with some Java should use SBT as a
build tool. More polyglot projects might need to reconsider the build tool
of choice based on their requirements.

## Linting

* Try to use one or more of the following linting or style checking tools:
  * scalac-scapegoat ([SBT plugin] [5]) - typical linter checks, e.g unused
    method params, type shadowing, use of var, slow list append, empty if, and
    much more.
  * scalastyle ([SBT plugin] [6]) - typical code style checks, i.e. no static
    analysis checks like scalac-scapegoat, so feel free to use together.
  * WartRemover ([SBT plugin] [7]) - provides additional linting checks to first
    option for the engineer concerned about type safety.

TODO: Custom file and structure style guide for more functional Scala code
bases.

[1]: http://docs.scala-lang.org/style/ "Scala Style Guide"
[2]: https://www.youtube.com/watch?v=DNPjeIamsck "OMG Stop Already"
[3]: http://docs.scala-lang.org/style/naming-conventions.html#accessorsmutators "Accessors/Mutators"
[4]: http://docs.scala-lang.org/style/control-structures.html#comprehensions "Comprehensions"
[5]: https://github.com/sksamuel/scalac-scapegoat-plugin "scalac-scapegoat-plugin for SBT"
[6]: https://github.com/scalastyle/scalastyle-sbt-plugin "scalastyle-sbt-plugin"
[7]: https://github.com/typelevel/wartremover "WartRemover SBT plugin"
