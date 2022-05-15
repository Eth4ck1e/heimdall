package com.bifrostsmp.heimdall.discord.events;

import com.bifrostsmp.heimdall.config.ConfigParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Random;

public class Join extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(ConfigParser.getHowdyChannel());
        Member member = event.getMember();
        String[] greeting = {
                "[member] just joined the server - glhf!",
                "[member] is here. Everyone, look busy!",
                "[member] just joined. Can I get an elytra?",
                "[member] joined the party.",
                "[member] joined. Get another Shop plot ready",
                "Ermagherd. [member] is here!",
                "Welcome, [member]. Stay awhile.",
                "Welcome [member]. We were expecting you ( ͡° ͜ʖ ͡°)",
                "Welcome [member]. We hope you brought pizza...",
                "Welcome [member]. Keep your diamonds close.",
                "[member] just joined. Hide your diamonds.",
                "Whats up [member].  Make sure to read the rules!",
                "Surprise! False alarm guys, it's only [member]",
                "Greetings [member].",
                "Is that [member]? Anyone know [member]?",
                "Hello [member], business or pleasure?",
                "Hey [member]. What's in the bag?",
                "Hey, [member] let's do \"get help\"!",
                "Oh no. [member] is here. Or is it Loki in disguise?",
                "Heimdall, summon the Bifrost. [member] is here!",
                "Welcome, [member]. Watch your step on the rainbow bridge.",
                "The Allfather greets you, [member], *whisper* this the where you bow...",
                "Frigg predicted your arrival, [member]. Welcome.",
                "Welcome, traveller [member]. May Brage soon tell your tale.",
                "Oh hey [member]! We were just talking about you.",
                "A new hand touches the beacon. Welcome [member]!",
                "New challenger approaching! [member] smashes onto the server",
                "Ohgodohfuck it’s [member]. Act natural.",
                "[member]’s here to chew gum and kick butt. They still got a lot of gum left tho",
                "[Member] is here to kick bubblegum and chew ass...and we're all out of bubblegum...\nwait..."
        };
        Random rand = new Random();
        int number = rand.nextInt(greeting.length);

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(Color.yellow);
        join.setDescription(greeting[number].replace("[member]", member.getAsMention()));

        channel.sendMessageEmbeds(join.build()).queue();
    }
}
