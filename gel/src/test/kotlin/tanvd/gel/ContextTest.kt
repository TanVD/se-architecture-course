package tanvd.gel

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ContextTest : TestBase() {
    @Test
    fun expand_constructFullyOperation_rendered() {
        GlobalContext["a"] = "123"
        GlobalContext["b"] = "456"
        GlobalContext["c"] = "789"
        Assertions.assertEquals("123456 \"789\"", GlobalContext.expand("\$a\${b} \"\$c\""))
    }

    @Test
    fun expand_constructPartlyOperation_rendered() {
        GlobalContext["a"] = "123"
        GlobalContext["b"] = "456"
        GlobalContext["c"] = "789"
        Assertions.assertEquals("plan456 123 \"789\"", GlobalContext.expand("plan\${b} \${a} \"\$c\""))
    }

    @Test
    fun expand_doubleQuotes_rendered() {
        GlobalContext["a"] = "123"
        GlobalContext["b"] = "456"
        GlobalContext["c"] = "789"
        Assertions.assertEquals("plan \"123\" \"456\" \"789\"", GlobalContext.expand("plan \"\$a\" \"\${b}\" \"\$c\""))
    }

    @Test
    fun expand_unaryQuotes_rendered() {
        GlobalContext["a"] = "123"
        GlobalContext["b"] = "456"
        GlobalContext["c"] = "789"
        Assertions.assertEquals("plan123 \'\$a\' \'\${b}\' \"789\"", GlobalContext.expand("plan\$a \'\$a\' \'\${b}\' \"\$c\""))
    }
}
