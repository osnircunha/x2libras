/**
 * Created by osnircunha on 3/19/16.
 */
var express = require('express');
var router = express.Router();
var watson = require('watson-developer-cloud');
var fs = require('fs');
var multer = require('multer');

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
        if (req.query.translate === 'true') {
          translate(results, function(list) {
            res.redirect('/api/translation/translate?text=' + list);
          })
        } else {
          res.status(200).send(results);
        }
      }
    });
  });

  return router;
}

var translate = function(imageNames, callback) {
  if (imageNames.images && imageNames.images[0] && imageNames.images[0].scores) {
    var words = [];
    imageNames.images[0].scores.forEach(function(word) {
      words.push(word.name.replace(/[^a-zA-Z0-9]/g, ' '));
    });
    callback(words);
  }

}


var getExtension = function(filename) {
  var i = filename.lastIndexOf('.');
  return (i < 0) ? '' : filename.substr(i);
}

module.exports = imageRoute;
