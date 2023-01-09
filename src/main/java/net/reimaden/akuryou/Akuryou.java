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

package net.reimaden.akuryou;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.reimaden.akuryou.commands.CommandManager;
import net.reimaden.akuryou.listeners.EventListeners;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

@SuppressWarnings("unused")
public class Akuryou {

    public static final Color COLOR = new Color(1, 1, 255);
    public static final long TIME = System.currentTimeMillis();

    private final Dotenv config;
    private final ShardManager shardManager;
    private final String[] activities = {
            "End of Daylight", "Complete Darkness", "Reincarnation", "Innocent Treasures"
    };
    private static final Properties properties = new Properties();

    public Akuryou() {
        config = Dotenv.configure().load();
        String token = config.get("TOKEN");
        int randomIndex = new Random().nextInt(activities.length);
        String activity = activities[randomIndex];

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.listening(activity));
        builder.enableCache(CacheFlag.VOICE_STATE);
        shardManager = builder.build();

        // Register listeners
        shardManager.addEventListener(new EventListeners(), new CommandManager());
    }

    public static void main(String[] args) {
        new Akuryou();
    }

    public Dotenv getConfig() {
        return config;
    }

    public ShardManager getShardManager() {
        return shardManager;
    }

    public String[] getActivities() {
        return activities;
    }

    public static Properties getProperties() {
        try {
            properties.load(Akuryou.class.getClassLoader().getResourceAsStream("project.properties"));
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
