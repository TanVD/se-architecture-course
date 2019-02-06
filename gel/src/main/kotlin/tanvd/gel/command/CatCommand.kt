package tanvd.gel.command

import java.io.File
import java.io.InputStream

class CatCommand(params: List<String>) : Command(params) {
    override fun execute(inputStream: InputStream): ByteArray {
        return File(params.first()).readBytes()
    }
}