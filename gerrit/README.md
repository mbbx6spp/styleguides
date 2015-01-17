# Gerrit Conventions

This document will define what conventions to use for consistency across
projects in Gerrit.

## Users, Groups & Permissions

* Always use groups in project permissions. Never specific users.
* Create as many logical user groups as seem natural for your permission
  needs. You could create too many, but it's unlikely and simpler to clean
  up later than polluting permissions with one-off user specific settings.
* Define permissions at the parent project as much as possible for inheritance

## Branches

There are two types of branches:
* persistent branches
* feature, bugfix, or integration branches which should all be temporary and
  short lived

New projects should all embrace CI/CD from the beginning and as such should
only have one persistent branch.


## Project Settings

* For each group of projects that are owned by the same team, create a
  base parent directory
* Require ChangeId in commit messages for each project
* Use Fast-Forward Only submit type
* Always start a project with an empty commit
* Set `master` as the initial branches of the project

```bash
# TODO set variables
declare -r group="..."
declare -r parent="..."
declare -r description="..."
declare -r name="..."

gerrit create-project \
  --owner "${group}" \
  --parent "${parent}" \
  --description "${description}" \
  --submit-type FAST_FORWARD_ONLY \
  --require-change-id \
  --empty-commit \
  "${name}"
```

## Naming

* Parent projects should not contain hierarchy suggesting characters such as '/'
* Non-parent projects should be prefixed by the name of their parent and the '/'
  character acting as a logical naming divider.


