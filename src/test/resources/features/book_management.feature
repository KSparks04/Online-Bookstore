Feature: Book Management
  As an owner
  I want to add and edit books
  So that they appear correctly in the store
  Scenario: Add a new book to the bookstore
    Given I am on the Add Book Form
    When I submit a book with:
      | ISBN | Title | Author | Publisher | Description | Inventory | Price | PageCount | Series | ImageFile |
      | 19393501 | The Maze Runner | James Dashner | Bloomsbury | They have to escape | 25 | 18.99 | 350 | Maze Runner | maze_runner.jpeg |
    Then the book should be saved successfully
    And I should see it on the Book List
  Scenario: Edit an existing book title
    Given a book exists with ISBN "193883501"
    When I change the book title to "The Maze Runner – Special Edition"
    And I save the book
    Then the book title should be updated to "The Maze Runner – Special Edition"