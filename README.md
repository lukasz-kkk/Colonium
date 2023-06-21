# Colonium





## API
Game Server API Documentation
set_name
Set the player's nickname.

### Request:

    type: "set_name"
    value: [string] The desired nickname for the player.


### Response:

    Success: The player's nickname is successfully set.
    Error: If the nickname already exists.


### Example:

Request:
{
"type": "set_name",
"value": "Player123"
}
Response (Success):
{
"message": "Nickname changed to Player123"
}
Response (Error):
{
"error": "Nickname already exists"
}
attack
Initiate an attack in the game.

### Request:

    type: "attack"
    src: [string] The source of the attack.
    dst: [string] The destination of the attack.


### Response:

    Success: The attack is initiated.
    Error: If the player or lobby does not exist.


### Example:

### Request:
{
"type": "attack",
"src": "SourceLocation",
"dst": "DestinationLocation"
}
Response (Success):
{
"message": "Attack initiated from SourceLocation to DestinationLocation"
}
Response (Error):
{
"error": "Player or lobby does not exist"
}
create_lobby
Create a new lobby.

### Request:

    type: "create_lobby"
    lobby_name: [string] The name of the lobby to be created.


### Response:

    Success: The lobby is successfully created.
    Error: If the lobby name already exists or other errors occur.


### Example:
Request:
{
"type": "create_lobby",
"lobby_name": "Lobby1"
}
Response (Success):
{
"message": "Lobby Lobby1 created successfully"
}
Response (Error):
{
"error": "Failed to create lobby"
}
join_lobby
Join an existing lobby.

### Request:

    type: "join_lobby"
    lobby_name: [string] The name of the lobby to join.


### Response:

    Success: The player successfully joins the lobby.
    Error: If the lobby does not exist or other errors occur.


Example:
Request:
{
"type": "join_lobby",
"lobby_name": "Lobby1"
}
Response (Success):
{
"message": "Joined Lobby1 successfully"
}

Response (Error):
{
"error": "Failed to join lobby"
}

### Technologies
- LibGDX
- Java
- Python

