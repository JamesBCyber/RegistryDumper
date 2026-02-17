# Minecraft Registry Dumper
A tool to help in modpack development and creating mod compatibility features

This mod adds a few slash commands that will create files in an easily copiable format that mimics
how mods and datapacks expect your data to be formatted

Output files can be found in logs/registrydumper with a clickable link to the folder or file within the in game chat

Commands Available
- `/dumpitems`
  - Dumps a json file containing all the files organized by their namespace 
  - Optional argument `true` will split it into multiple files, each namespace gets their own file
- `/dumptags`
  - Dumps all the tags available by the ITagManager, collects the Item ResourceLocations for every item in the tag and formats the output directory/files
  - Has an issue with dumping nested tags i.e. `#forge:tools/shields` and other json objects within the JsonArray. This will be looked into later if possible
- `/dumprecipes`
  - Runs server side to get the RecipeManager to get every recipe currently loaded by all the mods/datapacks
  - Runs them through a Deserializer to get the JsonObject, then outputs them in formated directories

## Disclaimer
The Deserializer does have all the transfers from Recipe Objects to json in numberous methods, only select mods (or recipe types) will be supported

Currently Supported Mods and Recipes:

- Minecraft
  - crafting_shaped
  - crafting_shapeless
  - smoking
  - smelting
  - blasting
  - campfire_cooking
  - stonecutting
  - \*smithing_trim
  - \*smithing_transform
  
- Farmers Delight
  - cooking
  - cutting

- Brewin and Chewin
    - fermenting
    - pouring


\**Its in the code but may not be functional*