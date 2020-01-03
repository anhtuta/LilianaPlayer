const fs = require('fs');
const mm = require('music-metadata');
const config = require('./config');
const INVALID_CHARACTERS = ["\\", "/", "*", "?", "\"", "<", ">", "|"];

let i = 0; // count file
fs.readdir(config.mp3_folder, function (err, files) {
  if (err) {
    return; // not a directory
  }

  files.forEach(function (subFolder) {
    let category = config.mp3_folder + "/" + subFolder;
    fs.readdir(category, function (err2, files) {
      if (err2) {
        return; // not a directory
      }

      files.forEach(function (file) {
        if (file.toLowerCase().endsWith(".mp3")) {
          i++;
          //console.log(i + ": " + category + "/" + file);
          mm.parseFile(category + "/" + file, { native: true })
            .then(metadata => {
              //console.log(util.inspect(metadata, { showHidden: false, depth: null }));
              //console.log(metadata)
              let { title, artist } = metadata.common;
              if (metadata.common.picture && metadata.common.picture.length > 0 &&
                metadata.common.picture[0].data.length > 0) {
                let imageName = artist + " - " + title + ".jpg";
                for (let k = 0; k < INVALID_CHARACTERS.length; k++) {
                  if (imageName.includes(INVALID_CHARACTERS[k])) {
                    imageName = file.replace(".mp3", ".jpg");
                    break;
                  }
                }
                let wstream = fs.createWriteStream(config.album_folder + '/' + imageName);
                wstream.write(metadata.common.picture[0].data);
                wstream.end();
              }
            })
            .catch(err => {
              console.error(err.message);
            });
        }
      })
    })
  });
});