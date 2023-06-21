# Colonium

## RULES
# Objective
The objective of Colonium is to achieve global domination by capturing and controlling as many territories as possible. Outsmart your opponents, employ tactical maneuvers, and claim victory by having the largest empire on the map.

# GAMEPLAY

Map: Each territory is represented by a cell on the grid.

Territories: At the beginning of the game, each player is assigned a home territory. You can recognize your territories by their unique color.

Expansion: You can choose to expand your empire by capturing adjacent territories that are not controlled by any player. To capture a territory, drag your army from your territory. If successful, the territory will become a part of your empire.

Upgrades: Upgrades can enhance your territories manpower, increase your income, or upgrade your army speed production. Spend your money wisely to gain an advantage over your opponents.

Income: Each territory in your empire generates a certain amount of income per turn. This income can be used to fund further upgrades. The more territories you control, the greater your income will be.

Attacks: You can attack territories without the owner or controlled by other players. To initiate an attack, drag army from your territory. The outcome of attacking province will depend on the number of troops present in each territory.

Troops: Territories contain troops, which are used for both defense and attack. The more troops you have, the stronger your territories become. You can reinforce your territories by transferring troops from one territory to another during your turn.

Defense: Territories with a higher number of troops are more resistant to enemy attacks.

Elimination: If a player loses control of all their territories and dont have army in move they are eliminated from the game. The game continues until only one player remains, who is then declared the winner.

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

