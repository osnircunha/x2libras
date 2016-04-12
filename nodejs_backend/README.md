# x2libras nodejs backend

Follow the instructions bellow to run backend in your local environment.

### Checkout code

1. Clone the repository with:

    ```sh
    $ git clone https://github.com/osnircunha/x2libras.git
    ```

1. Install [node][node]

1. Install the npm modules:

    ```sh
  $ cd nodejs_backend
  $ npm install
    ```
    **Note:** Make sure you are in the `nodejs_backend` directory, the `package.json` file should be visible.

1. In order to run the backend in your local environment you need an [Bluemix][bluemix] account and an application with `Visual Recognition` and `Language Translation` services:
  * Get credentials to use Visual Recognition [here][vr_docs] and to use Language Translation [here][lt_docs]. After that update .env file with your credentials.

    ```
    visualRecognition_username=<YOUR CREDENTIAL HERE>
    visualRecognition_password=<YOUR CREDENTIAL HERE>
    languageTranslation_username=<YOUR CREDENTIAL HERE>
    languageTranslation_password=<YOUR CREDENTIAL HERE>

    ```

1. Start the app

    ```sh
    $ npm start
    ```

## Services available

It will run the application using port 3000 - http://localhost:3000

See below the available services:

##### GET /api/libras/word?word=query
Return a list of word object based on the given query.  The word object structure is:
  ``` js
  {
    "description": <<Word description>>,
    "librasSample": <<Word sample usage in LIBRAS>>,
    "video": <<Video file name>>,
    "sample": <<Word sample usage>>,
    "word": <<Word itself>>,
  }
  ```

##### GET /api/libras/words
Return a list of all words from database. The word object structure is the same as the service above.

##### GET /api/libras/video/:id
Return a video file based on the given file name.
The videos are currently stored at a Bluemix Cloudant database and are available at http://www.ines.gov.br - Instituto Nacional de Educação de Surdos (INES)

##### GET /api/translation/translate?text=query
Return a list of translations comma separated based on given query. The query could be a simple string or a list of string comma separated.

##### POST /api/image_recognition/classify
Return a list of categories based on a given image file. It should provide an image files as request parameter named `images_file`.
The categories retrieved will be redirect to translate and the response will be the translated comma separated string.

[bluemix]: https://console.ng.bluemix.net/
[node]: http://nodejs.org/download
[repo]: git@github.com:osnircunha/x2libras.git
[vr_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/visual-recognition.html
[lt_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/language-translation.html
