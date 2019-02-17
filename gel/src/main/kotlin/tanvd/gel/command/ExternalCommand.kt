package tanvd.gel.command

import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException

/**
 * Gel script command
 * Calls external process, ignores stdin
 * @param params -- values to pass to process
 */
class ExternalCommand(private val name: String, params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val process = try {
            ProcessBuilder(name, *params.toTypedArray()).start()
        } catch (e: IOException) {
            if (e.message?.contains("error=2") == true) {
                throw IllegalArgumentException("such external command does not exist")
            }
            throw e
        }
        process.waitFor()
        return process.inputStream.readBytes()
    }
}
