# Git

Git commit messages can be very handy for context and problem resolution.
However this is only true if they are usable by the many git tools that
parse and display history.

## Subject Line

Git commit messages should start with a short (50 character) subject line
followed by a new-line. The subject line should convey the intent of the
commit and possibly be preceded by an issue or ticket number if applicable.

## Commit Message Body

The git commit message body should contain enough information to provide the
reader with context about the intent of the commit. Any significant details
that may help track down problems or provide information to the code reviewer
should be included here as well. These should line-wrap at 72 characters. They
should only include ASCII characters. ASCII art is OK, but only if it is
awesome and it makes the other readers smile.
