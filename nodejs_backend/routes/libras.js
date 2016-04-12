/**
 * Created by osnircunha on 9/28/15.
 */
var express = require('express');
var router = express.Router();
var fs = require('fs');
var mime = require('mime');
var nano = require('nano')({
  "url": process.env.cloudant_url
});
var librasDb = nano.use("libras_words");
var librasVideoDb = nano.use("libras_videos");

var forEachAsync = require('foreachasync').forEachAsync

/* GET home page. */
router.get('/word', function(req, res, next) {
  var word = req.query.word;
  console.log('request word |', word, '|');
  librasDb.search('words', 'wordSearch', {
    q: word
  }, function(err, doc) {
    if (!err) {
      var list = []
      doc.rows.forEach(function(item) {
        list.push({
          "description": item.fields.description,
          "librasSample": item.fields.librasSample,
          "video": item.fields.video,
          "sample": item.fields.sample,
          "word": item.fields.word
        });
      });
      console.log('list |', list, '|');
      res.status(200).send(list);
    } else {
      console.log(err);
    }
  });
});

router.get('/words', function(req, res, next) {
  librasDb.view("words", "wordsList", function(err, doc) {
    if (!err) {
      var list = []
      doc.rows.forEach(function(item) {
        list.push({
          "description": item.key.description,
          "librasSample": item.key.librasSample,
          "video": item.key.video,
          "sample": item.key.sample,
          "word": item.key.word
        });
      });
      res.status(200).send(list);
    }
  });
});

router.get('/video/:id', function(req, res, next) {
  var id = req.params.id;
  librasVideoDb.attachment.get(id, 'video', function(err, body) {
    if (!err) {
      res.status(200).send(body);
    }
  });
});

module.exports = router;
