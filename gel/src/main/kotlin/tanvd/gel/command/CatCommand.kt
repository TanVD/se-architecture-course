package tanvd.gel.command

import java.io.File
import java.io.InputStream

/**
 * Gel script command
 * Print content of file to stdout
 * @param params -- one value, path to file
 */
class CatCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        return File(params.first()).readBytes()
    }
}
