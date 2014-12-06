# Gerrit Workflow

Do not use the `git flow` workflow. More will be coming to explain why I
think why in the `cicd` styleguide soon. :)

The git flow workflow serves a purpose for teams where there is a variable
level of git knowledge and understanding. These are not the kinds of teams
I wish to cater for here.

## Changes

* There should be only one way to push a change out to all environments.
* It should be automated and hooked into Gerrit event triggers.
* Changes may impact the following:
  * code
  * tests
  * CI/CD scripts specific to the project
  * documentation

## Review Flow

1. Ensure up to date with Gerrit remote:

    git fetch gerrit

2. To create ticket branch:

    git checkout -b tickets/ABC-1234 gerrit/master

3. Make your changes as needed and validate changes locally or in dev env.

4. Add and/or remove files in the Git index as needed to stage your changes.

5. Commit (locally) your change:

    git commit -m "ABC-1234 Fix ...." # I will write more on git log messages

6. Create Gerrit review:

    git review --topic ABC-1234 master

## Deploy Flow

TODO: ASCII DIAGRAMS HERE SOON


## Git Configuration

There is one git configuration option that may cause you to be creating merge
commits without you realizing and others that simply make merges more likely.
Pay special attention to the first configuration in this list although the
second one will save your sanity more times than you can imagine:

* `merge.ff`
  By default, git does not create an extra merge commit when merging a commit
  that is a descendant of the current commit. Instead, the tip of the current
  branch is fast-forwarded. When set to false, this variable tells git to
  create an extra merge commit in such a case (equivalent to giving the
  `--no-ff` option from the command line). When set to only, only such
  fast-forward merges are allowed (equivalent to giving the --ff-only option
  from the command line).
  Just remove this option completely the default should be good (for each of
  your repositories): `git config --unset merge.ff`
* `branch.autosetuprebase=always`
  Always just set it up with this setting since the new branches you create in
  your local repository will almost always be non-persistent branches. Note
  that when you access the repo's persistent branches inside your local
  repository they will not be the source of truth anyway. The Gerrit remote's
  version of persistent branches like `master` should be the source of truth.
  To set this setting globally across all Git based repositories you can use
  this command:
  `git config --global --add branch.autosetuprebase always`

I also recommend the following for more informative output from rebase:

* `rerere.enabled=1`
  This activates recording of resolved conflicts, so that identical conflict
  hunks can be resolved automatically. Helpful for long running refactoring
  branches (which hopefully will be once in a blue moon, but very helpful in
  that case anyway): `git config --global --int rerere.enabled 1`

* `rebase.stat=true`
  Shows a diffstat of what changed upstream since the last rebase:
  `git config --global --bool rebase.stat true`

## And Finally...

Now that we are done with the serious stuff, here is a silly tiny script that
I wrote before leaving Desk.com to amuse myself:
[Script to find profanity in your git logs](https://gist.github.com/mbbx6spp/c3725ada401043d1db59)

Run it on your favorite git/gerrit repositories to find out
