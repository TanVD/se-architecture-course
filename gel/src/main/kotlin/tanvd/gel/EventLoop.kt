package tanvd.gel

import tanvd.gel.command.ExitException

fun main() {
    while (true) {
        val chain = Parser.parse(readLine()!!)
        val result = try {
            chain.execute(System.`in`)
        } catch (e: ExitException) {
            return
        }
        println(String(result))
    }
}