'use strict';

//import fetch from 'node-fetch';

var unirest = require("unirest");
var fs = require("fs");
const fetch = require('node-fetch');

// var fileName = "test.html";
// var req = unirest.get("https://fbref.com/en/comps/9/Premier-League-Stats");

// req.end(function(res) {
//     if (res.error) throw new Error(res.error);

//     let jsonArray = res.toJSON().body.events;
    
//     for (let i = 0; i < jsonArray.length; i++){
//         fs.appendFileSync(fileName, jsonArray[i].id + "," + Date.parse(jsonArray[i].deadline_time) + "\n", (err) => {
//             if (err) console.log(err);
//         });
//     }
// });

fetch("https://fbref.com/en/comps/9/Premier-League-Stats", {
  "headers": {
    "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "accept-language": "en-US,en;q=0.9",
    "cache-control": "no-cache",
    "pragma": "no-cache",
    "sec-ch-ua": "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"",
    "sec-ch-ua-mobile": "?0",
    "sec-ch-ua-platform": "\"Windows\"",
    "sec-fetch-dest": "document",
    "sec-fetch-mode": "navigate",
    "sec-fetch-site": "same-origin",
    "sec-fetch-user": "?1",
    "upgrade-insecure-requests": "1",
    "cookie": "sr_lang_views-en=-0.5; hubspotutk=207d2d2db3b551848ac3ac55e8fca646; __gads=ID=919461ca35f896c1-22691a8cdacf0098:T=1640758535:RT=1640758535:S=ALNI_MaTpFOICw0mKPCx-unWIwPVl0fabQ; __hstc=218152582.207d2d2db3b551848ac3ac55e8fca646.1629740485833.1642701335961.1645040025894.20; _ga=GA1.1.727492967.1594414199; _ga_T897NZ0GWZ=GS1.1.1652116403.1.0.1652116410.0; srcssfull=yes; is_live=true; __cf_bm=On3fX3zhZu9M12QtasFFgG8X.UdrhOVeTdOLEabdiLw-1659999415-0-Ae3LN31Awmm9mzipftD7hdf8Q9JeM3euK0lT9G/bKE3vth5LjR3pMjpsBz7o3BNtMQ7kEGrhAvgFX20hZrM0DdI=; sr_note_box_countdown=39",
    "Referer": "https://fbref.com/en/comps/9/2021-2022/2021-2022-Premier-League-Stats",
    "Referrer-Policy": "unsafe-url"
  },
  "body": null,
  "method": "GET"
}).then(res => res.text())
.then(text => console.log(text));