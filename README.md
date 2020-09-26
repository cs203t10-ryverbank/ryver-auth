# Ryver Bank Auth

The Ryver Bank authentication service handles all authentication and authorization responsibilities for the Ryver Bank API.

The microservices use JWTs for authentication and authorization.

## JWT claims format

The generated JWT has the following claims:

```json
{
    "sub": "manager_1",
    "uid": 1,
    "auth": "ROLE_MANAGER",
    "exp": 1601958943
}
```

### `sub`

The subject claim holds the username of the user.

### `uid`

The unique ID claim holds the unique ID of the user in the database. This claim should be used to verify the identity of the user.

### `auth`

The authorities claim holds a comma-separated String representing the different granted authorities for the user.

### `exp`

The expiry of the token signifies till when the token should be considered valid in seconds since Unix Epoch.

Beyond this time, the token should be invalidated.

