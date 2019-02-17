package tanvd.gel

import org.jetbrains.annotations.TestOnly
import java.lang.StringBuilder

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
        return buildString {
            var quote: Char? = null
            var curLine = StringBuilder()
            for (curChar in line) {
                when {
                    quote != null -> {
                        curLine.append(curChar)
                        if (quote == curChar) {
                            when (quote) {
                                '"' -> append(expandAll(curLine.toString()))
                                '\'' -> append(curLine)
                            }
                            quote = null
                            curLine = StringBuilder()
                        }
                    }
                    curChar in listOf('"', '\'') -> {
                        append(expandAll(curLine.toString()))
                        curLine = StringBuilder()

                        quote = curChar
                        curLine.append(quote)
                    }
                    else -> {
                        curLine.append(curChar)
                    }
                }
            }
            append(expandAll(curLine.toString()))
        }
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
