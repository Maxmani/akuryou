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
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import net.reimaden.akuryou.lavaplayer.PlayerManager;
import net.reimaden.akuryou.util.VCHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand {

    private static final String name = "play";
    private static final String option1 = "track";

    public static void register(List<CommandData> commandData) {
        OptionData url = new OptionData(OptionType.STRING, option1, "The link or name of the audio track you want to play.").setRequired(true);
        commandData.add(Commands.slash(name, "Play a music track.")
                .addOptions(url));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            OptionMapping url = event.getOption(option1);
            String trackName = url != null ? url.getAsString() : null;

            if (!event.getMember().getVoiceState().inAudioChannel()) {
                event.reply("You need to be in a voice channel to play music!").setEphemeral(true).queue();
                return;
            } else if (VCHelper.isInChannel(event) && !VCHelper.isInSameChannel(event)) {
                event.reply("You need to be in the same voice channel to use this command!").setEphemeral(true).queue();
                return;
            }

            if (!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                try {
                    audioManager.openAudioConnection(memberChannel);
                } catch (InsufficientPermissionException e) {
                    event.reply(e.getMessage()).setEphemeral(true).queue();
                    return;
                }
            }

            String link = String.join(" ", trackName);

            if (!isUrl(link)) {
                link = "ytsearch:" + link + " audio";
            }

            PlayerManager.getINSTANCE().loadAndPlay(event, link);
        }
    }

    private static boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
