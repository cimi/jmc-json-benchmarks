import org.junit.Test
import org.openjdk.jmc.flightrecorder.JfrLoaderToolkit
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths

class EventCounter {

    @Test
    fun countEvents() {
        val projectDirAbsolutePath = Paths.get("").toAbsolutePath().toString()
        val resourcesPath = Paths.get(projectDirAbsolutePath, "/src/test/resources")
        Files.walk(resourcesPath)
            .filter { path -> Files.isRegularFile(path) }
            .forEach { path ->
                val eventGroups = JfrLoaderToolkit.loadEvents(FileInputStream(path.toFile()))
                val eventCount = eventGroups.sumOf { it.itemCount }
                val newName = "${eventCount.toString().padStart(length = 6, padChar = '0')}_${path.fileName}"
                Files.move(path, path.resolveSibling(newName))
            }
    }
}
