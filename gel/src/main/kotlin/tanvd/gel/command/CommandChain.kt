package tanvd.gel.command

/**
 * Chain of commands implementing "pipe"
 * Command use as input output of previous command (or stdin, if first)
 * Command outputs to the next command (or stdout, if last)
 */
data class CommandChain(private val commands: List<Command>) {
    fun run(): ByteArray {
        var current = System.`in`
        for (command in commands) {
            current = command.execute(current).inputStream()
        }
        return current.readBytes()
    }
}
