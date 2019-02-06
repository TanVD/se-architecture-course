package tanvd.gel.command

import java.io.File
import java.io.InputStream

class WcCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val input = if (params.none { it.isNotBlank() }) inputStream else File(params.first()).inputStream()
        var lines = 0
        var words = 0
        var bytes = 0
        input.reader().forEachLine {
            lines++
            words += it.split(Regex("\\s+")).size
            bytes += it.toByteArray().size
        }
        return "$lines $words $bytes".toByteArray()
    }
}