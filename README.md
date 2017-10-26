# Style Guides

This repository contains a set of style guides, coding conventions and
standards for the languages I care about.

## Purpose

I am sick of putting this stuff up for a vote every new team I create or
start up. It consumes less time if we just use a set of conventions and
standards that we are already content with then iterate on those for our team's
needs.

## Intent

Anyone is allowed to fork this repository and update the style guides
for their own purposes and teams use.

*A lot of this I don't really have strong opinions on (unless otherwise
mentioned) the key is consistency of a codebase's style and conventions
in a team setting with many people contributing. It reduces the maintenance
costs of codebases if there is a shared understanding of standards and
conventions that should be adhered to.*

These should be enforced during review. When tools are available code
conventions should be made _executable_ to avoid squabbling over ambiguities.

## Structure

There will be a `master` branch which will contain the coding styles,
conventions and standards that I personally prefer. The `develop` branch
may contain some tweaks I am playing with and haven't decided upon yet.

Other branches will be the living standard for the team, OSS project,
etc. that I am an active participant in that doesn't mind this being
public.

Each language will have the following structure:

 * languagename /
   * README.md (or README.asciidoc or README.adoc)
   * TUTORIAL.md (or TUTORIAL.asciidoc or TUTORIAL.adoc)
   * any file format for language tooling that provides executable checking of
     conventions that encode the standards.

## Philosophy

Why is there a section on philosophy in a style guideline repo? The benefit
of writing easy to manage code is universally obvious, right? While the
direct goal of easily manageable code can be seen as beneficial, the
indirect results, including improving the team dynamic is not immediately
obvious to everyone. That, and good code gives us the power to form
Voltron.

The enforcement of coding standards and styles can mostly be left to build
automation, thankfully! This leaves us with an amazing cognitive ability
that we shouldn't waste. The aspect of building and deploying services
requires an immense amount of technical knowledge and design fudamentals.
How do we learn these? How does an entire team get on the same page? How
do technical leads respectfully and thoughtfully instill a vision and
methodology into others in their team? How do you [actually poach an egg?] [1]

To successfully achieve this we must take into account several aspects of
systems management, software design and human nature. To see the product
of competent engineers successfully delivered to customers through awesome
operations is a beautiful thing. What does this have to do with style? If
the style of the code is homogenous, the actual problem being solved,
solution being presented or change being applied can be the focus of the
conversation. This shift in focus, while the result of minor changes in
workflow causes the entirety of the interactive discussion to be about
actual issues, and not syntax. The primary objective here being to add
intelligent human discussion to the problem being solved. While we achieve
this, the other benefits should not be ignored. A team that does code
reviews fosters an increased level of respect amongst the group. The
discussions create a shared belief in the vision of the application/company
and the team. This takes the power of leadership out of one person's hands
and puts it into the hands of everyone who cares. In addition to these,
doing good code reviews gives everyone a chance to improve, constantly. Any
engineer who thinks they are at the top of their game is ignorant of their
surroundings and should not be trusted. Everyone has room to improve, and
respectful, stylistically correct, code reviews are an amazing way to do
exactly that.

[1]: https://www.youtube.com/watch?v=UMiCy8EH1goUsing "Poaching eggs"


## License

This is licensed under the 3-clause BSD license. See LICENSE file for
more details.


