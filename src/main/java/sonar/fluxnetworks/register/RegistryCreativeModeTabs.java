package sonar.fluxnetworks.register;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;
import sonar.fluxnetworks.FluxNetworks;

public class RegistryCreativeModeTabs {
    public static final ResourceLocation CREATIVE_MODE_TAB_KEY = FluxNetworks.location("tab");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CREATIVE_MODE_TAB = DeferredHolder.create(
            Registries.CREATIVE_MODE_TAB, CREATIVE_MODE_TAB_KEY
    );

    static void register(RegisterEvent.RegisterHelper<CreativeModeTab> helper) {
        helper.register(CREATIVE_MODE_TAB_KEY, CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + FluxNetworks.MODID))
                .icon(() -> new ItemStack(RegistryItems.FLUX_CORE.get()))
                .displayItems((parameters, output) -> {
                    output.accept(RegistryItems.FLUX_BLOCK.get());
                    output.accept(RegistryItems.FLUX_PLUG.get());
                    output.accept(RegistryItems.FLUX_POINT.get());
                    output.accept(RegistryItems.FLUX_CONTROLLER.get());
                    output.accept(RegistryItems.BASIC_FLUX_STORAGE.get());
                    output.accept(RegistryItems.HERCULEAN_FLUX_STORAGE.get());
                    output.accept(RegistryItems.GARGANTUAN_FLUX_STORAGE.get());
                    output.accept(RegistryItems.FLUX_DUST.get());
                    output.accept(RegistryItems.FLUX_CORE.get());
                    output.accept(RegistryItems.FLUX_CONFIGURATOR.get());
                    output.accept(RegistryItems.ADMIN_CONFIGURATOR.get());
                })
                .build());
    }

    private RegistryCreativeModeTabs() {}
}
