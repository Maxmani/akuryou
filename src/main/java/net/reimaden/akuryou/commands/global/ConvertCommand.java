/*
 * Copyright (C) 2023 Maxmani
 *
 * This file is part of Akuryou.
 *
 * Akuryou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Akuryou is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Akuryou.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.reimaden.akuryou.commands.global;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.reimaden.akuryou.util.ImageConverter;

import java.util.List;

public class ConvertCommand {

    private static final String name = "convert";
    private static final String option1 = "url";
    private static final String option2 = "file";
    private static final String option3 = "format";

    public static void register(List<CommandData> commandData) {
        OptionData url = new OptionData(OptionType.STRING, option1, "The URL of the image you want to convert.");
        OptionData file = new OptionData(OptionType.ATTACHMENT, option2, "The image you want to convert.");
        OptionData format = new OptionData(OptionType.STRING, option3, "The file format you want. Ignore for PNG.")
                .addChoice("PNG", "png")
                .addChoice("JPG", "jpg")
                .addChoice("GIF", "gif")
                .addChoice("WebP", "webp");
        commandData.add(Commands.slash(name, "Convert image files to other formats.")
                .addOptions(url, file, format));
    }

    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            OptionMapping url = event.getOption(option1);
            OptionMapping file = event.getOption(option2);
            OptionMapping format = event.getOption(option3);

            String fileUrl = url != null ? url.getAsString() : "";
            Message.Attachment attachment = file != null ? file.getAsAttachment() : null;

            String outputFormat;
            if (format != null) {
                outputFormat = format.getAsString();
            } else {
                outputFormat = "png";
            }

            // Make sure there's exactly one input
            if (isBoth(fileUrl, attachment)) {
                event.reply("Can't have both a URL and an attachment!").setEphemeral(true).queue();
                return;
            } else if (isNeither(fileUrl, attachment)) {
                event.reply("Provide either a URL or an attachment!").setEphemeral(true).queue();
                return;
            }

            if (!fileUrl.isEmpty()) {
                if (fileUrl.matches("^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")) {
                    if (fileUrl.matches("^.*\\.(?i)(jpg|jpeg|png|gif|webp)$")) {
                        ImageConverter.convertUrl(event, fileUrl, outputFormat);
                    } else {
                        event.reply("No valid image found!").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("Not a valid URL!").setEphemeral(true).queue();
                }
            }
            if (attachment != null) {
                if (attachment.isImage()) {
                    String fileExtension = attachment.getFileExtension();
                    if (fileExtension != null) {
                        ImageConverter.convertAttachment(event, attachment, outputFormat);
                    } else {
                        event.reply("No image found!").setEphemeral(true).queue();
                    }
                } else {
                    event.reply("Not a valid image attachment!").setEphemeral(true).queue();
                }
            }
        }
    }

    private static boolean isBoth(String fileUrl, Message.Attachment attachment) {
        return !fileUrl.isEmpty() && attachment != null;
    }

    private static boolean isNeither(String fileUrl, Message.Attachment attachment) {
        return fileUrl.isEmpty() && attachment == null;
    }
}
