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

package net.reimaden.akuryou.util;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class ImageConverter {

    public static void convertUrl(SlashCommandInteractionEvent event, String url, String outputFormat) {
        try {
            BufferedImage image = ImageIO.read(new URL(url));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, outputFormat, baos);
            byte[] imageData = baos.toByteArray();
            event.deferReply().queue();
            event.getHook().sendFiles(FileUpload.fromData(imageData, "converted." + outputFormat)).queue();
        } catch (IOException e) {
            event.deferReply().setEphemeral(true).queue();
            event.getHook().sendMessage("Error converting image: " + e.getMessage()).queue();
        }
    }

    public static void convertAttachment(SlashCommandInteractionEvent event, Message.Attachment attachment, String outputFormat) {
        try {
            BufferedImage image = ImageIO.read(new URL(attachment.getUrl()));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, outputFormat, baos);
            byte[] imageData = baos.toByteArray();
            event.deferReply().queue();
            event.getHook().sendFiles(FileUpload.fromData(imageData, "converted." + outputFormat)).queue();
        } catch (IOException e) {
            event.deferReply().setEphemeral(true).queue();
            event.getHook().sendMessage("Error converting image: " + e.getMessage()).queue();
        }
    }
}
