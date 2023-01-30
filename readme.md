# E-Commerce DEMO Project

## To run the app:
```shell
java -jar ecomm-0.0.1-SNAPSHOT.jar
```
the project was built with jdk 17 + spring boot 3.0.2, it's config, app runs on default 8080 port.

## To run tests
```shell
./gradlew  clean test
```
Or if you have your own gradle setup you could also use that.

## End Points/Swagger
For the sake of easier navigation I put swagger in.
```css
http://localhost:8080/swagger-ui/index.html
```
## Users
```css
http://localhost:8080/users
```

## Some notes
### User/customer Management and Security/Authentication
- Due to time constrain, user/customer management and security/authentication is not built
- I created two roles: ADMIN, CUSTOMER and a few users for demo purpose
- But I didn't really build any feature around user/customer managements or authentication
- Role do not anything at this moment
- USER ID is used as entry point for shopping cart, receipt calculation and some other logic temporarily
- All can be changed if needed.

### Products and Deals
- products and deals are built with many-to-many relationship, but again due to time constrain:
- For now, you can only add or remove deal to a product not the other way around
- You can only add one deal at a time to a product
- Multiple deals can be added to one product, but for receipt calculation, only one (with smaller deal ID) will be honored.

### Exception handling
- Did not get tim to build yet, only a very basic place holder

### Data Persistence
- Using H2 im-memory database as indicated
- If I misunderstood the intention, please let me know, I can easily change this.

### Shopping Cart/Basket
- This is represented as user+product+quantity in cartItems table.

### Receipt
- Right now receipt is only a subtotal calculation (a snapshot of shopping cart) and not persist
- So at anytime time a user goes to its receipt end point will see final price after discounts applied, subtotal and total amount due
- But this action will not change anything in the shopping cart
- My idea is that checkout process will permanently clear shopping cart, produce order, final receipt etc. which are not yet supported