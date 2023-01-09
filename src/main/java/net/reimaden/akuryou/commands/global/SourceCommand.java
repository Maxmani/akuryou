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

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class SourceCommand {

    private static final String name = "source";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Get a link to Akuryou's source code."));
    }

    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            String url = "https://github.com/Maxmani/akuryou";
            event.reply("My source code can be found here:")
                    .addActionRow(Button.link(url, "GitHub")).setEphemeral(true).queue();
        }
    }
}
