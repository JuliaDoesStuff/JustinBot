# JustinBot
A Discord bot for color roles, made with Discord4J.

# Commands
## invite
Shows the bot invite link and the Discord server link.

## ping
Shows the bot ping.

## add-color
Adds a color role.

### Options
Name | Description | Type | Optional
-----|-------------|------|---------
name | The name of the color role to add. | string | No
color | The HEX color of the color role to add. | string | No

## get-color
Gets info on a color role.

### Options
Name | Description | Type | Optional
-----|-------------|------|---------
name | The name of the color role to get info on. | string | No

## list-colors
Lists all of the color roles.

## remove-color
Removes a color role.

### Options
Name | Description | Type | Optional
-----|-------------|------|---------
name | The name of the color role to remove. | string | No

## select-colors
Generates a select menu to select a color role.

## update-color
Updates a color role.

### Options
Name | Description | Type | Optional
-----|-------------|------|---------
name | The name of the color role to update. | string | No
new-name | The new name of the color role to update. | string | Yes
new-color | The new HEX color of the color role to update. | string | Yes

# License
This project was created under the [MIT License](./LICENSE).