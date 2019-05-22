# Main objects
* Parser -- here is located main logic of parsing line 
into chain of commands. Parser uses GlobalContext to
expand variables.
* GlobalContext -- static storage of declared variables. Also it
encapsulates logic of variables expansion (with respect to
quotes)
* Command -- abstract class for all commands. It is an abstraction
of shell command with params. Also it's companion object encapsulates 
logic of creation command from it's name and params
* Main -- main event loop.

# Standard workflow
* Main event loop reads new line and calls `Parser.parse`
* `Parser.parse` calls `GlobalContext.expand` on line.
    * `GlobalContext.expand` expands variables in line except fully quoted
* `Parser.parse` calls `Parser.splitByPipes` with expanded line
    * `Parser.splitByPipes` splits line into piped commands (still strings)
* `Parser.parse` calls `Parser.prepareCommand` for each command string
    * `Parser.prepareCommand` normalize commands (converts infix to prefix)
      and splits them into name and params
* `Parser.parse` calls `Command.create` for each prepared command 
    * `Command.create` creates corresponding `Command` object if such exists,
      otherwise it creates `ExternalCommand`
* `Parser.parse` packs all the commands in `CommandChain` and returns them
* Main event loop calls `CommandChain.execute` with chain it got 
  and passes to it `System.in`
* Main event loop prints `ByteArray` got from chain and reads new line

# Args library

I have taken a look at three Kotlin libraries for commandline argument parsing:
* kotlinx.cli -- it is a proof of concept library implemented by Kotlin evangelist. It shows how to implement delegates-driven type safe
 arguments , but it is not a mature library. Also it is not even published to maven and marked as JB Incubator
* Clikt -- this is the other hand of maturity and feature-richness. Clikt is a big and popular library capable of type-safe commandline
arguments parsing. The problem is that Clikt has own abstraction of Command pattern (CliktCommand) which is not very flexible and there is
no convenient API to work with arguments parsing not implementing CliktCommand. Gel has own implementation of command which uses Input and
Output streams and it's API is not compatible with Clikt.
* Argparser -- this is an evolution of kotlinx.cli. But, still, this library just parses arguments and not pushes user to use approaches
of library author for the whole project (like Clikt does), so I've chosen it
