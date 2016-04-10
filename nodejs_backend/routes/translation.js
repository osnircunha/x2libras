/**
 * Created by osnircunha on 9/28/15.
 */
var express = require('express');
var router = express.Router();
var watson = require('watson-developer-cloud');

var languageTranslation = watson.language_translation({
  username: process.env.languageTranslation_username,
  password: process.env.languageTranslation_password,
  version: 'v2'
});

router.get('/identifylanguage', function(req, res, next) {
  languageTranslation.identify({
      text: req.query.text.replace(/[^a-zA-Z0-9]/g, ' ')
    },
    function(err, identifiedLanguages) {
      if (err)
        next(err);
      else
        res.status(200).send(identifiedLanguages);
    });
});

router.get('/translate', function(req, res, next) {

  languageTranslation.translate({
    text: req.query.text.indexOf('_') > -1 ? req.query.text.replace(/[^a-zA-Z0-9]/g, ' ') : req.query.text,
    source: 'en',
    target: 'pt'
  }, function(err, translation) {
    if (err) {
      next(err);
    } else {
      res.status(200).send(translation);
    }
  });

});

module.exports = router;
