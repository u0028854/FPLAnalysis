const { MongoClient } = require("mongodb");

// Replace the uri string with your connection string.
const uri = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass%20Community&ssl=false";

const client = new MongoClient(uri);

const testObject = {"_id":270,"id":266,"minutes":72,"goals_scored":1,"assists":0,"clean_sheets":0,"goals_conceded":1,"own_goals":0,"penalties_saved":0,"penalties_missed":0,"yellow_cards":0,"red_cards":0,"saves":0,"bonus":3,"bps":34,"total_points":10};

async function run() {
  try {
    // const database = client.db('FFSData');
    // const movies = database.collection('FFSPlayerData');

    const dbObj = client.db('test');
    const collObj = dbObj.collection('test');

    const result = await collObj.insertOne(testObject);
    console.log(`New listing created with the following id: ${result.insertedIds}`);

    // // Query for a movie that has the title 'Back to the Future'
    // const query = { FPL_POINTS: 24 };
    // //const movie = await movies.find(query);
    // const cursor = movies.find(query);

    // await cursor.forEach(
    //     (data) => { 
    //         console.log(data.PLAYER_NAME); 
    //         console.log(data.MATCH_DATE);
    //         console.log(data.OPP_NAME);
    //         console.log(data.MATCH_LOCATION);
    //     });
  } finally {
    // Ensures that the client will close when you finish/error
    await client.close();
  }
}

async function testCode(objectData){
  try{
    if(!objectData) throw new Error('Object data is null');

    const uri = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass%20Community&ssl=false";

    const client = new MongoClient(uri);
    const dbObj = client.db('test');
    const collObj = dbObj.collection('test');

    const options = { upsert: true };

    for(let i = 0; i < objectData.length; i++){
      let tempObject = objectData[i];
      let filter = { _id: tempObject._id };
      tempObject.saves = 67;

      await collObj.replaceOne(filter, tempObject, options).then(result => {
        console.log(`Updated id: ${JSON.stringify(result)}`);
      });
    }
    
    // await collObj.insertMany(objectData).then(result => {
    //   console.log(`New listing created with the following id: ${result.insertedIds}`);
    //   // resolve('resolved');
    // });
  }
  catch(error){
    console.error(error);
  }
  finally {
    console.log('closing client');
    await client.close();
    console.log('client closed');
  }
}

// testCode(testObject);
module.exports = testCode;
//run().catch(console.dir);