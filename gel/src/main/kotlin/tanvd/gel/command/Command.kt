package tanvd.gel.command

import com.xenomachina.argparser.ArgParser
import java.io.InputStream

/**
 * Command of Gel interpreter
 * @param params -- params that command use
 */
abstract class Command(protected val params: List<String>) {
    protected val argParser = ArgParser(params.toTypedArray())

    /**
     * Execute command with it's params
     * @param inputStream -- stdin of command, may be ignored by command
     * @return ByteArray which is stdout of command
     */
    abstract fun execute(inputStream: InputStream): ByteArray

    companion object {
        private val commands: Map<String, (List<String>) -> Command> = mapOf(
                "exit" to { _ -> ExitCommand() },
                "echo" to { params -> EchoCommand(params) },
                "cat" to { params -> CatCommand(params) },
                "wc" to { params -> WcCommand(params) },
                "pwd" to { _ -> PwdCommand() },
                "grep" to { params -> GrepCommand(params) },
                "=" to { params -> AssignCommand(params) },
                "cd" to { params -> CdCommand(params) },
                "ls" to { params -> LsCommand(params) }
        )

        /** Create command if such exists, otherwise create ExternalCommand */
        fun create(name: String, params: List<String>) = commands[name.toLowerCase()]?.invoke(params) ?: ExternalCommand(name, params)
    }

    //equals and hashcode
    //used it tests
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Command

        if (params != other.params) return false

        return true
    }

    override fun hashCode() = params.hashCode()
}
