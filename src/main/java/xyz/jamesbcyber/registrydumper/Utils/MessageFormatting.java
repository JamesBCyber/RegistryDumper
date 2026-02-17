package xyz.jamesbcyber.registrydumper.Utils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;

public class MessageFormatting {


    // --- Rich Text Message with Click-to-Copy ---

    /**
     * Sends a green, clickable message that copies the specified content to the clipboard.
     * The displayed text will be "Click to copy: [content]".
     *
     * @param player The player to send the message to.
     * @param contentToCopy The text content that will be copied to the clipboard.
     */
    public static void sendClickToCopyMessage(Player player, String contentToCopy) {
        MutableComponent message = Component.literal("Click to copy: ")
                .withStyle(style -> style.withColor(ChatFormatting.GREEN)); // Base text style

        MutableComponent clickableText = Component.literal(contentToCopy)
                .withStyle(style -> style
                        .withColor(ChatFormatting.GREEN)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, contentToCopy))
                        .withUnderlined(true)); // Optional: make it look like a link

        player.sendSystemMessage(message.append(clickableText));
    }

    public static void sendCopyMessage(Player player, String normalText, String copyText){
       MutableComponent message = Component.literal(normalText)
               .withStyle(style -> style.withColor(ChatFormatting.GREEN));

       MutableComponent clickText = Component.literal(copyText)
               .withStyle(style -> style
                       .withColor(ChatFormatting.GREEN)
                       .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, copyText))
                       .withUnderlined(true)
               );
       message = message.append(clickText);
       player.sendSystemMessage(message);
    }


    /**
     * Sends a green hyperlink-style message that copies the specified content to the clipboard.
     * The displayed text will be "Click here".
     *
     * @param player The player to send the message to.
     * @param contentToCopy The text content that will be copied to the clipboard.
     */
    public static void sendHyperlinkClickToCopyMessage(Player player, String normalText, String contentToCopy) {
        MutableComponent message = Component.literal(normalText)
                .withStyle(style -> style
                        .withColor(ChatFormatting.RED)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, contentToCopy))
                );

        player.sendSystemMessage(message);
    }

    // --- Message Linking to Log File ---

    /**
     * Sends a blue, clickable message that opens the specified log file.
     * The displayed text will be "Open log file".
     *
     * @param player The player to send the message to.
     * @param filePath The absolute path to the log file to open.
     */
    public static void sendOpenLogFileMessage(Player player, String filePath, String text) {
        if (filePath == null || filePath.isEmpty()) {
            player.sendSystemMessage(Component.literal("Error: Log file path is invalid.").withStyle(ChatFormatting.RED));
            return;
        }

        MutableComponent message = Component.literal(text)
                .withStyle(style -> style
                        .withColor(ChatColor.BLUE) // Using custom TextColor for specific blue
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, filePath)));

        player.sendSystemMessage(message);
    }

    // Custom TextColor class for specific RGB values
    // Using ChatFormatting.BLUE is also an option, but this gives more control
    private static class ChatColor {
        public static final TextColor GREEN = TextColor.fromRgb(0x00FF00); // Pure Green
        public static final TextColor BLUE = TextColor.fromRgb(0x0000FF); // Pure Blue
        // You can add more custom colors here
    }

}
