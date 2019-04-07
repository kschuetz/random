package dev.marksman.composablerandom.examples;

import dev.marksman.composablerandom.examples.components.Person;

import static dev.marksman.composablerandom.GeneratedStream.streamFrom;
import static dev.marksman.composablerandom.examples.components.Person.generatePerson;

public class PersonExample {

    public static void main(String[] args) {
        streamFrom(generatePerson().fmap(Person::pretty)).next(100).forEach(System.out::println);
    }
}
