
Scenario: Delete a book
Given the bookstore is not empty
When I delete a book with ISBN 77777
Then the book with ISBN 77777 should no longer exist in the system