# x2libras


The application uses inputs such as images and text to provide a  Libras (Linguagem Brasileira de Sinais) video. It uses IBM Watson [Visual Recognition][vr_docs] and [Language Translation][lt_docs] to find words based on picture category. Visual Recognition is used to get categories from a given image and Language Translation is used to translate the retrieved categories.

Checkout the code and try to learn few LIBRAS words using x2libras.


## Getting Started

This instructions will help you to checkout the x2libras app in your local environment.

#### [Setup Android application][android_readme]
#### [Setup NodeJS backend application][backend_readme]


## TODOs
1. Support to search word by voice using [Speech to Text][st_docs]
1. Improve image classifier and categories using [custom classifiers][vr_classifier_docs]


## License

  This sample code is licensed under Apache 2.0. Full license text is available in [LICENSE](LICENSE).

[bluemix]: https://console.ng.bluemix.net/
[node]: http://nodejs.org/download
[repo]: git@github.com:osnircunha/x2libras.git
[vr_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/visual-recognition.html
[vr_classifier_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/doc/visual-recognition/customizing.shtml
[lt_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/language-translation.html
[st_docs]: http://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/speech-to-text.html
[backend_readme]: https://github.com/osnircunha/x2libras/blob/master/nodejs_backend/README.md
[android_readme]: https://github.com/osnircunha/x2libras/blob/master/android/README.md
