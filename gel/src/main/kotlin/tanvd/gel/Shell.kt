package tanvd.gel

import org.jetbrains.annotations.TestOnly
import tanvd.gel.utils.Splitter

object Shell {
    object State {
        var shouldExit: Boolean = false

        @TestOnly
        fun clear() {
            shouldExit = false
        }
    }

    object Configuration {
        const val prompt: String = "~> "
        const val supportMultiLine: Boolean = true
    }

    fun run() {
        print(Configuration.prompt)
        var line = readLine()
        while (line != null) {
            while (Configuration.supportMultiLine && !Splitter.lastQuoteClosed(line)) {
                line += '\n'
                line += readLine()
            }

            val chain = Parser.parse(line)
            val result = try {
                chain.run()
            } catch (e: IllegalArgumentException) {
                (e.message ?: "Illegal argument").toByteArray()
            }

            if (State.shouldExit) {
                println("Bye")
                return
            }

            if (result.isNotEmpty()) {
                println(String(result))
            }

            print(Configuration.prompt)
            line = readLine()
        }
    }
}
