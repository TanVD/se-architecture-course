package tanvd.gel.command

import com.xenomachina.argparser.default
import java.io.File
import java.io.InputStream

/**
 * Gel script command
 * Implements grep -- regex searcher for lines in file or pipe
 */
class GrepCommand(params: List<String>) : Command(params) {
    private val isInsensitive by argParser.flagging("-i", help = "enable case-insensitive mode")

    private val isWordFully by argParser.flagging("-w", help = "enable word full mode")

    private val afterLines by argParser.storing("-A", help = "after lines", transform = {
        val value = trim().toIntOrNull()
        require(value != null) { "-A argument got not int" }
        value!!
    }).default(0)

    private val pattern by argParser.positional("PATTERN", "pattern to found")
    private val file: String? by argParser.positional("FILE", "file to found").default<String?>(null)

    private val regex = kotlin.run {
        var result = ""
        if (!pattern.contains("(?i)") && isInsensitive) {
            result += "(?i)"
        }
        if (isWordFully) {
            result += "\\b"
        }
        result += pattern
        if (isWordFully) {
            result += "\\b"
        }
        Regex(result)
    }

    override fun execute(inputStream: InputStream) = if (file != null) {
        File(file).useLines { processStream(it.toList()) }
    } else {
        processStream(inputStream.reader().readLines())
    }

    private fun processStream(strings: List<String>) = buildString {
        var wasFirst = false
        var printLines = afterLines
        var seenMatch = false
        for (line in strings) {
            if (regex.containsMatchIn(line)) {
                seenMatch = true
                printLines = afterLines
            }
            if (seenMatch && printLines >= 0) {
                if (wasFirst) append("\n")
                append(line)
                printLines--
                wasFirst = true
            }
            if (seenMatch && printLines < 0) {
                printLines = afterLines
                seenMatch = false
            }
        }
    }.toByteArray()
}
