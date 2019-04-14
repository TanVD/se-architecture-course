package tanvd.gel.utils


object Splitter {
    private val defaultQuotes = listOf('\'', '"')

    data class Part(val part: String, val quote: Char?) {
        /** Part with trimmed whitspaces and removed quotes if any. */
        val trimmed: String
            get() = part.trim().trim { it == quote }
    }

    /**
     * Splits line into parts -- plain and quoted with one of the passed quote.
     * Inside of quoted part all other quotes are ignored
     *
     * @param alsoSplit -- Regex. If char matches it is considered splitting. Splitting char splits non quoted parts
     * Char itself added to the second line.
     */
    fun splitIntoParts(line: String, alsoSplit: Regex? = null, quotes: List<Char> = defaultQuotes): List<Part> {
        val parts = ArrayList<Part>()
        var quote: Char? = null
        val curLine = StringBuilder()
        for (curChar in line) {
            when {
                quote != null -> {
                    curLine.append(curChar)
                    if (quote == curChar) {
                        parts += Part(curLine.toString(), quote)
                        quote = null
                        curLine.clear()
                    }
                }
                curChar in quotes -> {
                    parts += Part(curLine.toString(), null)
                    curLine.clear()

                    quote = curChar
                    curLine.append(quote)
                }
                alsoSplit?.matches(curChar.toString()) ?: false -> {
                    parts += Part(curLine.toString(), null)
                    curLine.clear()
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

    fun lastQuoteClosed(line: String, quotes: List<Char> = defaultQuotes): Boolean {
        val lastPart = splitIntoParts(line, null, quotes).lastOrNull() ?: return true
        val lastNotStartsWithQuote = lastPart.part.firstOrNull() !in quotes
        val lastPartQuotesClosed = lastPart.part.first() in quotes
                && lastPart.part.length > 1 && lastPart.part.first() == lastPart.part.last()
        return lastNotStartsWithQuote || lastPartQuotesClosed
    }
}

