package tanvd.gel

import org.jetbrains.annotations.TestOnly

object GlobalContext {
    private val bashRegex = Regex("(?:(\\\$[\\w\\d]+))|(?:(\\\$\\{[\\w\\d]+}))")

    private val variables = HashMap<String, String>()

    operator fun set(name: String, value: String) {
        variables[name] = value
    }

    operator fun get(name: String) = variables[name]

    fun expand(line: String): String {
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