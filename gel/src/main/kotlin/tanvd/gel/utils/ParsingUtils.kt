package tanvd.gel.utils


object Splitter {
    private val defaultQuotes = listOf('\'', '"')

    data class Part(val part: String, val quote: Char?)

    /**
     * Splits line into parts -- plain and quoted with one of the passed quote.
     * Inside of quoted part all other quotes are ignored
     *
     * @param alsoSplit -- split non quoted parts if symbol is not null. Values by which was line splitted will
     * be added to the start of next part
     */
    fun splitIntoParts(line: String, alsoSplit: Regex? = null, quotes: List<Char> = defaultQuotes): List<Part> {
        val parts = ArrayList<Part>()
        var quote: Char? = null
        var curLine = StringBuilder()
        for (curChar in line) {
            when {
                quote != null -> {
                    curLine.append(curChar)
                    if (quote == curChar) {
                        parts += Part(curLine.toString(), quote)
                        quote = null
                        curLine = StringBuilder()
                    }
                }
                curChar in quotes -> {
                    parts += Part(curLine.toString(), null)
                    curLine = StringBuilder()

                    quote = curChar
                    curLine.append(quote)
                }
                alsoSplit?.matches(curChar.toString()) ?: false -> {
                    parts += Part(curLine.toString(), null)
                    curLine = StringBuilder()
                    curLine.append(curChar)
                }
                else -> {
                    curLine.append(curChar)
                }
            }
        }
        if (curLine.isNotEmpty()) {
            parts += Part(curLine.toString(), null)
        }
        return parts
    }

    fun lastQuoteClosed(line: String, quotes : List<Char> = defaultQuotes): Boolean {
        val lastPart =  splitIntoParts(line, null, quotes).lastOrNull()
        return lastPart == null || lastPart.part.firstOrNull() !in quotes || (lastPart.part.first() in quotes
                && lastPart.part.length > 1 && lastPart.part.first() == lastPart.part.last())
    }
}

