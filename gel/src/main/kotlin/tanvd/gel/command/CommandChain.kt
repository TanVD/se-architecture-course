package tanvd.gel.command

import java.io.InputStream

class CommandChain(private val commands: List<Command>) : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        var current = inputStream
        for (command in commands) {
            current = command.execute(current).inputStream()
        }
        return current.readBytes()
    }
}