package tanvd.gel

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tanvd.gel.command.*

class ParserTest {
    @Test
    fun parse_simpleExistingCommand_gotCommand() {
        val chain = Parser.parse("echo 1 2 3")
        Assertions.assertEquals(CommandChain(listOf(EchoCommand(listOf("1", "2", "3")))), chain)
    }

    @Test
    fun parse_simpleNotExistingCommand_gotCommand() {
        val chain = Parser.parse("grep 1 2 3")
        Assertions.assertEquals(CommandChain(listOf(ExternalCommand("grep", listOf("1", "2", "3")))), chain)
    }

    @Test
    fun parse_assignCommandSimple_gotCommand() {
        val chain = Parser.parse("abcd=123")
        Assertions.assertEquals(CommandChain(listOf(AssignCommand(listOf("abcd", "123")))), chain)
    }

    @Test
    fun parse_assignCommandWithQuotes_gotCommand() {
        val chain = Parser.parse("abcd='123'")
        Assertions.assertEquals(CommandChain(listOf(AssignCommand(listOf("abcd", "123")))), chain)
    }

    @Test
    fun parse_simpleEchoEchoPipe_gotCommands() {
        val chain = Parser.parse("echo 1 2 3 | echo 4 5 6")
        Assertions.assertEquals(CommandChain(listOf(EchoCommand(listOf("1", "2", "3")), EchoCommand(listOf("4", "5", "6")))), chain)
    }


    @Test
    fun parse_simpleEchoWcPipe_gotCommands() {
        val chain = Parser.parse("echo 1 2 3 | wc")
        Assertions.assertEquals(CommandChain(listOf(EchoCommand(listOf("1", "2", "3")), WcCommand(emptyList()))), chain)
    }

    @Test
    fun parse_withQuotedPipeExistingCommand_gotCommand() {
        val chain = Parser.parse("echo \"1\" \"2|3\" \'4|5\'")
        Assertions.assertEquals(CommandChain(listOf(EchoCommand(listOf("1", "2|3", "4|5")))), chain)
    }

    @Test
    fun parse_withQuotedPipePipe_gotCommands() {
        val chain = Parser.parse("echo \"1\" \"2|3\" \'4|5\' | echo 789 \"15|17\" 21")
        Assertions.assertEquals(CommandChain(listOf(EchoCommand(listOf("1", "2|3", "4|5")), EchoCommand(listOf("789", "15|17", "21")))), chain)
    }
}
