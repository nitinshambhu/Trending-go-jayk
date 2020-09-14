

### Design/Tech Choices :
This is a quick summary of the design/tech choices I made. This only specifies the “what” I did.
This can serve as a guide to discuss further if we want to dive deep into the “why”.

**KOIN (vs Dagger) :** I chose to use `Koin`, a lightweight dependency injection library instead of `Dagger`
as it reduces the amount of boilerplate code that’s necessary

**Using UI states to manage UI behaviour :** `RepositoriesUiState` is used to manage the UI states.

There are typically 2 approaches

* Write `setText/setVisibility` methods listening to RxObservables or LiveData exposed by a ViewModel
* With advent of DataBinding, some use it to determine UI behavior

I always found both the approaches problematic, for reasons we can discuss later.
I took a middle path that allows me to move the UI behavior to a data class

**Used Coroutines extensively :** I used Coroutines despite RxJava being widely used in Android App/SDK development
to demonstrate how network operations can be performed using simple and readable Coroutine code.
Having said that, I’ve used RxJava in `RepositoriesAdapter` to emit click events

Please let me know if you are particular about RxJava and I can make the changes & send it back quite fast

**Remote Data fetch :** As per requirements, pull-to-refresh feature is implemented. It forces remote data fetch.
If there is no internet connection, we display an error screen just like the way GMail app shows a `No connection` snack bar

Clicking `Refresh` options menu and clicking `Retry` on error screen are considered same as pull-to-refresh event.
All the three force remote data fetch despite local cache having the data

**RxJava support to fetch data :**

* RxJava changes are added only to demonstrate that I could have used RxJava for network operations. But, I chose not to (reasons can be discussed further)
* As RxJava changes are added only for demonstration purposes, the tests are not written for these methods

**Offline support :**
* Currently, the app works offline as well
* The images/avatars are stored only in memory. So, launching the app after you press “back” or after no internet connection screen is displayed, everything works fine.
* However, if the app process is killed and then re-launched, images aren't served from memory as the memory is killed along with the process.
* We could write the images to the disk (perhaps using a library like `Glide`). But, given this is an app based on Trends which change frequently, I didn't think it's efficient to keep writing images to the disk & clearing them repeatedly

### Possible Improvements / Feature additions:

* The code currently has `ApiResponse.Success` and `ApiResponse.Error`. I would like to add `ApiResponse.Failure` as well.
  - `ApiResponse.Failure` is meant to handle different client http error codes returned by an api - `4xx`.
  - `ApiResponse.Error` is only meant to handle - 5xx.
The reason for this classification can be discussed later

* A utility to check whether the internet connection is available

* Currently, irrespective of the language of the repo, a red dot is displayed next to it.
It would be nice to have a different color for each language

* Comments are added only to non-android classes/files as Android classes are a context by themselves. May be open source projects need comments in more places