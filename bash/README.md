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
* [Advanced Bash Scripting Guide](http://www.faqs.org/docs/abs/HTML/)

The above provides good material to get started and also for reference later
on as you write better and better Bash scripts.

Now read how to write usable command-line interfaces:
[Hints for writing Unix tools](http://monkey.org/~marius/unix-tools-hints.html)

## Things You Should Know

See the [Tutorial](TUTORIAL.md) here for a quick run down of the important
parts of Bash to employ in writing scripts before adopting conventions at
all or your scripts will be unreadable and unmaintainable anyway. :)

The point of standards and conventions is for your team to be able to grow,
prune, maintain, and question the purpose of your Bash scripts more easily.
Consistency is the key. You can always fork this and modify the conventions
to suit your team's preferences. That is kind of the point.

Now you should evaluate appropriateness of use of Bash in the first place...

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
  be in [BNF or EBNF](http://en.wikipedia.org/wiki/Extended_Backus%E2%80%93Naur_Form)
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
  be able to supply all input on the command line. The one exception to this
  is reading data in from other commands.

### Running
* All scripts that could be sourced or be executed should separate sourcing
  and execution paths. See below

```bash
if [ "${BASH_SOURCE[0]}" != "$0" ]; then
  export -f func1
  # and any other functions and/or variables to export
else
  set -eu
  main "${@}"
  exit 0
fi
```

### Variables
* All variables in functions must be locally scoped.
* All variables anywhere should be declared.
* Globally exported variables must be in all uppercase.
* Globally exported variables should be prefixed with an indicator that it
  was created by in house code. i.e. for company Widgets, `W_VAR_NAME` would
  be most excellent
* Variable names should use `_` as a word separator
* Locally scoped variables should be in lower case
* Variable names should be meaningful, short names are nobody's friend
* Variables should usually be wrapped with `{}` when doing expansion, i.e.
```bash
if [ "${J_YO_DAWG}" ]
```
* Quote early and often! Use ' when you can, " when you can`t. Random
  spaces appearing in variables can cause weird problems!
* String manipulation can be done inside bash. You don't always have to use
  sed. Some examples, taken from [Robert Muth's Better Bash Scripting](http://robertmuth.blogspot.com/2012/08/better-bash-scripting-in-15-minutes.html)
```bash
Basics

f="path1/path2/file.ext"
len="${#f}" # = 20 (string length)

# slicing: ${<var>:<start>} or ${<var>:<start>:<length>}
slice1="${f:6}" # = "path2/file.ext"
slice2="${f:6:5}" # = "path2"
slice3="${f: -8}" # = "file.ext"(Note: space before "-")
pos=6
len=5
slice4="${f:${pos}:${len}}" # = "path2"

Substitution (with globbing)

f="path1/path2/file.ext"

single_subst="${f/path?/x}"   # = "x/path2/file.ext"
global_subst="${f//path?/x}"  # = "x/x/file.ext"

# string splitting
readonly DIR_SEP="/"

array=(${f//${DIR_SEP}/ })
second_dir="${array[1]}"     # = path2

Deletion at beginning/end (with globbing)

f="path1/path2/file.ext"

# deletion at string beginning
extension="${f#*.}"  # = "ext"

# greedy deletion at string beginning
filename="${f##*/}"  # = "file.ext"
# deletion at string end
dirname="${f%/*}"    # = "path1/path2"

# greedy deletion at end
root="${f%%/*}"      # = "path1"
```


### Functions
* All locally created functions should be prefixed with an indicator that
  it was created by in house code, similar to variable naming
* Function names should be in lower case and use _ as a word separator
* Putting most of your execution logic into functions and then just calling
  them at the end of the script can greatly improve readability for others.
  Remember, [always code as if the guy who ends up maintaining your code will
  be a violent psychopath who knows where you live.](https://groups.google.com/forum/#!msg/comp.lang.c++/rYCO5yn4lXw/oITtSkZOtoUJ)
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

  For more information check out the
  [Advanced Bash Scripting Guide's](http://www.faqs.org/docs/abs/HTML/)
  section on [exit codes](http://www.faqs.org/docs/abs/HTML/exitcodes.html).

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
* Temporary files should be removed on successful completion of a function or
  exit of the script. Using trap to do this can make it much easier if you've
  put all your temporary files in a single directory.

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
