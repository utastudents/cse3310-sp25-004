# Schema
## Account
### Login
- input: `login`
	- calls `handleLogin`
- response: ???
### Create Account
- input: `new_user`
	- calls: `handleNewUser`
- response: ???

## Game
### Get Active Players
- input: `getActivePlayers`
	-calls: `getActivePlayers()`
- response: ???
### Join Queue
- input: `joinQueue`
	- calls `joinQueue`
response: ???
### Challenge Player
- input: `challengePlayer`
	- calls: `challengePlayer`
- output: ???
### Bot vs Bot
- input: `BotvsBot`
	- calls: `BotvsBot`
- output: ???
### View Match
- input: `ViewMatch`
	- calls: `ViewMatch`
- output: ???
### Game Move
- input: `GameMove`
	- calls: `GameMove`
- output: ???
### BackToHome
## Summary
### Top Ten Data
- input: `requestTopTen`
	- calls `PageManager.retrieveTopTenJson()`
- response: `summaryTopTenData`
### Current User Data
- input: `requestUserJson`
	- calls PageManager.retrieveUserJson()
- response: `summaryUserJson`

