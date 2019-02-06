package tanvd.gel.command

import java.io.InputStream

abstract class Command(val params: List<String>) {
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

        fun create(name: String, params: List<String>): Command {
            return commands[name.toLowerCase()]?.invoke(params) ?: ExternalCommand(name, params)
        }
    }
}