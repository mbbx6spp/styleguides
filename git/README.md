# Git

Git commit messages can be very handy for context and problem resolution.
However this is only true if they are usable by the many git tools that
parse and display history.

## Git Commit Messages

### Inspiration

The two primary inspirations for the 'Git Logs' conventions described here
are:

* [Tim Pope's Git Commit Messages post](http://tbaggery.com/2008/04/19/a-note-about-git-commit-messages.html)
* More than eight years of using git as a DVCS on teams of varing abilities
  and writing scripts to automate changelog and risk assessment of releases.

### Subject Line

Git commit messages should start with a short (50 character) subject line
followed by a new-line. The subject line should convey the intent of the
commit and possibly be preceded by an issue or ticket number if applicable.

### Commit Message Body

The git commit message body should contain enough information to provide the
reader with context about the intent of the commit. Any significant details
that may help track down problems or provide information to the code reviewer
should be included here as well. These should line-wrap at 72 characters. They
should only include ASCII characters. ASCII art is OK, but only if it is
awesome and it makes the other readers smile.

### Issue Linking

There are two recommended styles of linking an issue/ticket with your commit:

* The first sequence of characters is the unique issue id that can be parsed
  easily and integration automated with the issue tracker being used. For
  example:
    ABC-12345 Update README.asciidoc 'Workflow' section

    This change was necessary to document the changes to project workflow.

* Adding three fields at the bottom of your Git commit messages in a consistent
  fashion:

      Issue-Id: ABC-12345
      Issue-Tracker-Type: Jira
      Issue-Tracker-URL: https://internaljira.yourdomain.com

  This is useful when you might have two or more issue trackers. One for
  internal issue management and one external and visible to partners or
  customers. The type of issue tracker may even change.

  Obviously you should provide simple automation for contributors to your
  codebase to avoid/decrease human error as adding these fields in a
  consistent way would be error prone manually. Also making sensible defaults
  would be important and running checks on the Git commit messages on
  submission of your review/patch/pull request (in whatever review system you
  use).

### Examples

Bad example:

```
Refactored all network code to use Netty 4.x
```

The above would be a bad git commit log because it does not have a body
outlining the major types of changes (and why they were made) along with
potential implications of such a likely massive change. You should not accept
such a git commit log during review.

Good example (small scope):

```
ABC-12345 Update README.md Contribution section
```

Assuming the above describes the changes fully, this is a very acceptable git
commit message. No need to for many small changes to have a commit message
body after the empty line.

Ok example (larger scope):

```
Refactor network components to use Netty 4.x APIs

The following components have been migrated from Netty 3.x to 4.x:

* Cluster state communication and acknowledge
* Request delegation to node owning shard
* ...

The implication of this is that the test suite had to be radically refactored
too.
```

While this is a good example there is no reference to an issue to link a
task/feature/bug issue with the change itself. While it is not always
possible to only have one Git commit per repository per issue, it is
important to be able to cross reference them and automate that cross
referencing. For everyone's sanity.

A better/good example would be:

```
Refactor network components to use Netty 4.x APIs

The following components have been migrated from Netty 3.x to 4.x:

* Cluster state communication and acknowledge
* Request delegation to node owning shard
* ...

Implications: test suite had to be radically refactored too.
Risk: High
Issue-Id: ABC-12345
```

_Note: we have a default issue tracker in the above case so we don't need to
specify `Issue-Tracker-URL` or `Issue-Tracker-Type`._

We are also using other name-value tags so we can do reporting on this
from the git commit messages if we want, although a lot of this should
probably be deferred to the issue tracker being used with just a link
from the git commit message to the issue(s). Issue dependency management
should be done from the issue tracker if possible so you can keep one
issue id per commit for integration sanity.
