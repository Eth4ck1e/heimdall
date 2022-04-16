# Heimdall

A plugin for Velocity to allow whitelists to be synced between all connected servers with minimal setup.

This plugin includes a discord bot feature to allow for whitelisting directly from discord.

MySQL is used as the database to sync server whitelists. When the /whitelist add player command is run the SQL database
is updated. At which point all connected servers will detect the database change and export the new database, overwrite
the whitelist.json, and run /whitelist reload.

Additional features include application handling within discord, tickets, and a possible zero-config release using an
embedded database.

Heimdall is a plugin made by the dev team from bifrostsmp.com:

@Eth4ck1e @HunnaG

## Installation

Place the Heimdall-Velocity plugin in your Velocity plugin folder

Start the proxy server

Enter your MySQL database information into the plugin config.yml

You may also optionally create a discord bot and enter the bot token here as well. (This is required to access discord
features)

```yaml
## This is the main plugin configuration file

DISCORD_TOKEN: ""

## MySQL database connection info
## database:
host:  # host of your database
port:  # default port for MariaDB and MySQL
database:  # name of your database. 
user: 
password: 
```

## Discord Commands

```java
/info        //returns basic plugin/bot information
/ping        //returns Pong!
/whitelist add <ign>    //adds player to whitelist database and initiates whitelist sync
```

## Contributing

Project is built using gradle

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update test builds as appropriate.

The first version 0.1.0 has been completed. From this point forward all future updates need to use the appropriate
versioning. New features update minor version x as 0.x+1.0 New major version (breaks compatibility with previous
versions) update x as x+1.0.0 Bug fixes update x as 0.0.x+1

## License

[MIT](https://choosealicense.com/licenses/mit/)
