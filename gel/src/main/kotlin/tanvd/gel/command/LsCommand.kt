package tanvd.gel.command

import com.xenomachina.argparser.default
import tanvd.gel.Shell
import java.io.InputStream

/**
 * Gel script command
 * Print all files and directories in current directory
 */
class LsCommand(params: List<String>) : Command(params) {
    private val path: String by argParser.positional("PATH", "new path").default<String>(".")

    override fun execute(inputStream: InputStream): ByteArray {
        require(params.size <= 1) { "cd command takes no more than one param" }
        require(Shell.State.currentWorkingDirectory.resolve(path).isDirectory) { "not a directory" }
        return Shell.State.currentWorkingDirectory
                .resolve(path)
                .listFiles()
                .map { file ->  file.relativeTo(Shell.State.currentWorkingDirectory.resolve(path))}
                .sorted()
                .joinToString(separator = "\n")
                .toByteArray()
    }
}
