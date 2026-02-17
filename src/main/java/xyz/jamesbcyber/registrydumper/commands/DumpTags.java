package xyz.jamesbcyber.registrydumper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.tags.ITagManager;
import xyz.jamesbcyber.registrydumper.Utils.Deserializer;
import xyz.jamesbcyber.registrydumper.Utils.FileHandling;
import xyz.jamesbcyber.registrydumper.Utils.MessageFormatting;

import java.io.File;
import java.util.*;
import com.google.gson.*;

public class DumpTags {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("dumptags").executes(DumpTags::executeBase));
    }

    private static int executeBase(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            new Thread(() -> {
                IForgeRegistry<Item> itemRegistry = ForgeRegistries.ITEMS;
                Set<ResourceLocation> itemResources = itemRegistry.getKeys();
                ITagManager<Item> tagManager = ForgeRegistries.ITEMS.tags();

                String tagTemplate = "logs/registrydumper/tagdump/%s/tags/items/%s%s";
                tagManager.stream().forEach(iTag -> {
                    JsonObject json = new JsonObject();
                    JsonArray itemArray = new JsonArray();
                    iTag.forEach(item -> {
                        itemArray.add(Deserializer.itemString(item));
                    });
                    json.add("values", itemArray);

                    ResourceLocation tagLocation = iTag.getKey().location();

                    File filepath = new File(String.format(tagTemplate, tagLocation.getNamespace(), tagLocation.getPath(), ".json"));
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    FileHandling.writeFile(filepath, gson.toJson(json));
                });

                MessageFormatting.sendOpenLogFileMessage(player, new File("logs/registrydumper/tagdump/").getAbsolutePath(), "Tags Dumped");
            }).start();
        }
        return Command.SINGLE_SUCCESS;
    }
}