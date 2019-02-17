package tanvd.gel.utils


object Splitter {

    data class Part(val part: String, val quote: Char?)

    /**
     * Splits line into parts -- plain and quoted with one of the passed quote.
     * Inside of quoted part all other quotes are ignored
     */
    fun splitIntoParts(line: String, quotes: List<Char> = listOf('\'', '"')): List<Part> {
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
}

