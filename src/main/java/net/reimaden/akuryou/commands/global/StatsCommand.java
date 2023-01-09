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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.reimaden.akuryou.Akuryou;

import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StatsCommand {

    private static final String name = "stats";

    public static void register(List<CommandData> commandData) {
        commandData.add(Commands.slash(name, "Display some bot statistics."));
    }

    @SuppressWarnings("DataFlowIssue")
    public static void run(SlashCommandInteractionEvent event) {
        if (event.getName().equals(name)) {
            JDA jda = event.getJDA();
            String botAvatar = jda.getSelfUser().getEffectiveAvatarUrl();
            // If hosting yourself, replace with your own user ID
            User manager = jda.getUserById(211487513804668930L);

            DecimalFormat df = new DecimalFormat("#.##");
            double maxMemory = bytesToMb(Runtime.getRuntime().maxMemory());
            double memoryUsage = bytesToMb(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
            String formattedMemory = "```" + df.format(memoryUsage) + " MB / " + df.format(maxMemory) + " MB```";
            JDA.ShardInfo shardInfo = jda.getShardInfo();
            int currentShard = shardInfo.getShardId();
            int totalShards = shardInfo.getShardTotal();
            String shardString = "```" + currentShard + " / " + totalShards + "```";
            long totalServers = jda.getGuilds().size();
            String serverString = "```In " + totalServers + " servers```";
            long restPing = jda.getRestPing().complete();
            long gatewayPing = jda.getGatewayPing();
            String latency = "```" + restPing + " ms | " + gatewayPing + " ms```";
            long currentTime = System.currentTimeMillis();
            long uptime = currentTime - Akuryou.TIME;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(uptime);
            long hours = TimeUnit.MILLISECONDS.toHours(uptime);
            long days = TimeUnit.MILLISECONDS.toDays(uptime);
            String formattedUptime = "```" + String.format("%d days, %d hours, %d minutes", days, hours, minutes) + "```";
            String jdaVersion = "```v" + JDAInfo.VERSION_MAJOR + "." + JDAInfo.VERSION_MINOR + "." + JDAInfo.VERSION_REVISION + "-" + JDAInfo.VERSION_CLASSIFIER + "```";
            String os = System.getProperty("os.name");
            String kernel = System.getProperty("os.version");
            String host = "```" + os + " " + kernel + "```";

            String version = Akuryou.getProperties().getProperty("version");

            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("__**Akuryou Stats:**__");
            embed.setDescription("Instance managed by: **" + manager.getAsTag() + "**");
            embed.setColor(Akuryou.COLOR);
            embed.addField("Memory Usage", formattedMemory, true);
            embed.addField("Shards", shardString, true);
            embed.addField("Servers", serverString, true);
            embed.addField("Latency (API)", latency, true);
            embed.addField("Uptime", formattedUptime, true);
            embed.addField("JDA Version", jdaVersion, true);
            embed.addField("Host", host, true);
            embed.setFooter("Version: v" + version, botAvatar);

            event.replyEmbeds(embed.build()).queue();
        }
    }

    private static double bytesToMb(long bytes) {
        return bytes / 1048576.0;
    }
}
