## README

Add the key to the `~/.gradle/gradle.properties` file.

### Requirements

Some words about the requirements

#### The app must support endless scrolling, automatically requesting and displaying more images when the user scrolls to the bottom of the view.

The endless scroll listener is implemented inside the `MainActivity`. In a production app I would've taken it out as
well but for this sample app it was small enough to leave it in.

#### The app must allow you to see a history of past searches, by using SearchRecentSuggestionsProvider for example.
I used a 3rd party library, MaterialSearchView. I pulled the library into the code base and modify what was necessary 
to comply with the requirements.

The search story is erased when the `MainActivity` is destroyed but it'd be easy to make it persistent.

#### The app should correctly handle orientation changes (without requiring android:configChanges in your manifest) and should work correctly with the “Don’t Keep Activities” developer option enabled.
This architecture resembles a bit the real architecture I implemented for the app I work on.
The architecture of the app is not pure MVP or MVVM, etc. It is custom architecture, heavily based on RxJava and a lot 
of the business logic gets pushed into th stream.


It is based on view models (VMs) that exposed Observables of data types and methods for different actions, e.g. refresh. 
The UI elements just subscribe to those observables and issue actions when things like PTR happen so that the VM can 
refresh the data.

For this sample app I have replaced the VMs for [`Stores`](https://github.com/NYTimes/Store/). I recently saw this 
library from NYTimes and I took the opportunity to evaluate it using this sample project. 

#### We encourage you to use Android Studio and Gradle. If you choose to use something else, please provide detailed instructions of how to build your application.
Gradle is used.

#### Don’t worry about supporting old versions of Android, a minSdkVersion of 15 or later is completely fine. Bonus points for material design.
API 15+

#### Feel free to use whatever technologies you're the most comfortable with. This includes any sort of open-source third-party frameworks. We encourage you to use open-source libraries like Butterknife, Gson, OkHttp, Otto, Picasso, Retrofit, RxJava, Volley, etc.
I have used the ones I normally used in my day to day, see the `app/guild.gradle`
