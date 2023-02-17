## Product Section

- On the user side, users interact with the application, in which they could view, and register for an account and buy the products.

### Overview

- The interface is designed for flexibility in end-users experiences with Revel sites. Visitors and customers can search for products in different ways, including searching by categories, titles, and products descriptions. Finally, each product would have their own page, which includes all of their information.


<div align="center">
  <img width="720" alt="Homepage" src="https://user-images.githubusercontent.com/109766527/219012270-b01b9422-d59b-407b-851b-839861d2ee5c.png">
</div>

- In the homepage, there are categories of products, a left-side bar for navigation, top-right corner buttons for notifications and user registration, and at the bottom the website additional information. Those are universally configured for this side of the app.

- The foundational template is loaded by the MainController, while the components templates such as the sidebar, search box, header, footer, and page errors are organized using thymeleaf fragments.

#### Product Listing

- Products and categories are filtered in order to only display the proper ones using custom query in the repository layer.

- When searching through the categories, products are divided into sub-categories. They and their categories hierarchy are represented properly by specifying their parents and chilren alias and ids.

<div align="center">
  <img width="720" alt="Products by Categories" src="https://user-images.githubusercontent.com/109766527/219093526-f6a387e3-40ad-42f4-8f9e-6e0157cb1c6a.png">
</div>

- Using thymeleaf, the hierarchy is demonstrated by the breadcrumb below the header from the current category way up to the root category. Each node is also a category, which displays its sub-categories and their products.

<div align="center">
  <img width="720" alt="Parents Category and Pagination" src="https://user-images.githubusercontent.com/109766527/219097172-b1736339-2301-48a0-aca3-aac7597576c7.png">
</div>

- Pagination at the bottom of the page used through out the app also uses the number of products, pre-defined size to organized products in pages using `PagingAndSortingRepository` and loaded by thymeleaf fragments. The proper connections between local modules and dicrectory paths are configured using `WebMvcConfigurer`.

- For each products, their information are displayed in individual pages, also loaded by their alias first, then displayed using thymeleaf.

<div align="center">
  <img width="720" alt="Parents Category and Pagination" src="https://user-images.githubusercontent.com/109766527/219109604-7c0601b6-8002-429f-b9a2-1a0b2366410b.png">
</div>

- The searching function uses the same methodology - has it searched using custom query and finally loaded to show the search results

<div align="center">
  <img width="720" alt="Parents Category and Pagination" src="https://user-images.githubusercontent.com/109766527/219111161-a641422f-3e07-4279-9c97-f9e9fd47a124.png">
</div>

- Overview class diagram for the Product Listing (only the means of representing methods and their connections, but not relationships)

```mermaid
classDiagram
  class Product {
    -id
    -name
    -alias
  }

  class ProductRepository {
    <<interface>>
    +listByCategory()
    +findByAlias()
    +search()
  }

  class ProductService {
    -productRepository

    +listByCategory()
    +getProduct()
    +search()
  }

  class ProductController {
    -productService
    -categoryService

    +viewProductDetail()
    +viewCategory()
    +search()
  }

  ProductController --> ProductService
  ProductService --> ProductRepository
  ProductRepository --> Product

  class Category {
    -id
    -name
    -alias
  }

  class CategoryRepository {
    <<interface>>
    +findAllEnabled()
    +findByAliasEnabled()
  }

  class CategoryService {
    -categoryRepository

    +listNoChildrenRepository()
    +getCategoryParents()
    +getCategory()
  }

  class MainController {
    -categoryService

    +index
  }

  MainController --> CategoryService
  ProductController --> CategoryService
  CategoryService --> CategoryRepository
  CategoryRepository --> Category

  index --> MainController
  search --> ProductController
  product_detail --> ProductController
  product_by_category --> ProductController
```