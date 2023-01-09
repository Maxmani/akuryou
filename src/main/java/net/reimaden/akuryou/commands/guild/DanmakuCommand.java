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

package net.reimaden.akuryou.commands.guild;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.FileUpload;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.reimaden.akuryou.Akuryou;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class DanmakuCommand {

    private static final String name = "danmaku";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Hop on Danmaku!!"));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            Role danmaku = event.getGuild().getRoleById(998607407485816922L);

            if (event.getGuild().getRoles().contains(danmaku)) {
                if (event.getMember().getRoles().contains(danmaku)) {
                    String role = danmaku.getAsMention();
                    File gif = new File(Akuryou.class.getClassLoader().getResource("danmaku.gif").getFile());

                    byte[] imageData;
                    try {
                        imageData = Files.readAllBytes(gif.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    MessageCreateBuilder builder = new MessageCreateBuilder();
                    builder.addContent(role);
                    builder.addFiles(FileUpload.fromData(imageData, "danmaku.gif"));

                    event.reply(builder.build()).queue();
                } else {
                    event.reply("You need the " + danmaku.getAsMention() + " role to use this command!").setEphemeral(true).queue();
                }
            } else {
                event.reply("Unable to find the **Danmaku!!** role!").setEphemeral(true).queue();
            }
        }
    }
}
