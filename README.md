# Library Management System

A simple console-based library management program written in Java.

## Features

- Add, list, search (by title or author) and remove books
- Add and list members (students and staff)
- Lend and return books
- Borrowing limits by member type (Student: 3, Staff: 5)
- Data is saved to `books.txt` and `members.txt` and loaded back on startup

## Concepts Used

| Concept | Where |
|---|---|
| Classes and objects, encapsulation (private fields + getters) | `Book`, `Member` |
| Abstract class and inheritance | `Member` → `Student`, `Staff` |
| Polymorphism, `@Override` | `getMaxBooks()`, `getType()`, `toString()` |
| `ArrayList` | `Library` |
| Custom exception, `try-catch`, `throws` | `LibraryException` |
| File reading / writing | `Library.save()`, `Library.load()` |
| User input with `Scanner`, `switch` menu | `Main` |

## Class Structure

```
Main ──uses──> Library ──contains──> ArrayList<Book>
                          └────────> ArrayList<Member>
                                             ▲
                                     ┌───────┴───────┐
                                  Student          Staff
```

## How to Run

```bash
javac -encoding UTF-8 -d out src/*.java
cd out
java Main
```

Requires Java 11 or later. You can also open the `src` folder in IntelliJ or Eclipse and run the `Main` class.

## Example Usage

1. Press `5` to add a student
2. Press `1` to add a few books
3. Press `7` and enter a book ID and member ID to lend a book
4. Press `9` to see a member's books
5. Press `0` to save and exit; your data will be there when you restart the program
