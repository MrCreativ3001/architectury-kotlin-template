package net.examplemod

import dev.architectury.registry.CreativeTabRegistry
import dev.architectury.registry.registries.DeferredRegister
import dev.architectury.registry.registries.RegistrySupplier
import net.examplemod.ExampleExpectPlatform.getConfigDirectory
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.CreativeModeTabs
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Supplier

object ExampleMod {
    const val MOD_ID = "examplemod"

    private val createModeTabs = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB)
    val exampleTab: RegistrySupplier<CreativeModeTab> = createModeTabs.register("example_tab") {
        CreativeTabRegistry.create(Component.translatable("category.examplemod")) {
            ItemStack(exampleItem.get())
        }
    }

    private val items = DeferredRegister.create(MOD_ID, Registries.ITEM)
    val exampleItem: RegistrySupplier<Item> = items.register(
        "example_item"
    ) {
        Item(
            Item.Properties().`arch$tab`(exampleTab) // DON'T CALL GET ON exampleTab HERE
        )
    }

    fun init() {
        createModeTabs.register()
        items.register()

        println("CONFIG DIR: ${getConfigDirectory().toAbsolutePath().normalize()}")
    }
}