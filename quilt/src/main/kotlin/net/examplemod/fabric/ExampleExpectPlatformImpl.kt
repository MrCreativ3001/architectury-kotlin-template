package net.examplemod.fabric

import org.quiltmc.loader.api.QuiltLoader
import java.nio.file.Path

object ExampleExpectPlatformImpl {
    /**
     * This is our actual method to [ExampleExpectPlatform.getConfigDirectory].
     */
    @JvmStatic // Jvm Static is required so that java can access it
    fun getConfigDirectory(): Path {
        return QuiltLoader.getConfigDir()
    }
}