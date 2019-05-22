package tanvd.gel.command

import tanvd.gel.GlobalContext
import java.io.InputStream


/**
 * Gel script command
 * Add variable to global variable context or update it
 * @param params -- two values, first name of variable and second is value
 */
class AssignCommand(params: List<String>) : Command(params) {
    private val name by argParser.positional("NAME", "name of variable to assign")
    private val value by argParser.positional("VALUE", "value to assign to variable")

    override fun execute(inputStream: InputStream): ByteArray {
        require(params.size == 2) { "Malformed assignment operator" }
        GlobalContext[name] = value
        return ByteArray(0)
    }
}
