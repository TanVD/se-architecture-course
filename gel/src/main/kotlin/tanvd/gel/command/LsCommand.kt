package tanvd.gel.command

import tanvd.gel.Shell
import java.io.InputStream

/**
 * Gel script command
 * Print all files and directories in current directory
 */
class LsCommand() : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        return Shell.State.currentWorkingDirectory
                .listFiles()
                .map { file ->  file.relativeTo(Shell.State.currentWorkingDirectory)}
                .sorted()
                .joinToString(separator = "\n")
                .toByteArray()
    }
}
