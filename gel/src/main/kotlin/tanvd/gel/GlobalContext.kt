package tanvd.gel

import org.jetbrains.annotations.TestOnly

/** Static storage of declared variables in this interpreter session */
object GlobalContext {
    private val bashRegex = Regex("(?:(\\\$[\\w\\d]+))|(?:(\\\$\\{[\\w\\d]+}))")

    private val variables = HashMap<String, String>()

    /** Set or update variable in context */
    operator fun set(name: String, value: String) {
        variables[name] = value
    }

    /** Get variable from context */
    operator fun get(name: String) = variables[name]

    /**
     * Expand variables existing in context in line with respect to unary quotes
     * @param line -- string to expand
     * @return string with all variables existing in context expanded, except those in unary quotes
     */
    fun expand(line: String): String {
        var result = ""
        var quote: Char? = null
        var curLine = ""
        for (curChar in line) {
            when {
                quote != null -> {
                    curLine += curChar
                    if (quote == curChar) {
                        when (quote) {
                            '"' -> result += expandAll(curLine)
                            '\'' -> result += curLine
                        }
                        quote = null
                        curLine = ""
                    }
                }
                curChar in listOf('"', '\'') -> {
                    result += expandAll(curLine)
                    curLine = ""

                    quote = curChar
                    curLine += quote
                }
                else -> {
                    curLine += curChar
                }
            }
        }
        result += expandAll(curLine)
        return result
    }

    /** Expand all variables, do not check if it is in unary quotes */
    private fun expandAll(line: String): String {
        var rendered = line
        bashRegex.findAll(line).forEach { match ->
            val matchValue = match.value
            val variable = if (matchValue.startsWith("\${")) matchValue.drop(2).dropLast(1) else matchValue.drop(1)
            get(variable)?.let {
                rendered = rendered.replace(matchValue, it)
            }
        }
        return rendered
    }

    @TestOnly
    fun clear() {
        variables.clear()
    }
}
