package xyz.jamesbcyber.registrydumper.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CommandsRegister {
    @SubscribeEvent
    public static void RegisterServerCommands(RegisterCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        DumpRecipes.register(dispatcher);
    }

    @SubscribeEvent
    public static void RegisterClientCommands(RegisterClientCommandsEvent event){
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        DumpItems.register(dispatcher);
        DumpTags.register(dispatcher);
    }
}
