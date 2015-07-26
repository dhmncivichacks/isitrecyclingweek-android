#Is it recycling week?

Appleton WI - Quickly lookup the recycling pickup schedule for the current week.

This open source app was written for citizens of Appleton, WI wanting to know if it is a recycling pickup week or not via their mobile devices. While not a sophisticated application, it is a useful one.

All data comes directly from the city-managed site: http://my.appleton.org/

This is a client app for the [AppletonAPI](https://github.com/mikeputnam/appletonapi)

![Set Location](/screenshots/2015-07-26_5554:Nexus_4_API_21_004_450px.png)![Pick from results](/screenshots/2015-07-26_5554:Nexus_4_API_21_005_450px.png)![Yes recycling](/screenshots/2015-07-26_5554:Nexus_4_API_21_003_450px.png)
##Goals:

* Do one thing, do it well. Resist the temptation to grow the features/purpose of the app to include more than determining if it is recycling week. If some other municipal data app would be useful, create a separate app.
* Be and stay open source. Contribute to and benefit from the local and global community of civic hackers.
* No advertisements. As a community-built, donated-time application let's not sell any products, mkay?
* Cost $0.00 and minimal time investment to maintain. Many Internet services/APIs provide a free tier of resources.
* Do not become data stewards if we can help it. The city already provides the data.
* Be usable by all citizens by being available in more than just the english language.

##How to help:
Want to help improve this app or it's [API](https://github.com/mikeputnam/appletonapi)? Have a suggestion?
Take a look at our [todo list / suggestion box / open issues](https://github.com/mikeputnam/isitrecyclingweek/issues) here on Github.

Generally speaking, this and any open software project can benefit from documentors, graphic designers, user experience designers, good ideas, inclusivity, and of course software developers.

###Development build
If you'd like to try out the latest or provide testing feedback you can install one of [these development builds](https://drive.google.com/a/nextwavehealth.com/folderview?id=0B8rPWjP8XYgDfmpscWR4U3JnZVh0VUllZ1J2ZTRFdVVJLW80Ynd2RG5tbkF5Umxua09rWTA&usp=drive_web#list).

In order to install these .apk files on your Android device, you'll need to enable installing apps from unknown sources(i.e. not the Play Store). Be cautious with this - I personally enable it then disable it again after I've installed the .apk

##Software Setup:

###Pre-requisites:
* [Java JDK7](http://www.oracle.com/technetwork/java/javase/downloads/jdk7-downloads-1880260.html)
* [Git](https://git-scm.com/downloads)
* [Android Studio](http://developer.android.com/tools/studio/) is Google's purpose-built IDE for writing Android apps. It includes the SDK's for emulating devices, a code editor based on IntelliJ IDEA, and source control plugins for git.

###General OS-agnostic steps:
1. Install Java
1. Install Git
1. Install Android Studio
1. Run the Android SDK that came bundled with Android Studio to download and setup platform/API version 21 (Android 5.0) -- I've also tested versions 19 and 22 successfully.
1. Clone this repo to a directory
1. Open the directory from Android Studio as an existing Android Project.
1. Hack away and submit pull reqests back to this repo!

##License:

This project is licensed under the ISC license. See [LICENSE.txt](LICENSE.txt)

##Thanks to:

* [City of Appleton](http://appleton.org/) for providing open data to hack on.
* Bob Waldron and [DHMN Civic Hacks](http://dhmncivichacks.blogspot.com/) for leading the first Northeast Wisconsin civic hackathon.
* All the people at the [Appleton Makerspace](http://appletonmakerspace.org/) for inspiration and a place to write code.
* [Jim Carlson](https://twitter.com/hypnagogic) and [BarCampMilwaukee](http://barcampmilwaukee.org/) for introducing me to unconferences.
* Greg Tracy and [SMSMyBus](http://smsmybus.com/) for an inspiring example of civic hacking in Madison, WI
* [Google App Engine](https://cloud.google.com/appengine/) for providing a generous free tier of their PaaS hosting.
