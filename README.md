# GamePlan Technical Report

## Introduction
GamePlan is a Kotlin Android App created with Android Studio.

The app allows users to create, view, update games. The games have a location, description and date.
The user profile includes an email and password for login and a profile picture. Users can also log in with their Google account. 

Once built, the .apk file can be installed on any device running Android 11 or higher. 

## Functionality

User can create an account with a username / password or sign in with their Google account. In both instances an account is created in Firebase which the user can authenticate against on future logins. 
If exists, the user's Google profile pic is stored as their profile pic and displayed in the Nav drawer header, else a default image is used. The user can update their profile pic by clicking on on it and choosing an image from local storage. This updates their profile pic in Firebase storage. 

Once logged in, the user lands on a list (recycler view) of current games which is loaded from the Firebase DB Each item has the game title, date a profile pic of the creator. The user can toggle between viewing their own games or all the games in the DB. 

Their is a floating action button which the user can click to Add a Game. Here they add a name, description, date and location. The Add location button open a new fragment with a map. Once the location is set by dragging the marker, it is returned to the Add Game fragment by adding a key/value pair to the [savedStateHandle](https://developer.android.com/guide/navigation/navigation-programmatic#returning_a_result) of the Add Game fragment on the back stack. This can then be accessed in the Add Game fragment.
After a game is added, the user is returned to the list view. 
When viewing their own games on the List view, the user has the option to swipe right to edit or swipe left to delete a game. The user can also view a game by clicking on it. 

On the view game screen, the user sees the details of the game including a map with a marker and a small thumbnail of the user profile pic who created the game. If the game was created by the logged in user, there are buttons to edit or delete the game. 

The edit screen reuses the "Add Game" screen but with the game details filled out and a "Save Game" instead of "Add Game" button. 

In the Nav Drawer, there is an option to switch to Map view. Here the user can view all games on a map, and again toggle between their own or all users' games. The FAB is also present on this screen to allow user add a game. 

In the Nav Drawer the user can sign out of the app. 

