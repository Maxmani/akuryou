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

package net.reimaden.akuryou.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.reimaden.akuryou.commands.global.*;
import net.reimaden.akuryou.commands.guild.DanmakuCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        // Global commands
        PingCommand.run(event);
        WebpCommand.run(event);
        ConvertCommand.run(event);
        SourceCommand.run(event);
        PlayCommand.run(event);
        LeaveCommand.run(event);
        StatsCommand.run(event);
        JoinCommand.run(event);

        // Guild commands
        DanmakuCommand.run(event);
    }

    // Register guild commands
    private void registerGuildCommands(Guild guild) {
        List<CommandData> commandData = new ArrayList<>();
        Guild reimaden = guild.getJDA().getGuildById(695307472495443971L);

        // Register commands here
        if (guild.equals(reimaden)) {
            DanmakuCommand.register(commandData);
        }

        guild.updateCommands().addCommands(commandData).queue();
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        registerGuildCommands(event.getGuild());
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        registerGuildCommands(event.getGuild());
    }

    @Override // Register global commands
    public void onReady(ReadyEvent event) {
        List<CommandData> commandData = new ArrayList<>();

        // Register commands here
        PingCommand.register(commandData);
        WebpCommand.register(commandData);
        ConvertCommand.register(commandData);
        SourceCommand.register(commandData);
        PlayCommand.register(commandData);
        LeaveCommand.register(commandData);
        StatsCommand.register(commandData);
        JoinCommand.register(commandData);

        event.getJDA().updateCommands().addCommands(commandData).queue();
    }
}
