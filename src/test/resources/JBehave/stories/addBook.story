Meta:
@add-book

Scenario: Add a new book
Given the bookstore is empty
When I add a book with ISBN 77777, title "Hyrule Warriors", author "Kyle Conrad", publisher "Link Inc.", description "A thrilling adventure of a young Kokiri", inventory 10, price 19.99, page count 300, series "Hyrule Compendium"
Then the book with ISBN 77777 should exist in the system