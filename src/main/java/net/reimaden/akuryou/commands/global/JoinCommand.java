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

import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.managers.AudioManager;
import net.reimaden.akuryou.util.VCHelper;

import java.util.List;

public class JoinCommand {

    private static final String name = "join";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Join the voice channel."));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            if (!VCHelper.userIsInChannel(event)) {
                event.reply("You need to be in a voice channel to use this command!").setEphemeral(true).queue();
                return;
            }

            if (!VCHelper.isInSameChannel(event)) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                try {
                    audioManager.openAudioConnection(memberChannel);
                    event.reply(":thumbsup:").queue();
                } catch (InsufficientPermissionException e) {
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                }
            } else {
                event.reply("I am already in the same voice channel.").setEphemeral(true).queue();
            }
        }
    }
}
