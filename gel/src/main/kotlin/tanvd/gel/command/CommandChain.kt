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

    //equals and hashcode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as CommandChain

        if (commands != other.commands) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + commands.hashCode()
        return result
    }
}