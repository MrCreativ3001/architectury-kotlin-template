package net.examplemod

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.CreativeTabRegistry.TabSupplier
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.examplemod.ExampleExpectPlatform.getConfigDirectory
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

object ExampleMod {
    const val MOD_ID = "examplemod"

    val exampleTab: TabSupplier = CreativeTabRegistry.create(
        ResourceLocation(MOD_ID, "example_tab"),
        Supplier {
            ItemStack(
                exampleItem.get()
            )
        }
    )

    private val items = DeferredRegister.create(MOD_ID, Registries.ITEM)
    val exampleItem: RegistrySupplier<Item> = items.register(
        "example_item"
    ) {
        Item(
            Item.Properties().`arch$tab`(exampleTab) // DON'T CALL GET ON exampleTab HERE
        )
    }

    fun init() {
        items.register()

        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
    }
}