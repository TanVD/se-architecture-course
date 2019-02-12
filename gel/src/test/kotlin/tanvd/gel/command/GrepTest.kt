package tanvd.gel.command

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import tanvd.gel.TestBase

class GrepTest : TestBase() {
    companion object {
        val sampleText = """
            Lorem ipsum dolor sit amet, consectetur adipiscing elit.

            Duis ac imperdiet tellus. Pellentesque imperdiet dictum justo,
            ut tristique quam aliquam ut. Vestibulum ultricies, mi vitae
            semper sollicitudin, magna mi sollicitudin metus, sed dapibus
            urna ipsum a quam. Interdum et malesuada fames ac ante ipsum
            primis in faucibus. Nulla facilisi. Vivamus porta ante ac
            elementum molestie. Quisque sit metus.""".trimIndent()
    }

    @Test
    fun grepCommand_containLineNoModifiers_gotLine() {
        val command = GrepCommand(listOf("tellus"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("Duis ac imperdiet tellus. Pellentesque imperdiet dictum justo,", String(result))
    }

    @Test
    fun grepCommand_notContainLineNoModifiers_noLine() {
        val command = GrepCommand(listOf("johnny"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("", String(result))
    }

    @Test
    fun grepCommand_fileContainLineNoModifiers_gotLine() {
        val command = GrepCommand(listOf("test", "src/test/resources/test.txt"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("test test\ntest", String(result))
    }

    @Test
    fun grepCommand_fileNotContainLineNoModifiers_noLine() {
        val command = GrepCommand(listOf("johnny", "src/test/resources/test.txt"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("", String(result))
    }

    @Test
    fun grepCommand_containLineInsensitive_gotLine() {
        val command = GrepCommand(listOf("-i", "TELLUS"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("Duis ac imperdiet tellus. Pellentesque imperdiet dictum justo,", String(result))
    }

    @Test
    fun grepCommand_containOtherCaseLineNoInsensitive_noLine() {
        val command = GrepCommand(listOf("TELLUS"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("", String(result))
    }

    @Test
    fun grepCommand_containLineWordFull_gotLine() {
        val command = GrepCommand(listOf("-w", "tellus"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("Duis ac imperdiet tellus. Pellentesque imperdiet dictum justo,", String(result))
    }

    @Test
    fun grepCommand_containPartOfLineWordFull_noLine() {
        val command = GrepCommand(listOf("-w", "tell"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("", String(result))
    }

    @Test
    fun grepCommand_containLineAfterLines_gotLines() {
        val command = GrepCommand(listOf("-A 1", "tellus"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("Duis ac imperdiet tellus. Pellentesque imperdiet dictum justo,\n" +
                "ut tristique quam aliquam ut. Vestibulum ultricies, mi vitae", String(result))
    }

    @Test
    fun grepCommand_containFewLinesAfterLines_allLines() {
        val command = GrepCommand(listOf("-A 2", "quam"))
        val result = command.execute(sampleText.byteInputStream())
        Assertions.assertEquals("ut tristique quam aliquam ut. Vestibulum ultricies, mi vitae\n" +
                "semper sollicitudin, magna mi sollicitudin metus, sed dapibus\n" +
                "urna ipsum a quam. Interdum et malesuada fames ac ante ipsum\n" +
                "primis in faucibus. Nulla facilisi. Vivamus porta ante ac\n" +
                "elementum molestie. Quisque sit metus.", String(result))
    }
}
