package tanvd.gel.command

import java.io.InputStream

/**
 * Gel script command
 * Calls external process, ignores stdin
 * @param params -- values to pass to process
 */
class ExternalCommand(private val name: String, params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        val process = ProcessBuilder(name, *params.toTypedArray()).start()
        process.waitFor()
        return process.inputStream.readBytes()
    }

    //equals and hashcode
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ExternalCommand

        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }
}