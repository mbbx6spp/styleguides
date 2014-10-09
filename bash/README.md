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

## Appropriateness

Here I will shed light on when I think it is appropriate to use Bash for
a task instead of the plethora of other shell, scripting, or compiled
langauges available to us every day.

Appropriate uses for a Bash script are:

* glueing together various shell commands and handling errors appropriately
* anything that should not require any special runtimes, interpreters or
  langauges installed on the target run hosts.
* any script that does not require using a data structure more complex than
  an array. (Yes, Bash 4 has support for associative arrays now, but most
  of our target systems are still Bash 3.)

## Conventions

### Errors

* All error messages should go to `STDERR`.
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
* You *should* also create a temporary directory using `mktemp` that the
  temporary file will reside in and change the permissions of the temporary
  directory before creating the file AND still use `umask` before creating
  the file containing sensitive data. You can do this using the `-d` option
  to `mktemp` to create a temporary directory.
* You *must* never use `eval` or I will slap you with a wet tuna until you
  beg for forgiveness.
* SUID and SGID *must* never be used on shell scripts.


### Naming

#### Files

* Executables should have no extension (preferred) or a .sh extension.
* Libraries *must* have a .sh, .env, .functions extension and *should* not be
  executable.

### Structure

#### Indentation

* Use 2 soft spaces. No tabs.

#### Line Length

* You should keep lines of Bash scripts 80 characters or less whenever
  possible.

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


TODO


