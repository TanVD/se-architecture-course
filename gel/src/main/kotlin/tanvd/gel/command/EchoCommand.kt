package tanvd.gel.command

import java.io.InputStream
import java.nio.charset.Charset

class EchoCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        return params.joinToString(separator = " ").toByteArray(Charset.defaultCharset())
    }
}