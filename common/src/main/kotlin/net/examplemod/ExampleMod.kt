package net.examplemod

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.CreativeTabRegistry.TabSupplier
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier


object ExampleMod {
    const val MOD_ID = "examplemod"

    // Registering a new creative tab
    val EXAMPLE_TAB: TabSupplier = CreativeTabRegistry.create(
        ResourceLocation(MOD_ID, "example_tab"),
        Supplier { ItemStack(EXAMPLE_ITEM.get()) }
    )
    private val ITEMS: DeferredRegister<Item> = DeferredRegister.create(MOD_ID, Registries.ITEM)
    val EXAMPLE_ITEM: RegistrySupplier<Item> = ITEMS.register(
        "example_item"
    ) {
        Item(
            Item.Properties().`arch$tab`(EXAMPLE_TAB.get()) as Item.Properties
        )
    }

    fun init() {
        ITEMS.register()

        println("CONFIG DIR: " + ExampleExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString())
    }
}
