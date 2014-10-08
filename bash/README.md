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

TODO
