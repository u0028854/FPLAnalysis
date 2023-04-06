const { MongoClient } = require('mongodb');

const DB_URI = 'mongodb://localhost:27017/';
const DB_NAME = 'test';
const DB_COLLECTION = 'test';

async function insertObjectData(objectData, db_name, db_collection){
  let client;
  try{
    if(!objectData) throw new Error('Object data is null');

    if(!db_name){
      db_name = DB_NAME;
    }

    if(!db_collection){
      db_collection = DB_COLLECTION;
    }

    client = new MongoClient(DB_URI);
    const collObj = client.db(db_name).collection(db_collection);

    const options = { upsert: true };

    if(Array.isArray(objectData)){
      for(let i = 0; i < objectData.length; i++){
        let tempObject = objectData[i];
        let filter = { _id: tempObject._id };

        await collObj.replaceOne(filter, tempObject, options).then(result => {
          console.log(`Updated id: ${JSON.stringify(result)}`);
        });
      }
    }
    else{
      let filter = { _id: objectData._id };

      await collObj.replaceOne(filter, objectData, options).then(result => {
        console.log(`Updated id: ${JSON.stringify(result)}`);
      });
    }
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

module.exports = insertObjectData;