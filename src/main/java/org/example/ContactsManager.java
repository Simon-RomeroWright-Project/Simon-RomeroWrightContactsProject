package org.example;

import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import java.util.*;

public class ContactsManager {
    private static Scanner scanner = new Scanner(System.in);
    private static Path contactsFile = Paths.get("contacts.txt");

    public static void main(String[] args) {
        if (!Files.exists(contactsFile)) {
            try {
                Files.createFile(contactsFile);
            } catch (IOException e) {
                System.out.println("Unable to create contacts file: " + e.getMessage());
            }
        }
        boolean running = true;
        while (running) {
            System.out.println("Choose from the following options: \n1. Show all contacts\n2. Add a new contact\n3. Search a contact by name\n4. Delete an existing contact\n5. Exit ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    showContacts();
                    break;
                case 2:
                    addContact();
                    break;
                case 3:
                    searchContacts();
                    break;
                case 4:
                    deleteContact();
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Selection is not valid");
            }
        }
    }


private static void showContacts() {
    try {
        List<String> lines = Files.readAllLines(contactsFile);
        if (lines.isEmpty()) {
            System.out.println("No contacts found");
        } else {
            System.out.println("Name  |  Phone number");
            System.out.println("---------------------");
            for (String line : lines) {
                String [] parts = line.split(",");
                System.out.println(parts[0] + "  |  " + parts[1]);
            }
        }
    } catch (IOException e) {
        System.out.println("Unable to read contacts file: " + e.getMessage());
    }
}

    private static void addContact() {
        System.out.print("Please enter the name: ");
        String name = scanner.nextLine();
        System.out.print("Please enter the phone number: ");
        String phoneNumber = scanner.nextLine();

        boolean contactAlreadyExists = false;
        try {
            List<String> lines = Files.readAllLines(contactsFile);
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts[0].equalsIgnoreCase(name)) {
                    contactAlreadyExists = true;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read contacts file: " + e.getMessage());
            return;
        }
        if (contactAlreadyExists) {
            System.out.println("There is already a contact named " + name + ". Do you want to overwrite it? (Yes/No)");
            String overwrite = scanner.nextLine();
            if (!overwrite.equalsIgnoreCase("yes")) {
                System.out.println("Contact not added.");
                return;
            }
        }

        String contactString = name + "," + phoneNumber;
        try {
            Files.write(contactsFile, (contactString + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Contact added: " + contactString);
        } catch (IOException e) {
            System.out.println("Unable to write to contacts file: " + e.getMessage());
        }
    }
private static void searchContacts() {
    System.out.print("Please enter the name you are searching for: ");
    String name = scanner.nextLine();
    try {
        List<String> lines = Files.readAllLines(contactsFile);
        boolean found = false;
        for (String line : lines) {
            String [] parts = line.split(",");
            if (parts[0].equalsIgnoreCase(name)) {
                System.out.println(line);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No such contact named" + name);
        }
    } catch (IOException e) {
        System.out.println("Unable to read file: " + e.getMessage());
    }
}

private static void deleteContact(){
        System.out.print("Enter the name of the contact to delete");
        String name = scanner.nextLine();
        try{
            List<String> lines =
                    Files.readAllLines(contactsFile);
            boolean found = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[0].equalsIgnoreCase(name)) {
                    lines.remove(i);
                    found = true;
                    break;
                }
            }
            if (found) {
                Files.write(contactsFile, lines, StandardCharsets.UTF_8);
                System.out.println("Contact deleted:" + name);
            } else {
                System.out.println("No contact found with name " + name);
            }
            } catch (IOException e) {
                System.out.println("Error reading or writing to contacts file: " + e.getMessage());
            }
        }
    }
