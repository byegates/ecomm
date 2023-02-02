# E-Commerce DEMO Project

## [VIDEO DEMO](https://youtu.be/KTrA1xwZHoU)
Video demo of important end points
<pre>
NOTE: jar file and code has since been updated after video upload,
certain behavior maybe slightly different from video, and end points are more than what's in video
Key behaviors and endpoints should be the same.
</pre>

## To run the app:
### download jar
[download jar file here](https://drive.google.com/file/d/1BteQPivoD9LDrbWMRcCYzPQ2FzQf6Xzo/view?usp=share_link)
### run
```shell
java -jar ecomm-0.0.1-SNAPSHOT.jar
```
the project was built with jdk 17 + spring boot 3.0.2, it's config, app runs on default 8080 port.
## you could also build it from source
```shell
./gradlew  clean build
```
## To run tests
```shell
./gradlew  clean test
```
If you have your own gradle setup you could also use that.
## End Points/Swagger
For the sake of easier navigation I put swagger in.
```css
http://localhost:8080/swagger-ui/index.html
```
## H2 Console
<pre>
url: jdbc:h2:mem:testDB
user: sa
no password
</pre>
```css
http://localhost:8080/h2-console
```

## Notes Regarding Endpoints
- adding or remove deal to product, you need to specific a action, it's either 'add' or 'remove' are right now supported
```shell
http://localhost:8080/products/{id}/{action}/{dealId}
```

## More notes
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
