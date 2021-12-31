# GamePlan Technical Report

## Introduction
GamePlan is a Kotlin Android App created with Android Studio.

The app allows users to create, view, update games. The games have a location, description and date.
The user profile includes an email and password for login and a profile picture. Users can also log in with their Google account. 

Once built, the .apk file can be installed on any device running Android 11 or higher. 

## Functionality

User can create an account with a username / password or sign in with their Google account. In both instances an account is created in Firebase which the user can authenticate against on future logins. 
If exists, the user's Google profile pic is stored in Firebase Storage as their profile pic and displayed in the Nav drawer header, else a default image is used. The user can update their profile pic by clicking on on it and choosing an image from local storage. This updates their profile pic in Firebase storage. 

Once logged in, the user lands on a list (recycler view) of current games which is loaded from the Firebase DB Each item has the game title description, lat&long, date, email of creator and a reference to the profile pic of the creator. 
The user can toggle between viewing their own games or all the games in the DB. The user can also filter games by entering text which searches the name and description of the games. 

There is a floating action button which the user can click to Add a Game. Here they add a name, description, date and location. The Add location button open a new fragment with a map. Once the location is set by dragging the marker, it is returned to the Add Game fragment by adding a key/value pair to the [savedStateHandle](https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result) of the Add Game fragment on the back stack. This can then be accessed in the Add Game fragment and included in the new game on creation.
After a game is added, the user is returned to the list view. The user can swipe down to refresh the list of games from the Firebase DB.
When viewing their own games on the List view, the user has the option to swipe right to edit or swipe left to delete a game. The user can also view any game by clicking on it. 

On the view game screen, the user sees the details of the game including a map with a marker and a small thumbnail of the user profile pic who created the game. If the game was created by the logged in user, there are buttons to edit or delete the game. 

The edit screen reuses the "Add Game" screen but with the game details filled out and a "Save Game" instead of "Add Game" button. 

In the Nav Drawer, there is an option to switch to Map view. Here the user can view all games on a map, and again toggle between their own or all users' games. The user can click on a game to see a preview of the game's title and date. The FAB is also present on this screen to allow user add a game. 

In the Nav Drawer the user's email and Google name is also displayed, if exists, and the user can sign out of the app. 

## UML CLass Diagram 

![UML CLass Diagram](/GamePlan.jpg?raw=true "Title")

## UX Approach 

The app has a Nav drawer with menu options - List view, Map view and Sign Out. The Nav drawer header contains the user's profile pic and email addresss. The user can change their profile pic by clicking on the pic. 
In the list view, the user can swipe right to edit or swipe left to delete a game. 
In the List and Map views of games, the user can toggle between all games and their own games. 
When adding a game, a date picker is used to select the date. 

## DX Approach 

The app uses the MVVM approach. This approach is used fully for game list and map of all games and the login. There is some compromise in the add / edit game fragment, as databinding is not used for due to challenges with binding to the lat / long location attributes of the game. Overall however these fragments follow the MVVM approach as much as possible. 
The main navigation is hosted in the Home Activity which allows fragments to be started in and navigated between in this activity. 
The app is linked to a Firebase project which hosts authentication, DB and storage for the app. 

## Git Approach 

There is a Develop branch in the Git repository which is merged back to the master branch when a tagged release is ready to be created. 

## Personal Statement

This project is based on the lectures and labs from the Mobile App Development module of the WIT HDip in Software Development with reference to the official Android Developer documentation. 
I have enjoyed the challenges of this project and have developed my skills in Android and general software development. 
Much of the challenges of this particular project came from converting my first Android project from a activity based project to a fragment based project. Once I overcame this challenge I was able to move forward and add more features to my project. 

