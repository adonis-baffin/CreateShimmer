package com.adonis.createshimmer.data;

import static com.adonis.createshimmer.common.CSCommon.REGISTRATE;

import com.adonis.createshimmer.common.CSCommon;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.data.loading.DatagenModLoader;

@Mod(CSCommon.ID)
public class CSData {
    public CSData(IEventBus modBus) {
        if (!DatagenModLoader.isRunningDataGen())
            return;
        REGISTRATE.registerBuiltinLocalization("interface");
        REGISTRATE.registerForeignLocalization();
//        REGISTRATE.registerPonderLocalization(CSPonderPlugin::new);
        modBus.register(this);
    }

    @SubscribeEvent
    public void generate(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var existingFileHelper = event.getExistingFileHelper();
        var lookupProvider = event.getLookupProvider();
        var output = generator.getPackOutput();
        var client = event.includeClient();
        var server = event.includeServer();

        // 添加配方提供器
        generator.addProvider(server, new CSRecipeProvider(output, lookupProvider));

        // 添加方块标签提供器
        var blockTagProvider = generator.addProvider(server,
                new CSBlockTagProvider(output, lookupProvider, existingFileHelper));

        // 添加物品标签提供器
        generator.addProvider(server,
                new CSItemTagProvider(output, lookupProvider, blockTagProvider.contentsGetter(), existingFileHelper));

        // 添加流体标签提供器
        generator.addProvider(server,
                new CSFluidTagProvider(output, lookupProvider, existingFileHelper));
    }
}
