package tanvd.gel.command

import java.io.InputStream

/**
 * Gel script command
 * Calls external process, ignores stdin
 * @param params -- values to pass to process
 */
class ExternalCommand(private val name: String, params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val process = ProcessBuilder(name, *params.toTypedArray()).start()
        process.waitFor()
        return process.inputStream.readBytes()
    }
}