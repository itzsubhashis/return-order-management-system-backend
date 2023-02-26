**Return Order Management System**

Project by Subhashis Das<br>
Cloud Link:
http://localhost:8080-> For accessing the locally deployed application <br>
<br>

<br><br>
http://localhost:8000/swagger-ui/ -> Swagger for authentication microservice<br>
http://localhost:8001/swagger-ui/ -> Swagger for component processing microservice<br>
http://localhost:8002/swagger-ui/ -> Swagger for packaging and delivery microservice<br>
> Login credentials:
>1) username: abc | password: admin
>2) username: cde | password: admin

Project Overview:

This project deals with the return order management wherein a pre-created user can login into the system and can perform:

a) New Request
b) Make Payment
c) View All Request

Login and Logout is implemented using spring security where token is generated upon successful authorization and it is validated at each step. If someone tampers with the token, then it would not allow the user to perform the changes.

Token has an expiry time and once the token expires, the user will get logged out and have to login again.

Frontend/UI is implemented using Angular and bootstrap and necessary validations are there for invalid inputs.

We have also implemented lombok,swagger,test cases,jpa,entity etc and checked using sonar lint. 

***Project Structure:***

Project is seperated into 2 parts:
- Backend
- Frontend

*Backend consists of:*

- API GATEWAY : All requests hit the api gateway and it redirects the user to the appropriate microservice.


- SERVICE REGISTRY : Detects all the microservices present and assists the api gateway.


- AUTHENTICATION MICROSERVICE : Used for authentication, validation, generation and token decoding.


- COMPONENT PROCESSING MICROSERVICE: Used for processing and storing the request details including the payment if user has done it.


- PACKAGING DELIVERY MICROSERVICE: Used to assist the COMPONENT PROCESSING MICROSERVICE to provide the necessary packaging costs.


*Frontend consists of:*

- Angular : Code written using angular and forms and other validations are mostly done using that.


- Bootstrap : Bootstrap is used for beautification


- CSS : Inline and external css are used to assist with the looks whenever desired.
