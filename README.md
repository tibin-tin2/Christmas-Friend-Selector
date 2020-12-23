# Christmas-Friend-Selector
A simple android app to select Christmas friends


Christmas Friend Selector will have some values preloaded, but users can able to add or delete existing values but 'LONG PRESS' Start button. Long press will bring you an admin page. Where you can clear Add new users and Delete existing users. And it allows you to clear some flags.

Flags:
1. Add : to Add a new friend name given in the text box;
2. Delete : to delete existing friend given name in the text box;
3. CS : to clear selected flag for given name in the text box;
4. CSS : to clear selected by flag for given name in the text box;
5. Clear all is Selected : to clear selected flag for all friends;
5. Clear all is Selected By : to clear selected by flag for all friends;
6. Clear All :Clear Selected, Selected By flag and christmas friend list for all users;
7. Show Christmas Friend List : Shows the currently assigned Christmas friend list.


Future Update plan :

Some names are preloaded with the app. In the next update it will be removed.

_______
UPDATE
-------
Added Google Firebase support. Source code for the firebase Support is in another new branch called ```using-firebase```. Firebase Configuration is not included in source code. 
Steps to use source code. 
1. import project in Android Studio.
2. Tools -> Firebase and -> Realtime Database. And connect to Firebase and create a new Database in Firebase website; 
3. Add a new node named ```names```
4. next node will be the integere count starting from 0.
5. after that add threee nodes. 
    a. isSelected:0
    b. isSelectedBy:0
    c. name: FistName
    
eg structure 

  christmas-friend-xxxxxxxxx\
  |_____names\
..........|______0\
..................|_____isSelected: 0\
..................|_____isSelectedBy: 0\
..................|_____name: FirstName\
..........|______1\
..................|_____isSelected: 0\
..................|_____isSelectedBy: 1\
..................|_____name: FirstName   \
..........|______2\
..................|_____isSelected: 1\
..................|_____isSelectedBy: 0\
..................|_____name: FirstName\
..........|______3\
..................|_____isSelected: 1\
..................|_____isSelectedBy: 1\
..................|_____name: FirstName\
