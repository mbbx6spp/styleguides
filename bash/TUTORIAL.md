## Things You Should Know

### You can declare variables, so do it already

When defining script local (outside of function scope) variables you should
`declare` your variables. If the variables will not be `export`-ed externally
to the calling script you should name them in lower case separating words
with underscore, for example:

```bash

declare -r root_path="$(basename "$0")" # note it's readonly because of the -r :)
# Bash actually supports arrays, yup. It's true, see the -a designation below
declare -a subdirs=(app conf db lib spec) # yep, the subdirs in many Rails apps (last time I looked)
declare -i default_port=8080 # note I declared it as an "int" with -i

# Even exported variables should be declared in your script like this:
declare -r MY_ENV_VAR="OMGWTFBBQ"
# Then later in the script export it like so
export MY_ENV_VAR

# You can create a readonly int var like so:
declare -ir default_port=8080
# Then if you use it like this:
#   default\_port=3000
# somewhere else in your script you will get:
#   -bash: default\_port: readonly variable

```

### You can create functions. Yes really.

What are you waiting for? If you find yourself repeating yourself a lot define
new functions for this like so:

```bash
# Used in one of our CI scripts and assumes GIT\_BIN,
# GIT\_REMOTE and GERRIT\_BRANCH are set by Jenkins already.
function tag_release() {
  local -r sha="$("${GIT_BIN}" rev-parse --short HEAD)"
  local -r ts="$(date -u '+%Y%m%dT%H%M%S')"

  if [ "${GERRIT_BRANCH}" = "master" ] && [ ! -z "${sha}" ]; then
    "${GIT_BIN}" tag -a "releases/${sha}-${ts}" -m "Production release ${sha} at ${ts}"
    "${GIT_BIN}" push "${GIT_REMOTE}" --tags
  fi
}
# This is an abridged version, but you get the idea hopefully.

# You should return non-zero values from functions when an
# error has occured. Use exit codes that are consistent with
# the styleguide you are using
# This accepts arguments and runs some "validations" on it.
function bla() {
  local -r name="${1}"
  local -ir age=${2}

  if [ -z "${name}" ]; then
    >&2 echo "You need a non empty name"
    return 1
  fi

  if [ "${age}" -lt 18 ]; then
    >&2 echo "You are under age, sorry"
    return 1
  fi

  if [ "${age}" -gt 60 ]; then
    echo "Good day ${name}"
  else
    echo "Yo, what up ${name}?"
  fi
}

# Use this like:
#  bla "Casey" 32
#  bla "John" 83
#  bla "Charlie" 4
```

Remember these "functions" are subroutines in reality like in other procedural
languages. These aren't functions in the math-y sense. And for what you will
use Bash for that is probably ok.

Make sure you always declare function-local variables with `local` inside the
function itself. Also remember you can use the same specifications as declare
such as:
* `-r` - for readonly
* `-i` - for integer
* `-a` - for array

You can make an array or an integer readonly with `-ar` and `ir` respectively.

### Using arrays

Yes, arrays are supported in Bash. You can use them like so:

```bash

declare -ar rails_app_subdirs=(app conf db lib spec)

for dir in "${rails_app_subdirs[@]}"; do
  echo "${dir}"
done
```

### Use POSIX compliant conditionals

Apparently there are two styles of conditionals syntax in Bash for control
flow constructs like `if`, `while`, etc.

Use the `[]` version of `test` (see `man 1 test`). Some gotchas are:
* it isn't `==` for equality check but `=`
* Numeric comparisons are done using: `-lt`, `-gt`, `-eq`, `-ne`, etc.
* When comparing strings put quotes around variables :)
* Remember to `man 1 test` for more details
* Use `[[]]` when you need to do glob matching


Here are some examples:

```bash

if [ "${USER}" = "${expected_user}" ]; then
  # ...
fi

if [ "${default_port}" -lt 1024 ]; then
  # do something like `sudo` if necessary for priviledged ports?
fi

if [[ "${value}" =~ ".*$name.*" ]]; then
  echo 'Yo dawg, we heard you like globs'
fi

```

### Defaulting values of variables

Sometimes you need to set defaults to variables when you're initializing your
script. For example, you might want the user to be able to override a
default using an environment variable or script argument they set when
running a script.

You might do something like this:

```bash
if [ $# -lt 1 ]; then
  declare -r server_name="default.example.com"
else
  declare -r server_name="${1}"
fi
```

This is an awful lot of boilerplate for something so simple. Good news!
Bash offers you two ways to do *defaulting* of variables.

```bash
declare -r server_name="${1:-https://default.example.com}"
```

If `$1` is empty string, i.e. someone purposely did: `mycommand ""`
then it would still use the default using the `:-` defaulting method.

Alternatively you might want this instead (in a different situation):

```bash
unset MY_ENV_PREFIX
declare prefix="${MY_ENV_PREFIX->>>}"
echo "${prefix}" # shows >>>

MY_ENV_PREFIX=???
declare prefix="${MY_ENV_PREFIX->>>}"
echo "${prefix}" # shows ???

MY_ENV_PREFIX=""
declare prefix="${MY_ENV_PREFIX->>>}"
echo "${prefix}" # shows  (empty string)
```

Which you use will depend on which case you prefer to handle.

I personally usually use the `:-` method with only a couple of exceptional
cases where I use the `-` default. I wouldn't make a rule on this other
than the usage has to fit the actual requirements.

### Case statements don't have to be evil

Here is an example of using a `case` statement in Bash:

```bash

case "${answer}" in
  yes|y|y*)
    echo "yes"
    break
    ;;
  no|n|nope)
    echo "no"
    break
    ;;
  *)
    echo "wtf?"
    break
    ;;
esac

```

Use appropriately. You're an engineer, so think about what that means for you
and your team.

