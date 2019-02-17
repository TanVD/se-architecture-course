package tanvd.gel

import tanvd.gel.command.ExitException

fun main() {
    print("~> ")
    var line = readLine()
    while (line != null) {
        val chain = Parser.parse(line)
        val result = try {
            chain.execute(System.`in`)
        } catch (e: ExitException) {
            return
        } catch (e: IllegalArgumentException) {
            (e.message ?: "Illegal argument").toByteArray()
        }
        if (result.isNotEmpty()) {
            println(String(result))
        }

        print("~> ")
        line = readLine()
    }
}
