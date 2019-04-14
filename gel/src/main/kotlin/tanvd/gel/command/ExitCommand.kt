package tanvd.gel.command

import tanvd.gel.Shell
import java.io.InputStream

/**
 * Gel script command
 * Exit Gel interpreter
 */
class ExitCommand : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        Shell.State.shouldExit = true
        return ByteArray(0)
    }
}
