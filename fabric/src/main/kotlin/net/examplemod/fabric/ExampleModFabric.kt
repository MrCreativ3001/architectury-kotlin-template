package net.examplemod.fabric

import net.examplemod.fabriclike.ExampleModFabricLike
import net.fabricmc.api.ModInitializer


object ExampleModFabric: ModInitializer {
    override fun onInitialize() {
        ExampleModFabricLike.init()
    }
}
