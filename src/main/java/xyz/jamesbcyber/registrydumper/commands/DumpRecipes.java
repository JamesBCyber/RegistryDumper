package xyz.jamesbcyber.registrydumper.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import xyz.jamesbcyber.registrydumper.Utils.Deserializer;
import xyz.jamesbcyber.registrydumper.Utils.FileHandling;
import xyz.jamesbcyber.registrydumper.Utils.MessageFormatting;

import com.google.gson.*;
import java.io.File;

public class DumpRecipes {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("dumprecipes").executes(DumpRecipes::executeBase));
    }

    private static int executeBase(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof Player player) {
            new Thread(() -> {
                MinecraftServer sev = context.getSource().getServer();
                RecipeManager recipesMan = sev.getRecipeManager();
                Deserializer.setRegistryAccess(sev.registryAccess());

                String recipesTemplate = "logs/registrydumper/recipedump/%s/recipes/%s%s";

                for (Recipe<?> recipe : recipesMan.getRecipes()){
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    JsonObject jsonobj = Deserializer.RecipeToJson(recipe);
                    if (jsonobj.keySet().isEmpty()){ continue; }

                    String namespace = recipe.getId().getNamespace();
                    String recipePath = recipe.getId().getPath();

                    File filepath = new File(String.format(recipesTemplate, namespace, recipePath, ".json"));
                    FileHandling.writeFile(filepath, gson.toJson(jsonobj));
                }

                MessageFormatting.sendOpenLogFileMessage(player, new File("logs/registrydumper/recipedump/").getAbsolutePath(), "Recipes Dumped");
            }).start();
        }
        return Command.SINGLE_SUCCESS;
    }
}
