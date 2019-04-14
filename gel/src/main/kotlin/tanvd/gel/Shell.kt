package tanvd.gel

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream
import com.xenomachina.argparser.ShowHelpException
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

            val result = try {
                Parser.parse(line).run()
            } catch (e: IllegalArgumentException) {
                (e.message ?: "Illegal argument").toByteArray()
            } catch (e: ShowHelpException) {
                val array = ByteOutputStream()
                array.writer().use {
                    e.printUserMessage(it, null, 60)
                }
                array.bytes
            } catch (e: Exception) {
                "Unknown error occurred: ${e.message}".toByteArray()
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
