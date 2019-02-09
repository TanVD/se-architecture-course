package tanvd.gel.command

import java.io.InputStream

class ExitException : Exception()

/**
 * Gel script command
 * Exit Gel interpreter
 */
class ExitCommand : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        throw ExitException()
    }
}
