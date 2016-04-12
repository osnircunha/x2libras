# x2libras android

Follow the instructions bellow to get Libras Android application running.

### Latest build
[x2Libras apk][apk_url]


### Checkout code

1. Clone the repository with:

    ```sh
    $ git clone https://github.com/osnircunha/x2libras.git
    ```

1. Install [Android Studion][android_sdk]

1. On Android Studio select `Open an existing Android Studio project` and open `android` folder from git repository.


By default it is using the Bluemix instance of x2libras backend. You can set up and run the backend locally by following [Set up back end instructions][backend_readme] and change the host on `Constants.java` class.

```java
package com.ocunha.librasapp.utils;

public class Constants {
    private static final String BASE_URL = "http://localhost:3000/api";
...
```

[android_sdk]: http://developer.android.com/sdk/index.html
[backend_readme]: https://github.com/osnircunha/x2libras/blob/master/nodejs_backend/README.md
[apk_url]: http://ec2-54-201-148-54.us-west-2.compute.amazonaws.com/jenkins/job/x2libras/lastSuccessfulBuild/artifact/android/app/build/outputs/apk/app-debug.apk
