package tanvd.gel.command

import java.io.InputStream

/**
 * Chain of commands implementing "pipe"
 * Command use as input output of previous command (or stdin, if first)
 * Command outputs to the next command (or stdout, if last)
 */
class CommandChain(private val commands: List<Command>) : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        var current = inputStream
        for (command in commands) {
            current = command.execute(current).inputStream()
        }
        return current.readBytes()
    }
}