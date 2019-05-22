package tanvd.gel.command

import tanvd.gel.Shell
import java.io.InputStream

/**
 * Gel script command
 * Print working directory path to stdout
 */
class PwdCommand : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        return Shell.State.currentWorkingDirectory.toString().toByteArray()
    }
}
