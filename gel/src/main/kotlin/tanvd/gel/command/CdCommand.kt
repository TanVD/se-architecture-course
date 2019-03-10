package tanvd.gel.command

import tanvd.gel.Shell
import java.io.InputStream

/**
 * Gel script command
 * Change working directory by given path
 */
class CdCommand(params: List<String>) : Command(params) {
    private val path by argParser.positional("PATH", "new path")

    override fun execute(inputStream: InputStream): ByteArray {
        require(params.size == 1) { "cd command takes exactly one param" }
        require(Shell.State.currentWorkingDirectory.resolve(path).isDirectory) { "not a directory" }
        Shell.State.currentWorkingDirectory = Shell.State.currentWorkingDirectory.resolve(path).normalize()
        return ByteArray(0)
    }
}
