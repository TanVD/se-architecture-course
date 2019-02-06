package tanvd.gel

import org.junit.jupiter.api.AfterEach

abstract class TestBase {
    @AfterEach
    fun cleanup() {
        GlobalContext.clear()
    }
}

fun emptyStream() = ByteArray(0).inputStream()