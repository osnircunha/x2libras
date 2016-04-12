/**
 * Created by osnircunha on 3/19/16.
 */
var express = require('express');
var router = express.Router();
var watson = require('watson-developer-cloud');
var fs = require('fs');
var multer = require('multer');
var request = require('request');

var storage = multer.diskStorage({
  destination: function(req, file, cb) {
    cb(null, './uploads/')
  },
  filename: function(req, file, cb) {
    cb(null, file.fieldname + '.' + Date.now() + getExtension(file.originalname))
  }
});

var uploading = multer({
  storage: storage
});

var visualRecognition = watson.visual_recognition({
  version: 'v2-beta',
  username: process.env.visualRecognition_username,
  password: process.env.visualRecognition_password,
  version_date: '2015-12-02'
});

var imageRoute = function(app) {
  app.post('/api/image_recognition/classify', uploading.single('images_file'), function(req, res, next) {
    var params = {
      images_file: fs.createReadStream(req.file.destination + req.file.filename)
    };
    visualRecognition.classify(params, function(err, results) {
      fs.unlinkSync(req.file.path);
      if (err) {
        next(err);
      } else {
        if (results.images && results.images[0] && results.images[0].scores) {
          translate(res, results.images[0].scores);
        } else {
          res.status(200).send('Empty');
        }
      }
    });
  });

  return router;
}

var translate = function(res, imageNames) {
  var words = [];
  console.log('iterate to words:', imageNames.length, 'items |', imageNames, '|');
  imageNames.forEach(function(word) {
    words.push(word.name.replace(/[^a-zA-Z0-9]/g, ' '));
  });
  console.log('words |', words, '|');
  res.redirect('/api/translation/translate?text=' + words);
}


var getExtension = function(filename) {
  var i = filename.lastIndexOf('.');
  return (i < 0) ? '' : filename.substr(i);
}

module.exports = imageRoute;
