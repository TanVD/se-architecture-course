package tanvd.gel.command

import tanvd.gel.GlobalContext
import java.io.InputStream


class AssignCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val (name, value) = params
        GlobalContext[name] = value
        return ByteArray(0)
    }
}