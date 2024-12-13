package org.example;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/books.json";
        String jsonContent = Files.readString(Paths.get(filePath));

        Gson gson = new Gson();
        Client[] clientList = gson.fromJson(jsonContent, Client[].class);

        long count = Stream.of(clientList).count();
        System.out.println("1)clients");
        System.out.println(Arrays.toString(clientList));
        System.out.println("count: " + count);

        Map<String, Long> books = Arrays.stream(clientList)
                .flatMap(c -> Arrays.stream(c.getFavoriteBooks()))
                .collect(Collectors.groupingBy(Book::getName, Collectors.counting()));
        System.out.println("\n2)books: " + books);

        List<Book> sortBooks = Arrays.stream(clientList)
                .flatMap(c -> Arrays.stream(c.getFavoriteBooks()))
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .toList();

        System.out.println("\n3)sort books: ");
        for (Book book : sortBooks) {
            System.out.println(book.getName() + " " + book.getPublishingYear());
        }

        List<Client> clientsWithFilter = Arrays.stream(clientList)
                .filter(c -> Arrays.stream(c.getFavoriteBooks()).anyMatch(b -> b.getAuthor().equalsIgnoreCase("Jane Austen")))
                .toList();
        System.out.println("\n4)clients with Jane Austen: ");
        clientsWithFilter.forEach(System.out::println);

        Optional<Client> clientWithMaxBooks = Arrays.stream(clientList)
                .max(Comparator.comparingInt(client -> client.getFavoriteBooks().length));

        if (clientWithMaxBooks.isPresent()) {
            System.out.println("\n5) client with max books: " + clientWithMaxBooks.get().getName());
        } else {
            System.out.println("\n5) client with max books not exist");
        }

        System.out.println("\n6) group clients:");
        double averageBookCount = Arrays.stream(clientList)
                .mapToInt(client -> client.getFavoriteBooks().length)
                .average().orElse(-1);

        int roundAvgCount = (int) Math.round(averageBookCount);

        List<Client> lessThenAvg = Arrays.stream(clientList)
                .filter(Client::isSubscribed)
                .filter(c -> c.getFavoriteBooks().length <= roundAvgCount).toList();

        List<Client> moreThenAvg = Arrays.stream(clientList)
                .filter(Client::isSubscribed)
                .filter(c -> c.getFavoriteBooks().length >= roundAvgCount).toList();

        List<Client> equalThenAvg = Arrays.stream(clientList)
                .filter(Client::isSubscribed)
                .filter(c -> c.getFavoriteBooks().length == roundAvgCount).toList();

        String moreSmsText = "you are a bookworm";
        String lessSmsText = "read more";
        String equalSmsText = "fine";

        for (Client clinet : lessThenAvg) {
            Sms sms = new Sms(clinet.getPhone(), lessSmsText);
            System.out.println(sms);
        }

        for (Client clinet : equalThenAvg) {
            Sms sms = new Sms(clinet.getPhone(), equalSmsText);
            System.out.println(sms);
        }

        for (Client clinet : moreThenAvg) {
            Sms sms = new Sms(clinet.getPhone(), moreSmsText);
            System.out.println(sms);
        }
    }
}