
package Array;

import java.util.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

    class Book {
        int id;
        String title;
        String author;

        Book(int id, String title, String author) {
            this.id = id;
            this.title = title;
            this.author = author;
        }

        @Override
        public String toString() {
            return "Book ID: " + id + ", Title: " + title + ", Author: " + author;
        }
    }

    class BorrowedBook {
        Book book;
        LocalDate borrowDate;
        LocalDate dueDate;
        String studentEmail;

        BorrowedBook(Book book, LocalDate borrowDate, LocalDate dueDate, String studentEmail) {
            this.book = book;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.studentEmail = studentEmail;
        }
    }

    class Library {
        List<Book> books;
        Map<Integer, BorrowedBook> borrowedBooks;

        Library() {
            books = new ArrayList<>();
            borrowedBooks = new HashMap<>();
        }

        void addBook(int id, String title, String author) {
            books.add(new Book(id, title, author));
            System.out.println("Book added successfully.");
        }

        void removeBook(int id) {
            books.removeIf(book -> book.id == id);
            System.out.println("Book removed successfully.");
        }

        void displayBooks() {
            if (books.isEmpty()) {
                System.out.println("No books available in the library.");
            } else {
                books.forEach(System.out::println);
            }
        }

        void borrowBook(int bookId, LocalDate borrowDate, String studentEmail) {
            books.stream().filter(book -> book.id == bookId).findFirst().ifPresentOrElse(book -> {
                LocalDate dueDate = borrowDate.plusDays(7);
                borrowedBooks.put(bookId, new BorrowedBook(book, borrowDate, dueDate, studentEmail));
                System.out.println("Book borrowed successfully. Due date is " + dueDate);
            }, () -> System.out.println("Book with ID " + bookId + " not found."));
        }

        void returnBook(int bookId, LocalDate returnDate) {
            BorrowedBook borrowedBook = borrowedBooks.get(bookId);
            if (borrowedBook == null) {
                System.out.println("No borrowed book found with ID " + bookId);
                return;
            }

            long daysLate = ChronoUnit.DAYS.between(borrowedBook.dueDate, returnDate);
            if (daysLate > 0) {
                System.out.println("Book returned late by " + daysLate + " days. Late fee: Rs. " + (daysLate * 10));
            } else {
                System.out.println("Book returned on time. No late fee.");
            }

            borrowedBooks.remove(bookId);
        }
    }

    class NotificationSystem {
        static void scheduleNotifications(Map<Integer, BorrowedBook> borrowedBooks) {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    LocalDate today = LocalDate.now();
                    for (BorrowedBook borrowedBook : borrowedBooks.values()) {
                        if (ChronoUnit.DAYS.between(today, borrowedBook.dueDate) == 1) {
                            System.out.println("Reminder: Book '" + borrowedBook.book.title + "' is due tomorrow for " + borrowedBook.studentEmail);
                        }
                    }
                }
            }, 0, 86400000); // Check every 24 hours
        }
    }

    public class project {
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            Library library = new Library();

            library.addBook(101, "The Great Gatsby", "F. Scott Fitzgerald");
            library.addBook(102, "1984", "George Orwell");
            library.addBook(103, "To Kill a Mockingbird", "Harper Lee");

            System.out.print("Enter Student Email to access the Library System: ");
            String studentEmail = scanner.nextLine();

            System.out.println("Welcome to the Library System!");

            while (true) {
                System.out.println("\nLibrary Management System");
                System.out.println("1. Add Book");
                System.out.println("2. Remove Book");
                System.out.println("3. Display Books");
                System.out.println("4. Borrow Book");
                System.out.println("5. Return Book");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.print("Enter Book ID: ");
                        int id = scanner.nextInt();
                        scanner.nextLine();
                        System.out.print("Enter Book Title: ");
                        String title = scanner.nextLine();
                        System.out.print("Enter Book Author: ");
                        String author = scanner.nextLine();
                        library.addBook(id, title, author);
                        break;
                    case 2:
                        System.out.print("Enter Book ID to remove: ");
                        int removeId = scanner.nextInt();
                        library.removeBook(removeId);
                        break;
                    case 3:
                        library.displayBooks();
                        break;
                    case 4:
                        System.out.print("Enter Book ID to borrow: ");
                        int borrowId = scanner.nextInt();
                        library.borrowBook(borrowId, LocalDate.now(), studentEmail);
                        break;
                    case 5:
                        System.out.print("Enter Book ID to return: ");
                        int returnId = scanner.nextInt();
                        library.returnBook(returnId, LocalDate.now());
                        break;
                    case 6:
                        System.out.println("Exiting system...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice, please try again.");
                }
            }
        }
    }

