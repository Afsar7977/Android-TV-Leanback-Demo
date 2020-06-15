## Android TV Leanback Demo App.

In the past couple of years Android TV has seen substantial increase in its popularity hence more and more user are switching to 
Android TV as their go to Device for entertainment purpose. With this there's also an increase in Android Tv App Development which 
is quite good to hear for us developers right? Ahh.. I would say probably not. 

Since there is very few tutorial on the net which can help you to start an Organisation Project i've faced a lot of problems when 
starting to code in this domain. So i decided to create a simple App to demonstrate the basic flow of TV Development while showcasing
the neccessary things which are needed to start an Full Fledge Project(I'm talking in terms of API).

Android TV consists of mainly four modules :

1. Main Page.
2. Details Page.
3. Player Page.
4. Search Page(will be added soon).

We have to thoroughly aimed at designing the App so that the user which is sitting far from the Television can easily use our 
Application quite easily.

Android Leanback offers us inbuilt fragments which helps us to initiate the coding like BrowseFragment, DetailsFragment, VideoSupportFragment
and SearchSupportFragment all these when extended from our Class will give the basic functionalities of an TV Layout on which you
can start building your own methods and logic.

In this app i've tried to use the basic Google Videos Api provided by them in their tv-showcase github page for creating an 
List-Row of some elements which are presented using Row Fragment and on clicking them you traverse to the Details Page.

For Details Page you can customise the Action Adapter(which you'll see in the details overview method), the related videos row, 
and the background manager of the page by changing some lines in the project.

Then comes the Player Page which is designed to play even the latest formats of Videos such as HLS,Dash and other streaming formats
with utmost efficiency. 

The rest of the code and flow is almost similar to Mobile Development and i guess you'd be able to understand it quite easily.
If by chance you found any bugs or error in the project please reach out to me and give your feedback as it'll not just help me 
solve the issue but also to make this project even more better for you guys.
