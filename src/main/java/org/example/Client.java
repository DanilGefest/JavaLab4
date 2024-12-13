package org.example;

import lombok.Data;

@Data
public class Client {
    private String name;
    private String surname;
    private String phone;
    private boolean subscribed;
    private Book[] favoriteBooks;
}
