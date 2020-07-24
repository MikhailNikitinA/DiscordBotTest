package com.nikitin.DiscordBot.command;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestClass {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        list.add("213");
        list.add("523");

        List<String> collect = Stream.of("152", "523").peek(s -> list.add("111")).collect(Collectors.toList());

        System.out.println(list);
        System.out.println(collect);


    }
}
