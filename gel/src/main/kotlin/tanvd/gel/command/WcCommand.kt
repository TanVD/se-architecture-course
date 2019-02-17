package tanvd.gel.command

import java.io.File
import java.io.InputStream

/**
 * Gel script command
 * Print to stdout number of lines, words and bytes in input (file or stdin)
 * @param params -- if one value, than use as path of input file, otherwise input is stdin
 */
class WcCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        require(params.size <= 1) { "wc command takes not more than one param"}

        val input = if (params.isEmpty()) inputStream else {
            val file = File(params.first())
            require(file.exists()) { "file passed to wc command does not exist" }
            file.inputStream()
        }
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
