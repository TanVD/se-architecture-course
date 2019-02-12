package tanvd.gel.command

import java.io.InputStream
import java.nio.charset.Charset

/**
 * Gel script command
 * Print params to stdout
 * @param params -- params to print to stdout
 */
class EchoCommand(params: List<String>) : Command(params) {
    private val inputParams by argParser.positionalList("PARAMS", "params to output")

    override fun execute(inputStream: InputStream): ByteArray {
        return inputParams.joinToString(separator = " ").toByteArray(Charset.defaultCharset())
    }
}
