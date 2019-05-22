package tanvd.gel.command

import com.xenomachina.argparser.default
import tanvd.gel.Shell
import java.io.File
import java.io.InputStream

/**
 * Gel script command
 * Implements grep -- regex searcher for lines in file or pipe
 */
class GrepCommand(params: List<String>) : Command(params) {
    private val isInsensitive by argParser.flagging("-i", help = "enable case insensitive mode")

    private val isWordFully by argParser.flagging("-w", help = "enable word full mode")

    private val afterLines by argParser.storing("-A", help = "after lines").default("0")
    private val afterLinesValue
        get() = afterLines.trim().toInt()

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

    override fun execute(inputStream: InputStream): ByteArray {
        val input = if (file == null) inputStream else {
            val file = Shell.FileGetter.getFileByPath(file)
            require(file.exists()) { "file passed to grep command does not exist" }
            file.inputStream()
        }

        var wasFirst = false
        return buildString {
            input.reader().useLines {
                var printLines = afterLinesValue
                var seenMatch = false
                for (line in it) {
                    if (regex.containsMatchIn(line)) {
                        seenMatch = true
                        printLines = afterLinesValue
                    }
                    if (seenMatch && printLines >= 0) {
                        if (wasFirst) append("\n")
                        append(line)
                        printLines--
                        wasFirst = true
                    }
                    if (seenMatch && printLines < 0) {
                        printLines = afterLinesValue
                        seenMatch = false
                    }
                }
            }
        }.toByteArray()
    }
}
