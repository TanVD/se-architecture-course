package tanvd.gel.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tanvd.gel.GlobalContext
import tanvd.gel.TestBase
import tanvd.gel.emptyStream
import java.io.File
import java.nio.file.Paths

class CommandsTest : TestBase() {
    @Test
    fun assignCommand_notExistsVariable_variableAdded() {
        val command = AssignCommand(listOf("a", "b"))
        command.execute(emptyStream())
        Assertions.assertEquals("b", GlobalContext["a"])
    }

    @Test
    fun assignCommand_existsVariable_variableUpdated() {
        GlobalContext["a"] = "b"

        val command = AssignCommand(listOf("a", "c"))
        command.execute(emptyStream())
        Assertions.assertEquals("c", GlobalContext["a"])
    }

    @Test
    fun catCommand_fromFile_gotRightResult() {
        val command = CatCommand(listOf("src/test/resources/test.txt"))
        Assertions.assertEquals(File("src/test/resources/test.txt").readText(), String(command.execute(emptyStream())))
    }

    @Test
    fun echoCommand_echoValues_valuesPrinted() {
        val command = EchoCommand(listOf("a", "b"))
        Assertions.assertEquals("a b", String(command.execute(emptyStream())))
    }

    @Test
    fun exitCommand_echoConsole_exceptionThrown() {
        val command = ExitCommand()
        try {
            command.execute(emptyStream())
        } catch (e: ExitException) {
            return
        }
        Assertions.fail<ExitException>("Exit exception not thrown")
    }

    @Test
    fun pwdCommand_pathPrinter_pathEqualsToCurrent() {
        val command = PwdCommand()
        Assertions.assertEquals(Paths.get(".").toAbsolutePath().normalize().toString(), String(command.execute(emptyStream())))
    }


    @Test
    fun wcCommand_fromFile_gotRightResult() {
        val command = WcCommand(listOf("src/test/resources/test.txt"))
        Assertions.assertEquals("3 4 14", String(command.execute(emptyStream())))
    }

    @Test
    fun wcCommand_fromStream_gotRightResult() {
        val command = WcCommand(emptyList())
        Assertions.assertEquals("2 3 11", String(command.execute("123 456\n8910".byteInputStream())))
    }

    @Test
    fun commandChain_pipesOutputs_gotRightResult() {
        val echoCommand = EchoCommand(listOf("1 2 3"))
        val wcCommand = WcCommand(emptyList())
        val chain = CommandChain(listOf(echoCommand, wcCommand))
        Assertions.assertEquals("1 3 5", String(chain.execute(emptyStream())))
    }

}
