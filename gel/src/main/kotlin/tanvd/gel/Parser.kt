package tanvd.gel

import tanvd.gel.command.Command
import tanvd.gel.command.CommandChain

/** Parser object -- it parses line into Commands and pipe them */
object Parser {
    /** Parse line into command chain, expanding variables from context */
    fun parse(line: String): CommandChain {
        val commands = splitByPipes(GlobalContext.expand(line), listOf('"', '\''))
                .map { prepareCommand(it) }
                .map { (name, params) -> Command.create(name, params) }
        return CommandChain(commands)
    }

    /** Parse command into name and params */
    private fun prepareCommand(command: String): Pair<String, List<String>> {
        val values = command.split(Regex("\\s+")).map { it.trim('"', '\'') }
        val name = values.first()
        val params = values.drop(1)
        return if (!name.contains("=")) {
            name to params
        } else {
            "=" to name.split("=")
        }
    }

    /** Split line by pipes between commands with respect to quotes */
    private fun splitByPipes(line: String, quotes: List<Char>): List<String> {
        val commands = ArrayList<String>()

        var quote: Char? = null
        var curLine = ""
        for (curChar in line) {
            when {
                quote != null -> {
                    curLine += curChar
                    if (quote == curChar) {
                        quote = null
                    }
                }
                curChar in quotes -> {
                    quote = curChar
                    curLine += quote
                }
                curChar == '|' -> {
                    commands.add(curLine.trim())
                    curLine = ""
                }
                else -> {
                    curLine += curChar
                }
            }
        }
        commands.add(curLine.trim())

        return commands
    }
}