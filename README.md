# simple-jwt-authentication-spring

## SETUP ##
``
INSERT INTO roles(name) VALUES('ROLE_USER');
``

``
INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
``

``
INSERT INTO roles(name) VALUES('ROLE_ADMIN');
``

## RUN ##
``
mvn spring-boot:run
``

## USING ##
### SIGN-UP ###
``
POST
``
``
http://localhost:8080/api/v1/auth/signup
``

```json
{
    "username": "teste",
    "email": "teste@teste.com",
    "password": "123456",
    "roles": [
        "ROLE_ADMIN"
    ]
}
```

### SIGN-IN ###
``
POST
``
``
http://localhost:8080/api/v1/auth/signin
``

```json
{
    "username": "teste",
    "password": "123456"
}
```
