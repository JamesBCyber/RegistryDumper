package xyz.jamesbcyber.registrydumper.Utils;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;
import umpaz.brewinandchewin.common.crafting.KegPouringRecipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.DoughRecipe;
import vectorwing.farmersdelight.common.crafting.FoodServingRecipe;

import com.google.gson.*;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.*;

public class Deserializer {
    private static RegistryAccess registryAccess;

    public static RegistryAccess getRegistryAccess() {
        return registryAccess;
    }

    public static void setRegistryAccess(RegistryAccess registryAccess) {
        Deserializer.registryAccess = registryAccess;
    }

    public static JsonObject RecipeToJson(Recipe<?> craftingRecipe) throws MissingResourceException {
        if (registryAccess == null){
            throw new NullPointerException("RegistryAccess was not set");
        }
        JsonObject jsonobj;

        if (craftingRecipe instanceof ShapedRecipe){
            jsonobj = shapedToJson((ShapedRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof ShapelessRecipe){
            jsonobj = shapelessToJson((ShapelessRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof SmeltingRecipe){
            jsonobj = smeltingToJson((SmeltingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof BlastingRecipe){
            jsonobj = blastingToJson((BlastingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof SmokingRecipe){
            jsonobj = smokingToJson((SmokingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof CampfireCookingRecipe){
            jsonobj = campfireToJson((CampfireCookingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof StonecutterRecipe){
            jsonobj = stoneCutterToJson((StonecutterRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof SmithingTransformRecipe){
            jsonobj = smithingTransformToJson((SmithingTransformRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof SmithingTrimRecipe){
            jsonobj = smithingTrimToJson((SmithingTrimRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof KegFermentingRecipe){
            jsonobj = fermentingToJson((KegFermentingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof KegPouringRecipe){
            jsonobj = kegPouringToJson((KegPouringRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof CookingPotRecipe){
            jsonobj = cookingToJson((CookingPotRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof CuttingBoardRecipe){
            jsonobj = cuttingToJson((CuttingBoardRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof FoodServingRecipe){
            jsonobj = foodServingToJson((FoodServingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof DoughRecipe){
            jsonobj = doughToJson((DoughRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof ArmorDyeRecipe){
            jsonobj = armorDyeToJson((ArmorDyeRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof BookCloningRecipe){
            jsonobj = bookCloneToJson((BookCloningRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof MapCloningRecipe){
            jsonobj = mapCloneToJson((MapCloningRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof MapExtendingRecipe){
            jsonobj = mapExtendToJson((MapExtendingRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof FireworkRocketRecipe){
            jsonobj = fireworkRocketToJson((FireworkRocketRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof FireworkStarRecipe){
            jsonobj = fireworkStarToJson((FireworkStarRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof FireworkStarFadeRecipe){
            jsonobj = fireworkStarFadeToJson((FireworkStarFadeRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof TippedArrowRecipe){
            jsonobj = tippedArrowToJson((TippedArrowRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof BannerDuplicateRecipe){
            jsonobj = bannerDuplicateToJson((BannerDuplicateRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof ShieldDecorationRecipe){
            jsonobj = shieldDecorToJson((ShieldDecorationRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof ShulkerBoxColoring){
            jsonobj = SkulkerColorToJson((ShulkerBoxColoring) craftingRecipe);
        } else if (craftingRecipe instanceof SuspiciousStewRecipe){
            jsonobj = suspiciousStewToJson((SuspiciousStewRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof RepairItemRecipe){
            jsonobj = repairToJson((RepairItemRecipe) craftingRecipe);
        } else if (craftingRecipe instanceof DecoratedPotRecipe){
            jsonobj = decoratedPotToJson((DecoratedPotRecipe) craftingRecipe);
        } else {
            jsonobj = new JsonObject();
        }

        return jsonobj;
    }

    public static String itemString(Object item){
        Item lookup;
        if (item instanceof ItemStack){
            lookup = ((ItemStack) item).getItem();
        } else if (item instanceof Item){
            lookup = (Item) item;
        } else { return ""; }
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(lookup)).toString();
    }

    private static JsonObject getResultJson(Recipe<?> craftingRecipe){
        JsonObject result = new JsonObject();
        ItemStack resultItems = craftingRecipe.getResultItem(getRegistryAccess());
        result.addProperty("item", itemString(resultItems));
        if (resultItems.getCount() > 1){
            result.addProperty("count", resultItems.getCount());
        }
        return result;
    }

    private static JsonElement ingredientListToJson(Recipe<?> craftingRecipe){
        if (craftingRecipe.getIngredients().size() > 1){

            JsonArray ingredientJson = new JsonArray();
            craftingRecipe.getIngredients().forEach(ingredient -> {
                ingredientJson.add(ingredient.toJson());
            });
            return ingredientJson;
        } else {
            return craftingRecipe.getIngredients().get(0).toJson();
        }
    }

    private static <T extends AbstractCookingRecipe> JsonObject abstractCookingToJson(T craftingRecipe, String type){
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        json.addProperty("category", craftingRecipe.category().toString().toLowerCase());
        json.addProperty("cookingtime", craftingRecipe.getCookingTime());
        json.addProperty("experience", craftingRecipe.getExperience());
        if (!craftingRecipe.getGroup().isEmpty()){
            json.addProperty("group", craftingRecipe.getGroup());
        }

        json.add("ingredients", ingredientListToJson(craftingRecipe));

        json.add("result", getResultJson(craftingRecipe));

        return json;
    }

    private static <T extends SingleItemRecipe> JsonObject singleItemRecipeToJson(T craftingRecipe, String type){
        JsonObject json = new JsonObject();
        json.addProperty("type", type);
        ItemStack resultItems = craftingRecipe.getResultItem(getRegistryAccess());
        json.addProperty("count", resultItems.getCount());

        json.add("ingredients", ingredientListToJson(craftingRecipe));

        json.addProperty("result", itemString(resultItems));

        return json;
    }

    private static JsonObject shapedToJson(ShapedRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", craftingRecipe.category().toString().toLowerCase());
        if (!craftingRecipe.getGroup().isEmpty()){
            json.addProperty("group", craftingRecipe.getGroup());
        }

        int width = craftingRecipe.getWidth();
        int height = craftingRecipe.getHeight();
        JsonArray pattern = new JsonArray();

        ArrayList<String> available_keys = new ArrayList<>(List.of(
                "#", "A", "B", "C", "D", "E",
                "F", "G", "H", "I", "J", "K", "L", "M",
                "N", "O", "P", "Q", "R", "S", "T", "U",
                "V", "W", "X", "Y", "Z"));
        HashMap<JsonElement, String> keys = new HashMap<>();

        StringBuilder pattern_line = new StringBuilder();
        int counter = 0;
        for (Ingredient ingredient : craftingRecipe.getIngredients()) {
            JsonElement ingredientJson = ingredient.toJson();
            boolean empty = false;
            if (ingredientJson instanceof JsonArray && ((JsonArray) ingredientJson).isEmpty()){
                empty = true;
            } else if (!keys.containsKey(ingredientJson)){
                keys.put(ingredientJson, available_keys.get(0));
                available_keys.remove(0);
            }
            counter += 1;
            if (empty){ pattern_line.append(" ");
            } else { pattern_line.append(keys.get(ingredientJson)); }
            if (counter >= width){
                counter = 0;
                pattern.add(pattern_line.toString());
                pattern_line.delete(0, pattern_line.length());
            }
        };
        JsonObject keyjson = new JsonObject();
        for (JsonElement k : keys.keySet()){
            keyjson.add(keys.get(k), k);
        }

        json.add("key", keyjson);
        json.add("pattern", pattern);
        json.add("result", getResultJson(craftingRecipe));

        if (craftingRecipe.showNotification()){
            json.addProperty("show_notification", craftingRecipe.showNotification());
        }

        return json;
    }

    private static JsonObject shapelessToJson(ShapelessRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shapeless");
        json.addProperty("category", craftingRecipe.category().toString().toLowerCase());
        if (!craftingRecipe.getGroup().isEmpty()){
            json.addProperty("group", craftingRecipe.getGroup());
        }

        json.add("ingredients", ingredientListToJson(craftingRecipe));
        json.add("result", getResultJson(craftingRecipe));

        if (craftingRecipe.showNotification()){
            json.addProperty("show_notification", craftingRecipe.showNotification());
        }

        return json;
    }

    private static JsonObject smeltingToJson(SmeltingRecipe craftingRecipe) {
        return abstractCookingToJson(craftingRecipe, "minecraft:smelting");
    }

    private static JsonObject blastingToJson(BlastingRecipe craftingRecipe) {
        return abstractCookingToJson(craftingRecipe, "minecraft:blasting");
    }

    private static JsonObject smokingToJson(SmokingRecipe craftingRecipe) {
        return abstractCookingToJson(craftingRecipe, "minecraft:smoking");
    }

    private static JsonObject campfireToJson(CampfireCookingRecipe craftingRecipe) {
        return abstractCookingToJson(craftingRecipe, "minecraft:campfire_cooking");
    }

    private static JsonObject stoneCutterToJson(StonecutterRecipe craftingRecipe) {
        return singleItemRecipeToJson(craftingRecipe, "minecraft:stonecutting");
    }

    private static JsonObject smithingTransformToJson(SmithingTransformRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
//        json.addProperty("type", "minecraft:smithing_transformation");
//        json.add("template", getPrivateIngredient(craftingRecipe, "template"));
//        json.add("base", getPrivateIngredient(craftingRecipe, "base"));
//        json.add("addition", getPrivateIngredient(craftingRecipe, "addition"));
//        json.add("result", getResultJson(craftingRecipe));
        return json;
    }

    private static JsonObject smithingTrimToJson(SmithingTrimRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
//        json.addProperty("type", "minecraft:smithing_trim");
//        json.add("template", getPrivateIngredient(craftingRecipe, "p_267298_"));
//        json.add("base", getPrivateIngredient(craftingRecipe, "p_266862_"));
//        json.add("addition", getPrivateIngredient(craftingRecipe, "p_267050_"));
        return json;
    }

    private static JsonObject fermentingToJson(KegFermentingRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "brewinandchewin:fermenting");
        json.addProperty("experience", craftingRecipe.getExperience());
        json.addProperty("fermentingtime", craftingRecipe.getFermentTime());
        json.addProperty("temperature", craftingRecipe.getTemperature());
        if (craftingRecipe.getRecipeBookTab() != null){
            json.addProperty("recipe_book_tab", craftingRecipe.getRecipeBookTab().toString());
        }

        FluidStack fluidIngredient = craftingRecipe.getFluidIngredient();
        if (fluidIngredient != null){
            JsonObject basefluid = new JsonObject();
            basefluid.addProperty("count", fluidIngredient.getAmount());
            basefluid.addProperty("fluid", fluidIngredient.getFluid().getFluidType().toString());
            json.add("basefluid", basefluid);
        }

        json.add("ingredients", ingredientListToJson(craftingRecipe));

        JsonObject result = new JsonObject();
        if (craftingRecipe.getResultFluid() != null){
            result.addProperty("count", craftingRecipe.getAmount());
            result.addProperty("fluid", craftingRecipe.getResultFluid().getFluidType().toString());
            json.add("result", result);
        } else if (craftingRecipe.getResultItem() != null){
            json.add("result", getResultJson(craftingRecipe));
        }

        return json;
    }

    private static JsonObject kegPouringToJson(KegPouringRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "brewinandchewin:keg_pouring");
        json.addProperty("amount", craftingRecipe.getAmount());
        if (craftingRecipe.getRawContainer().isPresent()){
            json.addProperty("contain", itemString(craftingRecipe.getContainer()));
        }
        json.addProperty("strict", craftingRecipe.isStrict());
        json.addProperty("filling", craftingRecipe.canFill());
        json.addProperty("fluid", craftingRecipe.getRawFluid().getFluidType().toString());

        json.add("output", getResultJson(craftingRecipe));

        return json;
    }

    private static JsonObject cookingToJson(CookingPotRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "farmersdelight:cooking");
        if (!craftingRecipe.getGroup().isEmpty()){
            json.addProperty("group", craftingRecipe.getGroup());
        }
        json.addProperty("experience", craftingRecipe.getExperience());
        json.addProperty("cookingtime", craftingRecipe.getCookTime());
        if (craftingRecipe.getRecipeBookTab() != null){
            json.addProperty("recipe_book_tab", craftingRecipe.getRecipeBookTab().toString());
        }
        if (!craftingRecipe.getOutputContainer().isEmpty()){
            JsonObject container = new JsonObject();
            container.addProperty("item", itemString(craftingRecipe.getOutputContainer()));
            json.add("container", container);
        }
        json.add("ingredients", ingredientListToJson(craftingRecipe));
        json.add("result", getResultJson(craftingRecipe));

        return json;
    }

    private static JsonObject cuttingToJson(CuttingBoardRecipe craftingRecipe) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "farmersdelight:cutting");
        json.add("ingredients", ingredientListToJson(craftingRecipe));

        json.add("tool", craftingRecipe.getTool().toJson());

        // cutting board needs custom result to handle multiple outputs
        JsonArray result = new JsonArray();
        craftingRecipe.getRollableResults().forEach( rollable -> {
            result.add(rollable.serialize());
        });
        json.add("result", result);

        return json;
    }

    // Only food_serving.json recipe
    // Will update if more options are provided
    private static JsonObject foodServingToJson(FoodServingRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only wheat_dough_from_water.json recipe
    // Will update if more options are provided
    private static JsonObject doughToJson(DoughRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only armor_dye.json recipe
    // Will update if more options are provided
    private static JsonObject armorDyeToJson(ArmorDyeRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only book_cloning.json recipe
    // Will update if more options are provided
    private static JsonObject bookCloneToJson(BookCloningRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only map_cloning.json recipe
    // Will update if more options are provided
    private static JsonObject mapCloneToJson(MapCloningRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only map_extending.json recipe
    // Will update if more options are provided
    private static JsonObject mapExtendToJson(MapExtendingRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only firework_rocket.json recipe
    // Will update if more options are provided
    private static JsonObject fireworkRocketToJson(FireworkRocketRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only firework_star.json recipe
    // Will update if more options are provided
    private static JsonObject fireworkStarToJson(FireworkStarRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only firework_star_fade.json recipe
    // Will update if more options are provided
    private static JsonObject fireworkStarFadeToJson(FireworkStarFadeRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only tipped_arrow.json recipe
    // Will update if more options are provided
    private static JsonObject tippedArrowToJson(TippedArrowRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only banner_duplicate.json recipe
    // Will update if more options are provided
    private static JsonObject bannerDuplicateToJson(BannerDuplicateRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only shield_decoration.json recipe
    // Will update if more options are provided
    private static JsonObject shieldDecorToJson(ShieldDecorationRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only shulker_box_coloring.json recipe
    // Will update if more options are provided
    private static JsonObject SkulkerColorToJson(ShulkerBoxColoring craftingRecipe) {
        return new JsonObject();
    }

    // Only suspicious_stew.json recipe
    // Will update if more options are provided
    private static JsonObject suspiciousStewToJson(SuspiciousStewRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only repair_item.json recipe
    // Will update if more options are provided
    private static JsonObject repairToJson(RepairItemRecipe craftingRecipe) {
        return new JsonObject();
    }

    // Only decorated_pot.json recipe
    // Will update if more options are provided
    private static JsonObject decoratedPotToJson(DecoratedPotRecipe craftingRecipe) {
        return new JsonObject();
    }

//    @Nullable
//    private static Object getReflectedPrivate(Object obj, String field){
//        Field f;
//        Object v;
//        try{
//            f = obj.getClass().getDeclaredField(field);
//            f.setAccessible(true);
//            v = f.get(obj);
//        } catch (Exception e){
//            return null;
//        }
//        return v;
//    }

    private static JsonElement getPrivateIngredient(SmithingTrimRecipe recipe, String field){
        Field fieldProperty;
        JsonElement ing;
        try{
            fieldProperty = recipe.getClass().getDeclaredField(field);
            fieldProperty.setAccessible(true);
            ing = ((Ingredient) fieldProperty.get(recipe)).toJson();
        } catch (Exception e){
            System.err.println("Failed to Read Private Variable");
            System.err.println(recipe.getClass());
            System.err.println(recipe);
            System.err.println(e);
            return null;
        }
        return ing;
    }

    @Nullable
    private static JsonElement getPrivateIngredient(SmithingTransformRecipe recipe, String field){
        Field fieldProperty;
        JsonElement ing;
        try{
            fieldProperty = recipe.getClass().getDeclaredField(field);
            fieldProperty.setAccessible(true);
            ing = ((Ingredient) fieldProperty.get(recipe)).toJson();
        } catch (Exception e){
            System.err.println("Failed to Read Private Variable");
            System.err.println(recipe.getClass());
            System.err.println(recipe);
            System.err.println(e);
            return null;
        }
        return ing;
    }

}
