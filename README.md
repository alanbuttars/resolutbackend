# Resolut Backend

[![Build Status](https://travis-ci.org/alanbuttars/resolutbackend.svg?branch=master)](https://travis-ci.org/alanbuttars/resolutbackend)

This repository represents a simple RESTful API for money transfers between accounts.

### Deploy instructions

1. Install Java SDK 11+

    https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html

2. Install maven

    https://dzone.com/articles/installing-maven

3. Deploy

    ```
    mvn clean install
    mvn exec:java
    ```
   
   The above commands will deploy the application to port `8080` by default.
   
### API

The API is structured as follows:

1. Each user may have one or more accounts, e.g. `alice` has two accounts, `home` and `work`
2. Transfers are made between accounts, e.g. `alice` may send a transfer from her `home` account to `bob`'s `work` account

The following endpoints are supported.

| Resource  | Action                            | Usage  |
|-----------|:----------------------------------|:------------------------------------------------------------------|
| Users     | List all users                    | `GET /users`                                                      |
|           | Fetch user                        | `GET /users/{userId}`                                             |
| Accounts  | List all accounts for a user      | `GET /users/{userId}/accounts`                                    |
|           | Fetch account for a user          | `GET /users/{userId}/accounts/{accountId}`                        |
| Transfers | List all transfers for an account | `GET /users/{userId}/accounts/{accountId}/transfers`              |
|           | Fetch transfer for an account     | `GET /users/{userId}/accounts/{accountId}/transfers/{transferId}` |
|           | Create transfer                   | `POST /users/{userId}/accounts/{accountId}/transfers`             |