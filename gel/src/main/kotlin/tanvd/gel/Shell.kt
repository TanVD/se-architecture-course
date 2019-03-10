package tanvd.gel

import org.jetbrains.annotations.TestOnly
import tanvd.gel.utils.Splitter
import java.io.File
import java.nio.file.Paths

object Shell {
    object State {
        var shouldExit: Boolean = false

        var currentWorkingDirectory: File = Paths.get(".").toAbsolutePath().normalize().toFile()

        @TestOnly
        fun clear() {
            shouldExit = false
        }
    }

    object Configuration {
        const val prompt: String = "~> "
        const val supportMultiLine: Boolean = true
    }

    object FileGetter {
        fun getFileByPath(path: String?) : File {
            if (path == null) {
                return State.currentWorkingDirectory
            }
            return State.currentWorkingDirectory.resolve(path)
        }
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
            } catch (e: Exception) {
                "Unknown error ocurred: ${e.message}".toByteArray()
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
