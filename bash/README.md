# bash

## Overview

Bash is a ubiquitous shell scripting language that I may or may not replace
with a more suitable candidate in light of the Shellshock vulnerabilities.

However, it is ubiquitous on Linux and widely supported on other UNIX-like
operating systems. So it is hard to "replace". :)

## Influences

Influences of this style guide for Bash are numerous, but a large part of it
is simply having written Bash shell scripts for years without realizing it's
basic constructs until recently when I dived in and actually read the
documentation and spent time to improve my own skills, the hard way.

### Ode To Ben

However, a coworker (Ben Smith) has influenced me to make Bash scripts as
elegant, less error prone, and enjoyable to write as possible. For this I
thank you, although the SREs that request my reviews on their Bash scripts
probably do not thank you. I think I wrote an essay again on a simple 100
line bash script review citing the reasons to prefer one style over another.
And this is the whole reason I bothered to create this repository.

Thank you! (: (Note: the left handed smiley, just for you.)

## Background Material

Here is a list of sources for Bash scripting that I am basing my style
guide on:

* [The Bash Guide](http://guide.bash.academy/)
* [Bash FAQ](http://mywiki.wooledge.org/BashFAQ/)
* [Bash Pitfalls](http://mywiki.wooledge.org/BashPitfalls)

The above provides good material to get started and also for reference later
on as you write better and better Bash scripts.

Now read how to write usable command-line interfaces:
[Hints for writing Unix tools](http://monkey.org/~marius/unix-tools-hints.html)

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

  if [ ${age} -lt 18 ]; then
    >&2 echo "You are under age, sorry"
    return 1
  fi

  if [ ${age} -gt 60 ]; then
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

Here are some examples:

```bash

if [ "${USER}" = "${expected_user}" ]; then
  # ...
fi

if [ ${default_port} -lt 1024 ]; then
  # do something like `sudo` if necessary for priviledged ports?
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

## Appropriateness

Here I will shed light on when I think it is appropriate to use Bash for
a task instead of the plethora of other shell, scripting, or compiled
langauges available to us every day.

Appropriate uses for a Bash script are:

* Gluing together various shell commands and handling errors appropriately
* Anything that should not require any special runtimes, interpreters or
  langauges installed on the target run hosts.
* Any script that does not require using a data structure more complex than
  an array. (Yes, Bash 4 has support for associative arrays now, but most
  of our target systems are still Bash 3.)
* Anything you would have done manually on the command line twice should be
  put in a script in some repo. Otherwise please get back to administering
  a Solaris box at your local university.

## Conventions

### Usage
* All scripts must take a -h argument and return the usage. The usage should
  be in BNF or EBNF:
  http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form
* The usage must include every option and argument the script will accept.
* The script must include usable examples below the EBNF. This is really
  helpful when testing code reviews too.
* The script should use lower case for most arguments, unless the option
  'negates' in which case it should be upper case.
* Adding a `-f` option to bypass error checking can really help you in a bind.
* The script should never take any irreversible actions without prompting or
  having `-f` passed.
* Adding a `-d` that does `set -x` is also awesome.
* If you are using `read` you probably did something wrong. The user should
  be able to supply all input on the command line.

### Variables
* All variables in functions must be locally scoped.
* All variables anywhere should be declared.
* Globally exported variables must be in all uppercase.
* Globally exported variables should be prefixed with an indicator that it
  was created by in house code. i.e. for company Widgets, `W_VAR_NAME` would
  be most excellent
* Variable names should use _ as a word separator
* Locally scoped variables should be in lower case
* Variable names should be meaningful, short names are nobody's friend

### Functions
* All locally created functions should be prefixed with an indicator that
  it was created by in house code, similar to variable naming
* Function names should be in lower case and use _ as a word separator
* A function shouldn't depend on `set -e` being set from outside its own
  context. If a function *fails* it should `return` a non-zero positive
  integer from the function that corresponds to the exit code it would like
  returned in the larger context.

When testing conditions in your function you should `return` the appropriate
non-zero integer when necessary.
```bash
function a() {
  echo "starting a()"
  false
  echo "ending a()"
}

function b() {
  echo "b()"
}

set +e
a; b;
# The above should output the following:
# starting a()
# ending a()
# b()

set -e
a; b;
# The above should output the following and return with non-zero exit:
# starting a()
```

Choose the most appropriate way of handling your function. Although in my
Bash script I always use `set -ue` after all the basic definitions and checks
are run.

### Errors

* All error messages should go to `STDERR`.
* All scripts should `set -e` to exit on error.
* All scripts should `set -u` to help prevent unbound variables.
* Doing trap catching to extend debugging output in the event of an error
  will in fact get you laid. This example shows how you would do that
  in a sourced library script:

      function j_its_a_trap() {
        local script_name="$0"
        local trap_name="$1"
        local last_line="$2"
        local last_error="${4:-1}"
        if [ -z "${J_DISABLE_ERR_TRAP:-}" ]; then
          echo "J-ERROR: ${trap_name} ${script_name}: line ${last_line}: return code: ${last_error}" >&2
          echo "J-ERROR: [line: $( caller )] $*" >&2
        fi
      }

      function j_enable_traps() {
        local force="${1:-}"
        unset J_DISABLE_ERR_TRAP
        if ! tty >/dev/null 2>&1 || [ -n "$force" ]; then
          trap 'j_its_a_trap "SIGERR" ${LINENO} ${$?}' ERR
          trap 'j_its_a_trap "SIGINT" ${LINENO} ${$?}' INT
          trap 'j_its_a_trap "SIGHUP" ${LINENO} ${$?}' HUP
          trap 'j_its_a_trap "SIGTERM "${LINENO} ${$?}' TERM
          trap 'j_its_a_trap "SIGKILL" ${LINENO} ${$?}' KILL
          trap 'j_its_a_trap "SIGQUIT" ${LINENO} ${$?}' QUIT
        else
          trap 'j_kill_waiting "SIGINT" ${LINENO} ${$?}' INT
        fi
      }

      j_enable_traps
* All scripts encountering an unhandlable error *must* report a non-zero exit
  code. The exit code should conform to common conventions outlined below.

      | Exit Code | Reason |
      |-----------|--------|
      | 0         | Ok, successful run of script. |
      | 1         | Catchall for general errors.  |
      | 2         | Missing command, keyword, or permission problem. |
      | 126       | Command invoked cannot execute. |
      | 127       | Command not found. |
      | 128       | Invalid argument to exit. |
      | 128+N     | Fatal error signal received where N is signal sent process.|

  For example, 130 corresponds to a Ctl+C-ed process since signal of 2 is sent
  to the process which typically terminates it.

### Security

* When storing sensitive information in a file location (temporary or
  permanent) ensure you set umask for permissions before creating the
  file where this sensitive data will be stored. You do this like so:
  `umask 0177`.
* When using temporary files, you *must* use the `mktemp` command which will
  create a new file with the specified template. It makes sure that no file
  or symbolic link already exists at that path. You would use this like so:
  `declare tmp_filename="$(mktemp /tmp/appname.secret.XXXX)"`
* The `mktemp` prefix should indicate which script created the file
* There should be a user specific TMP directory defined, most likely as
  `$W_TMP`. All mktemp operations should happen in here instead of just `/tmp`
* If the script creates a lot of temporary files they should be placed in
  an appropriately secured directory specific to the application. It should
  use something similar to `my_tmp_dir=$(mktemp $W_TMP/appname.XXXXX)` to do
  this.
* You *must* never use `eval` or I will slap you with a wet tuna until you
  beg for forgiveness.
* SUID and SGID *must* never be used on shell scripts.

### Output

* Colors should be used rarely if ever. Not everyone's terminal supports them
  and they can make the output look horrible for people using screen or other
  non-vt220 termcaps.
* Superfluous headers and whitespace should be avoided.
* Non-error output must always go to `STDOUT`
* Errors must always be sent to `STDERR`
* Output should be easy to parse using grep, sort, etc
* Output should not require extra context to understand, variable names should
  be shown so the user doesn't need a template or legend to read it.

      echo "=========================================================="
      echo "Push Stats"
      echo "Started $start_date"
      echo "Finished $end_date"
      echo "=========================================================="
      echo "Version: $version"
      echo "Repository: $repository"
      echo "Started: $start_date"
      echo "Ended: $end_date"
      echo "Phases: $phase_count"
      echo "Services: $service_count"
      echo "j-updates: $ssh_count"
      echo "Elapsed $total_time seconds"
      echo "=========================================================="

* Machine parsable output example of above sent to the logger for later stats
  gathering or possibly event alerting.

      echo "deploy: start_date=${start_date}|end_date=${end_date}|\
      version=${version}|repository=${repository}|phase_count=${phase_count}|\
      service_count=${service_count}|ssh_count=${ssh_count}|\
      total_time=${total_time}" | logger

### Naming

#### Files

* Executables should have no extension (preferred) or a .sh extension.
* Libraries *must* have a .sh, .env, .functions extension and *should* not be
  executable.
* Script names should be easily readable and convey useful information about
  the functionality of the script.
  i.e. j-git-migrate-branches, j-config-deploy

### Structure

#### Indentation

* Use 2 soft spaces. No tabs.
* All continued lines should be indented further than the line it extends

#### Line Length

* All lines should be 80 characters or less, use `\` to extend lines (and
  indent!)

#### Comments

* Each file should start with a comment block that describes its contents,
  its purpose, its requirements/expectations, and its limitations. Only a
  shebang is permitted above a header comment block in an executable script
  due to necessity.
* Each function which is non-trivial in functionality should have a comment
  header that describes the inputs, outputs and side effects of the function.
* Keep track of what you plan on improving using TODO comments.
* Explain tricky lines of the script inline immediately above the offending
  line and explain why it is 'tricky'. (Note: tricky is never good, but
  sometimes necessary.)

#### Style

* Always use `$(command)` instead of backticks.
  [Here are the reasons why](http://mywiki.wooledge.org/BashFAQ/082)
* You *must* put `; do` and `; then` on the same line as the `while`, `for`
  or `if`.

### Tools

Agree on a linting/checkstyle tool to use for your projects that incorporate
Bash scripts.

I strongly suggest [`shellcheck`] [1]. You can install this after you have
installed Haskell. You can thank me later, but for now, run your install
command for Haskell (either GHC plus cabal and happy, etc. or Haskell Platform)
before you go out to lunch. :)

Even though typically only useful in your Bash "libraries" of functions you
might also want to consider if using [`shunit2`] [2] is appropriate or not.

[1]: http://www.shellcheck.net/about.html "About ShellCheck"
[2]: https://code.google.com/p/shunit2/ "shunit2 Bash testing"
