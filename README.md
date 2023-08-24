# Portfolio-Manager
The Portfolio Manager Application is a comprehensive tool designed to help investors manage their investment portfolios. Through a user-friendly API, users can add, update, and monitor their investments, allowing them to make more informed decisions based on their portfolio's performance.

## Prerequisites
Before you begin, ensure you have met the following requirements:

- **Docker**: Installation guide is available [here](https://docs.docker.com/get-docker/).
- **Stock Prices Mock API**: A running instance of this microservice on localhost is needed.

## How to Run the Project

1. **Run the Application**:
   ```bash
   docker compose up -d

## How to Use the Project

The application exposes a RESTful API for managing portfolios.<br>
You can use your favourite tool for sending HTTP queries to access the API. <br> 
You can also take a look at the API here: http://localhost:8080/swagger-ui/index.html
    
## Example HTTP requests to the API that our project provides

1. **Add New User**
   ```bash
   curl -X POST http://localhost:8080/users/add -H "Content-Type: application/json" -d '{"username": "goshko", "email": "gip@gmail.com", "firstName": "Georgi", "lastName": "Ivanov"}'

2. **Buy Assets**
   ```bash
   curl -X POST http://localhost:8080/buy/asset -H "Content-Type: application/json" -d '{"username": "goshko", "assetSymbol": "AAPL", "quantity": 30.0, "price": 100.0}'
