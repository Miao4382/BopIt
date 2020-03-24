# BopIt

*COP5659 Big Program Assignment 1*

**Table of Content**

* [Introduction](#introduction)
* [Usage-and-Dependencies](#Usage-and-Dependencies)
* [Game-Modes](#Game-Modes)
* [Implementation-of-Sensors](#Implementation-of-Sensors)
* [Multiplayer-Mode-Details](#Multiplayer-Mode-Details)
* [Audio](#Audio)
* [Summary](#Summary)
* [Known-Issues/Bugs](#Known-Issues-and-Bugs)

## Introduction
Our group decided to design and create a mobile game emulating the children’s toy “Bop It”, adapting the actions to make use of sensor feedback. The app works by issuing directions to the user, the user then has a small interval of time in which to perform said action, thereby continuing the game. The time interval feedback is shown by a count down progress bar in the middle of the screen. The allowed interval is reduced based on current score of the user, thus introducing difficulty to the gameplay. This cycle repeats until the user fails to complete an action on time. This is the core gameplay mechanic. The score is incremented for each action completed, and displayed at the top at the end of the game. 

## Usage-and-Dependencies
You should be able to run our app by cloning the git repository, opening, and running the app on a phone connected to android Studio. Since the app uses sensors like accelerometer for shaking and flipping, you will need a physical Android phone to play the game. The app has a Firebase dependency for multiplayer mode, but this should all be included in the git repo and should not cause issues. In addition, the Firebase database is open for reading and writing to everyone, so again there shouldn't be any issues here. 

## Game-Modes
Our app has two modes, single-player and multi-player. The player has the option to select either from the main activity. The single player mode simply keeps track of how well the player does, indicating a score on the screen. Multi-player mode enables two players with the app to challenge each other by playing the game separately and then comparing their scores and providing feedback based on which player has the higher or lower score.

## Implementation-of-Sensors
The four possible actions: flip, tap, shake, spin.
The code to detect whether the action has been performed makes used of values obtained from the accelerometer and gyroscope (note: most phones have both, however, in the case of a more dated model that does not have a gyroscope, then the number of possible actions is reduced. This is the case of the phone we used, so our demo does not incorporate the spin action). When the user completes an action successfully, a random toast is displayed as feedback with a celebratory message. Additionally, the more times that the user successfully completes an action, the more that the interval they have to complete actions is reduced, making it more difficult to get a high score.

## Multiplayer-Mode-Details
In order to keep track of multiple user scores, we use Firebase Real Time Database to store the data. There are two tags in our real time database -- a user tag and a challenge tag. A user must log in with either an existing or new username. Our multiplayer version does not incorporate a passcode to authenticate a user as this was not the focus, but in future versions we could implement this. After logged in, the current games that either the user has started or some other user has started are displayed, as well as the score. To challenge or play another user, the user enters the specific username and hits the 'play' button to either start a new challenge or continue an old one. Once a user completes a game, their score is sent to the database where it is tagged with their username, and the score is updated on the "game center" screen. 

## Audio
We included audio files for each time an action is completed, similar to how the actual BopIt game works. However, the command or 'action' is only displayed as text on the screen. Further work would include making solo player driven entirely by audio commands, which would emulate almost entirely the BopIt game. 

## Summary
This application makes use of Android’s sensor interface. We included a multiplayer mode in order to explore options for communication between devices and found that a shared database model was sufficient for our app. In addition to learning about how to access sensor values and have events occur when certain threshold values were reached, this project was a good opportunity to learn more about the design process of building a simple app.

## Known-Issues-and-Bugs
**Gameplay bug**

Our game has a non-deterministic issue in which gameplay freezes. When this bug occurs, the progress bar will fix at 100%, instruction will freeze and sensor actions are not recoganized. The game is not stopped and there is no exception thrown when this bug occurs, user can still tap "back" to go back to home screen, tap solo or multiplayer to start a new activity of the game, but unable to actually start the game by tap the "Start" button. This can either not happen at all, happen right away, or in the middle of the game. In this case it is best to restart the app. 

**Multiplayer bug**

For multiplayer -- There is sometimes an issue with creating a game as the syncronization of the 'ondataChange' listeners using Firebase was difficult to work with. This results in sometimes challenges being created and displayed twice in the game center. 
