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
        }
        println(String(result))

        print("~> ")
        line = readLine()
    }
}