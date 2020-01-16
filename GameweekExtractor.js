'use strict';

var unirest = require("unirest");
var fs = require("fs");

var fileName = "GWSchedule.csv";
var req = unirest.get("https://fantasy.premierleague.com/api/bootstrap-static/");

req.end(function(res) {
    if (res.error) throw new Error(res.error);

    let jsonArray = res.toJSON().body.events;
    
    for (let i = 0; i < jsonArray.length; i++){
        fs.appendFileSync(fileName, jsonArray[i].id + "," + Date.parse(jsonArray[i].deadline_time) + "\n", (err) => {
            if (err) console.log(err);
        });
    }
});