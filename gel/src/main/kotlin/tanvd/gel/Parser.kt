package tanvd.gel

import tanvd.gel.command.Command
import tanvd.gel.command.CommandChain
import tanvd.gel.utils.Splitter

/** Parser object -- it parses line into Commands and pipe them */
object Parser {
    /** Parse line into command chain, expanding variables from context */
    fun parse(line: String): CommandChain {
        val commands = splitByPipes(GlobalContext.expand(line))
                .map { prepareCommand(it) }
                .map { (name, params) -> Command.create(name, params) }
        return CommandChain(commands)
    }

    /** Parse command into name and params */
    private fun prepareCommand(command: String): Pair<String, List<String>> {
        val values = Splitter.splitIntoParts(command, Regex("\\s")).map { it.part.trim().trim { char -> char == it.quote } }
                .filter { it.isNotBlank() }
        val name = values.first()
        val params = values.drop(1)
        return if (!name.contains("=")) {
            name to params
        } else {
            "=" to name.split("=").filter { it.isNotBlank() } + params
        }
    }

    /** Split line by pipes between commands with respect to quotes */
    private fun splitByPipes(line: String): List<String> {
        val parts = Splitter.splitIntoParts(line, Regex("\\|"))
        val commands = ArrayList<String>()

        var curLine = StringBuilder()
        for (part in parts) {
            if (part.part.startsWith("|")) {
                commands += curLine.toString()
                curLine = StringBuilder(part.part.drop(1))
            } else {
                curLine.append(part.part)
            }
        }

        if (curLine.isNotEmpty()) {
            commands += curLine.toString()
        }
        return commands
    }
}
