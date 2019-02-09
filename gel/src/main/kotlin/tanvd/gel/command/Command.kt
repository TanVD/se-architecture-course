package tanvd.gel.command

import java.io.InputStream

/**
 * Command of Gel interpreter
 * @param params -- params that command use
 */
abstract class Command(val params: List<String>) {
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
                "=" to { params -> AssignCommand(params) }
        )

        /** Create command if such exists, otherwise create ExternalCommand */
        fun create(name: String, params: List<String>): Command {
            return commands[name.toLowerCase()]?.invoke(params) ?: ExternalCommand(name, params)
        }
    }
}