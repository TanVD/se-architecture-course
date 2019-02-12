package tanvd.gel.command

import java.io.File
import java.io.InputStream

/**
 * Gel script command
 * Print content of file to stdout
 * @param params -- one value, path to file
 */
class CatCommand(params: List<String>) : Command(params) {
    private val filename by argParser.positional("FILE", "path to filename to cat")

    override fun execute(inputStream: InputStream): ByteArray {
        require(params.size == 1) { "cat command takes exactly one param" }

        val file = File(filename)
        require(file.exists()) { "file passed to cat command does not exist" }

        return file.readBytes()
    }
}
