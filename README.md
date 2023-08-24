# Portfolio-Manager
The Portfolio Manager Application is a comprehensive tool designed to help investors manage their investment portfolios. Through a user-friendly API, users can add, update, and monitor their investments, allowing them to make more informed decisions based on their portfolio's performance.

## Prerequisites
Before you begin, ensure you have met the following requirements:

- **Java 11+**: This application is built using Java. Ensure JDK 11 or later is installed. Download from [Oracle's official site](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).

- **Maven**: This is the chosen build tool. If not installed, follow the [official guide](https://maven.apache.org/install.html).

- **Docker** (Optional): For those opting for a containerized environment. Installation guide is available [here](https://docs.docker.com/get-docker/).


## How to Run the Project
1. **Make sure that the Stock Prices Mock Microservice is running**

2. **Clone the Repository**:
   ```bash
   git clone https://github.com/Hypermood/Portfolio-Manager.git
   cd portfolio-manager

3. **Build with Maven**
    ```bash
    mvn clean install
4. **Run the Application**
    ```bash
    mvn spring-boot:run

5. **Alternatively, with Docker**
    ```bash
    docker build -t portfolio-manager .
    docker run -p 8080:8080 portfolio-manager

## How to Use the Project

The application exposes a RESTful API for managing portfolios.

### User Endpoints

1. **Create User**
    - HTTP Method: POST
    - URL: /users/add
    - BODY:
   ```json
    {
       userName: string,
       email: string,
       firstName: string, (Optional)
       lastName: string (Optional)
    }
2. **Delete User**
    - HTTP Method: DELETE
    - URL: /users/delete
    - BODY:
   ```json
    {
       userName: string,
    }
### Assets' Endpoints

1. **Buy Assets**
    - HTTP Method: POST
    - URL: /buy/asset
    - BODY:
   ```json
   {
       userName: string,
       assetSymbol: string,
       quantity: double,
       price: double
   }

2. **Sell Asset**
    - HTTP Method: POST
    - URL: /sell/asset
    - BODY:
   ```json
   {
       userName: string,
       assetSymbol: string,
       quantity: unsigned int,
       boughtPrice: unsigned int
   }
### Analytics Endpoint

1. **Get Analytics**
    - HTTP Method: GET
    - URL: /analytics?userName=${userName}
    
## Example HTTP requests to the API that our project provides

1. **Add New User**
   ```bash
   curl -X POST http://localhost:8080/users/add -H "Content-Type: application/json" -d '{"username": "goshko", "email": "gip@gmail.com", "firstName": "Georgi", "lastName": "Ivanov"}'

2. **Buy Assets**
   ```bash
   curl -X POST http://localhost:8080/buy/asset -H "Content-Type: application/json" -d '{"username": "goshko", "assetSymbol": "AAPL", "quantity": 30.0, "price": 100.0}'
