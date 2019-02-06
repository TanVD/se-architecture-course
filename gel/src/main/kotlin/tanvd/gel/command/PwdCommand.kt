package tanvd.gel.command

import java.io.InputStream
import java.nio.file.Paths

class PwdCommand : Command(emptyList()) {
    override fun execute(inputStream: InputStream): ByteArray {
        return Paths.get(".").toAbsolutePath().normalize().toString().toByteArray()
    }
}