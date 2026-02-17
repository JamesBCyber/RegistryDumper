package xyz.jamesbcyber.registrydumper.commands;

import com.google.gson.*;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import xyz.jamesbcyber.registrydumper.Utils.FileHandling;
import xyz.jamesbcyber.registrydumper.Utils.MessageFormatting;

import com.google.gson.*;
import java.io.File;
import java.util.*;

public class DumpItems {
    final static String enableDirectory = "Directory";

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("dumpitems").executes(DumpItems::executeBase).then(Commands.argument(enableDirectory, BoolArgumentType.bool()).executes(DumpItems::executeMultiFile)));
    }

    private static int executeBase(CommandContext<CommandSourceStack> context) {
        System.out.println(context.getSource());
        if (context.getSource().getEntity() instanceof Player player) {
            new Thread(() -> {

                IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
                Set<ResourceLocation> itemResources = itemRegistry.getKeys();

                Map<String, ArrayList<String>> ResourceMap = new HashMap<>();

                for (ResourceLocation itemLocation : itemResources) {
                    String namespace = itemLocation.getNamespace();
                    ResourceMap.putIfAbsent(namespace, new ArrayList<String>());
                    ResourceMap.get(namespace).add(itemLocation.getPath());

                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String message = gson.toJson(ResourceMap);


                File logfile = FileHandling.writeFile(new File("logs/registrydumper/"), "AllItems.json", message);
                if (logfile != null) {
                    MessageFormatting.sendOpenLogFileMessage(player, logfile.getAbsolutePath(), "Click for Dump");
                } else {
                    player.sendSystemMessage(Component.literal("Failed to write file"));
                }

            }).start();
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int executeMultiFile(CommandContext<CommandSourceStack> context) {
        if (!BoolArgumentType.getBool(context, enableDirectory)){
            return executeBase(context);
        }

        if (context.getSource().getEntity() instanceof Player player) {
            new Thread(() -> {

                IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
                Set<ResourceLocation> itemResources = itemRegistry.getKeys();

                Map<String, ArrayList<String>> ResourceMap = new HashMap<>();
                for (ResourceLocation itemLocation : itemResources) {
                    String namespace = itemLocation.getNamespace();
                    ResourceMap.putIfAbsent(namespace, new ArrayList<String>());
                    ResourceMap.get(namespace).add(itemLocation.toString());
                }

                String itemTemplate = "logs/registrydumper/itemdump/%s%s";
                for (String namespace : ResourceMap.keySet()){
                    JsonArray json = new JsonArray();
                    ResourceMap.get(namespace).forEach(json::add);

                    File filepath = new File(String.format(itemTemplate, namespace, ".json"));
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileHandling.writeFile(filepath, gson.toJson(json));
                }

                MessageFormatting.sendOpenLogFileMessage(player, new File("logs/registrydumper/itemdump/").getAbsolutePath(), "Items Dumped");

            }).start();
        }
        return Command.SINGLE_SUCCESS;
    }

}
