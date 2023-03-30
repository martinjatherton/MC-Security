# MC-Security

Global banning system for Minecraft. This was a project I made at 15-years-old (largely using YouTube tutorials & expanding on my prior knowledge of making smaller plugins) to support my application to study Computer Science at University.

Java server plugin that interacted with various PHP/JSON pages to enter and retrieve data on users who try and join a Minecraft server and, if they were banned, stop them from accessing. Each server had its own unique API key that was authenticated for each request that would then run the appropriate queries on the MySQL database.

Website also had a front end, built with Twitter Bootstrap, that displayed each 'ban' (with information on player name, server banned from, which player banned them, and an optional reason that was parsed from the Minecraft command line).

MySQL relational database that stored tables on registered servers, registered users, registered bans, and users who had logged onto an MC-Security server to log their name vs their Minecraft UUID). PHPMyAdmin used for day-to-day database management where necessary

(From what I remember) The API website was hosted on a CentOS Digital Ocean VPS which I hacked together from the command line.

The project never really saw light of day and never had a server registered that wasn't my own, but it was a great experience in learning some new languages and implementing them together into a functional project. The code, in large places, is absolutely naff - but not a bad effort for a spotty teenager if I say so myself.

Official Site (defunct): www.MC-Security.com

Official Server (defunct): server.MC-Security.com

Commands:

  /ban <name> <reason> - Ban a player from your server and list it on MC-Security.com
  
  /unban <name> - Unban a player from your server and unlst it from MC-Security.com
  
  /lookup (/lup) <name> - Show the player's MC-Security record.
  
Permissions:

  mcs.ban:
  
    description: Ban a player.
    
  mcs.unban:
  
    description: Unban a player.
    
  mcs.login.view:
  
    description: View a player's previous bans on login. <Feature coming soon...>
    
  mcs.login.update:
  
    description: Notify the player when they join if an MC-Security update is available.
    
  mcs.lookup:
  
     description: Lookup a player's information.
     

