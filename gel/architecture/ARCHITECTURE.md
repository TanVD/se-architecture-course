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