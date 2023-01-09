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

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

@SuppressWarnings("DataFlowIssue")
public class VCHelper {

    public static boolean userIsInChannel(SlashCommandInteractionEvent event) {
        return event.getMember().getVoiceState().inAudioChannel();
    }

    public static boolean isInChannel(SlashCommandInteractionEvent event) {
        return event.getGuild().getSelfMember().getVoiceState().inAudioChannel();
    }

    public static boolean isInSameChannel(SlashCommandInteractionEvent event) {
        VoiceChannel userChannel;
        GuildVoiceState userState = event.getMember().getVoiceState();
        if (!userState.inAudioChannel()) {
            return false;
        } else {
            userChannel = userState.getChannel().asVoiceChannel();
        }
        VoiceChannel botChannel;
        GuildVoiceState botState = event.getGuild().getSelfMember().getVoiceState();
        if (!botState.inAudioChannel()) {
            return false;
        } else {
            botChannel = botState.getChannel().asVoiceChannel();
        }

        return userIsInChannel(event) && isInChannel(event) && userChannel == botChannel;
    }
}
