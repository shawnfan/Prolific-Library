# Prolific Interactive #


This is the Android Code Challenge from Prolific Interactive. Its purpose is to implement a simple Android app which will connect to web server and performs a GET, a POST and a PUT.

You can clone the code and use ./gradlew installDebug to install it from command line.

It is using [retrofit 2.0](https://github.com/square/retrofit) to make all the API requests.



## Screen1: ##
This will list all the books in the library. In the menu, there are two features implemented here: Add a new book and Delete All book.
![Screen Shot 2016-04-04 at 1.23.28 AM.png](https://bitbucket.org/repo/azAabK/images/4105378939-Screen%20Shot%202016-04-04%20at%201.23.28%20AM.png)

If click on "Delete All", it will ask you "Are you sure to delete all book?" Then yes it will call the API to delete All.
![Screen Shot 2016-04-04 at 1.25.46 AM.png](https://bitbucket.org/repo/azAabK/images/1699531694-Screen%20Shot%202016-04-04%20at%201.25.46%20AM.png)

If click on Add, it will go to Screen2.
If click on any book listed there, it will go to Screen3.

## Screen2: ##
This will ask you to input Book Name, author, publisher, and tag.
![Screen Shot 2016-04-04 at 1.24.11 AM.png](https://bitbucket.org/repo/azAabK/images/886593069-Screen%20Shot%202016-04-04%20at%201.24.11%20AM.png)

If click on Done button or left corner back button, it will go back to Screen1.
Click on Submit, it will first check whether you forget to input any of Book Name, author, publisher, and tag. If something is missing, show the Toast. Otherwise, book will be added successfully and it will go back to Screen1.

## Screen3 ##
This will show all the information about this book.
It will also have two buttons to operation:
1. Checkout, it will update server about "who checkout out this book at YYYY:MM:DD"
![Screen Shot 2016-04-04 at 1.25.06 AM.png](https://bitbucket.org/repo/azAabK/images/3670366761-Screen%20Shot%202016-04-04%20at%201.25.06%20AM.png) 
![Screen Shot 2016-04-04 at 1.25.17 AM.png](https://bitbucket.org/repo/azAabK/images/150326642-Screen%20Shot%202016-04-04%20at%201.25.17%20AM.png)

2. Delete: ask user "Are you sure you want to delete this book?"
If yes, delete it and go back to Screen1. If no, just cancel.
![Screen Shot 2016-04-04 at 1.25.31 AM.png](https://bitbucket.org/repo/azAabK/images/3565981131-Screen%20Shot%202016-04-04%20at%201.25.31%20AM.png)


Notes:
1. Due to all the requests need internet connection, I add check Internet connection features before all the requests.
2. Face with some issues when using retrofit 2.0, Thanks for [Lucas Crawford](http://stackoverflow.com/users/2760679/lucas-crawford) answer my retrofit questioin on StackoverFlow. 
[Question 1 about how to delete by using retrofit](http://stackoverflow.com/questions/36251080/retrofit-2-0-how-to-delete)
[Question 2 about cannot make delete and Put work with retrofit](http://stackoverflow.com/questions/36255825/retrofit-2-0-delete-put-are-not-working)
3. I am using [jsonschema2pojo](http://www.jsonschema2pojo.org/) to directly generate POJO code for Prolific Library Book.

To be continued:
1. Swipe to Delete feature is still in the development, I am using this API: [daimajia/AndroidSwipeLayout](https://github.com/daimajia/AndroidSwipeLayout)
Please check branch swipeListContent.

Please contact me if you find some problems: xiaoyaoworm@gmail.com