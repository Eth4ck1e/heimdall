package com.bifrostsmp.heimdall.discord.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.util.Random;

public class Join extends ListenerAdapter {
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TextChannel channel = event.getGuild().getTextChannelsByName("howdy", true).get(0);
        Guild guild = event.getGuild();
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
                "Hey [member]. What's in the bag?"
        };
        Random rand = new Random();
        int number = rand.nextInt(greeting.length);

        EmbedBuilder join = new EmbedBuilder();
        join.setColor(Color.yellow);
        join.setDescription(greeting[number].replace("member",event.getMember().getAsMention()));

        //TODO add the roles to be added to players when they join
        channel.sendMessageEmbeds(join.build()).queue();
        guild.addRoleToMember(member, guild.getRolesByName("applicant", true).get(0)).queue();
    }
}
