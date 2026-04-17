package com.github.ezframework.jakerexample;

import com.github.ezframework.jaker.Jaker;

import java.util.Locale;

public class Demo {
    public static void main(String[] args) {
        Jaker jaker = Jaker.builder().locale(Locale.forLanguageTag("en-US")).seed(42L).build();
        System.out.println("Name: " + jaker.faker().name().firstName());
        System.out.println("Email: " + jaker.faker().internet().email());
        System.out.println("Email (custom local/domain): " + jaker.faker().internet().email("qa.user", "example.org"));
        System.out.println("Company: " + jaker.faker().company().companyName());
        System.out.println("City: " + jaker.faker().address().city());
        System.out.println("CreditCard: " + jaker.faker().finance().creditCardNumber());
        System.out.println("IBAN: " + jaker.faker().bank().iban());
        System.out.println("IBAN (unsafe generated): " + jaker.faker().bank().unsafe().iban());
        System.out.println("Sentence: " + jaker.faker().text().sentence(8));

        // New providers: Person and Number
        System.out.println("Person: " + jaker.faker().person().firstname() + " " + jaker.faker().person().lastname());
        System.out.println("DOB: " + jaker.faker().person().dob());
        System.out.println("UUID: " + jaker.faker().person().uuid());
        System.out.println("Created At: " + jaker.faker().person().createdTimestamp());
        System.out.println("Updated At: " + jaker.faker().person().updatedTimestamp());
        System.out.println("Source: " + jaker.faker().person().source());
        System.out.println("Phone (safe): " + jaker.faker().person().phone());
        System.out.println("Phone (unsafe): " + jaker.faker().person().unsafe().phone());
        System.out.println("Random 1-100: " + jaker.faker().number().numberBetween(1, 100));
        System.out.println("Random double: " + jaker.faker().number().random());
        System.out.println("Random int < 1000: " + jaker.faker().number().random(1000));
        System.out.println("Random double 10-20: " + jaker.faker().number().random(10.0, 20.0));
        System.out.println("Date in last year: " + jaker.faker().dateTime().isoDate(1));

        // Infra/data providers
        System.out.println("Server host: " + jaker.faker().server().hostname());
        System.out.println("Server IPv4: " + jaker.faker().server().ip());
        System.out.println("Server IPv6: " + jaker.faker().server().ip(true));
        System.out.println("File name: " + jaker.faker().file().name());
        System.out.println("File extension: " + jaker.faker().file().extension());
        System.out.println("File size (bytes): " + jaker.faker().file().size(1024, 4096));
        System.out.println("Game server: " + jaker.faker().gameServer().game() + "@" + jaker.faker().gameServer().ip());
        System.out.println("Fruit: " + jaker.faker().fruit().name() + " (" + jaker.faker().fruit().category() + ")");
        System.out.println("Animal: " + jaker.faker().animal().name() + " (" + jaker.faker().animal().gender() + ")");
        System.out.println("Country: " + jaker.faker().country().name() + " [" + jaker.faker().country().code() + "]");

        // Unique examples
        System.out.println("Unique first name #1: " + jaker.faker().unique().name().firstName());
        System.out.println("Unique first name #2: " + jaker.faker().unique().name().firstName());
    }
}
