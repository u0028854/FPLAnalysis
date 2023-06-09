const { MongoClient } = require('mongodb');

const DB_URI = 'mongodb://127.0.0.1:27017/';
const DB_NAME = 'test';
const DB_COLLECTION = 'test';

async function insertObjectData(objectData, db_name, db_collection, callingFunction){
  console.log('Starting insertObjectData for ' + callingFunction);
  
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
          // console.log(`Updated id: ${JSON.stringify(result)}`);
        });
      }
    }
    else{
      let filter = { _id: objectData._id };

      await collObj.replaceOne(filter, objectData, options).then(result => {
        // console.log(`Updated id: ${JSON.stringify(result)}`);
      });
    }
  }
  catch(error){
    throw new Error(error);
  }
  finally {
    console.log('closing client for ' + callingFunction);
    await client.close();
    console.log('client closed for ' + callingFunction);
  }
}

async function queryObjectData(query, db_name, db_collection, callingFunction){
  console.log('Starting queryObjectData for ' + callingFunction);
  
  let client;
  let retVal = [];

  try{
    if(!query) throw new Error('Query is null');

    if(!db_name){
      db_name = DB_NAME;
    }

    if(!db_collection){
      db_collection = DB_COLLECTION;
    }

    client = new MongoClient(DB_URI);
    let resultSetCursor;
    
    if(Array.isArray(query)){
      resultSetCursor = client.db(db_name).collection(db_collection).aggregate(query);
    }
    else{
      resultSetCursor = client.db(db_name).collection(db_collection).find(query);
    }

    await resultSetCursor.forEach(resultSetObject => {
      retVal.push(resultSetObject);
    });
  }
  catch(error){
    throw new Error(error);
  }
  finally {
    console.log('closing client for ' + callingFunction);
    await client.close();
    console.log('client closed for ' + callingFunction);
  }

  return retVal;
}

module.exports.insertObjectData = insertObjectData;
module.exports.queryObjectData = queryObjectData;