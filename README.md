# Not Just Any Bookstore
Contributors: John Guo, Emmanuel Adokoya, Amar Saini, Kaitlyn Conron, and Kaiya Sparks
## Overall Objective:
- To create a functional bookstore where a owner is able to add a new book to a store with a specified inventory.
- Enable a user to be able to efficiently browse a bookstore and find their next great read.
- Give recommendations based on previous purchases to users to enhance their experience.
### Current Workflow
[![Java CI with Maven](https://github.com/KSparks04/Online-Bookstore/actions/workflows/maven.yml/badge.svg)](https://github.com/KSparks04/Online-Bookstore/actions/workflows/maven.yml)
## Deployment Link 🔗:
https://notjustanybook-h3eugehua3h9dmcv.canadacentral-01.azurewebsites.net/ 

## Sprint 1 (October 20th - November 3rd)
**Objectives**
  - Set up simple functionality, i.e. take a book name, author, etc. and display it.
    - Includes:
      - Creating a book model and repository to store uploaded books
      - Basic html pages to complement features such as editing book information and displaying list of books in the store
  - Developing comprehensive test cases for the controllers to test endpoints
  - Set up the azure deployment and CI
## Sprint 2 (November 3rd - November 17th)
**Objectives**
  - Develop a user model and repository to store relevant information
    - Includes: User registration and log in page
  - Adding a checkout page
    - Includes: Displaying a list of books with their prices and quantity, total price of cart and checkout button
  - Adding purchase functionality
    - Includes: providing a button on a books details page to add to cart/purchase
  - Research on using Jaccard to develop a recommendations feature
  - Update the book model to feature new attributes such as page length and genre 

# Planned Application
## Bookstore Web App
Gives Bookstore owners an intuitive way to sell books online while maximizing user experience.
## Bookstore Owner
Bookstore owner looking to set up an online bookstore to sell books ranging various genres.
### Things an owner can do
- Can upload and edit Book information (ISBN, Title, Author, Description, Picture, Publisher, ...),
- Add and adjust price and inventory of books
  
## Users
People looking to browse and purchase books from the bookstore.
### Things a user can do
- A user can create an account,
- search for books available on the bookstore website, 
- sort for specific attributes like genre, rating and price, 
- add books to checkout, 
- purchase books in checkout, 
- receive recommendations based on past purchases
## Book
Printed work of fiction or non-fiction.
For this application, the simulated books is paperback or hardback.
  ### Consists of:
  - ISBN
  - Title
  - Author
  - Publisher
  - Book Cover
  - Description
  - Inventory
  - Price
  - Reviews
  - Genre
