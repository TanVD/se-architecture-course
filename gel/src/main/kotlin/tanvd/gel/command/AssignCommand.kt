package tanvd.gel.command

import tanvd.gel.GlobalContext
import java.io.InputStream


/**
 * Gel script command
 * Add variable to global variable context or update it
 * @param params -- two values, first name of variable and second is value
 */
class AssignCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val (name, value) = params
        GlobalContext[name] = value
        return ByteArray(0)
    }
}