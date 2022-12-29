import * as https from "https";
import { MongoClient } from "mongodb";

async function getNasaApod() {
  let apiKey = process.env.NASA_API_KEY
  const apodUrl = "https://api.nasa.gov/planetary/apod?api_key=" + apiKey;

  return new Promise((resolve) => {
    let data = "";

    https.get(apodUrl, (res) => {
      res.on("data", (chunk) => {
        data += chunk;
      });
      res.on("end", () => {
        let apod = JSON.parse(data);
        resolve(apod);
      });
    });
  });
}

async function insertApod(apod) {
    return new Promise((resolve) => {
        console.log("Creating mongo client...")
        let mongoEndpoint = `mongodb+srv://${process.env.MONGO_USERNAME}:${process.env.MONGO_PASSWORD}@apodmongodb.9po0nwz.mongodb.net/?retryWrites=true&w=majority`;
        let client = new MongoClient(mongoEndpoint);
        console.log("Connected to Mongo")

        let apodDb = client.db("apodDB");
        console.log("Apod DB: " + apodDb)

        let apodCollection = apodDb.collection("apod");

        let result = apodCollection.insertOne(apod);
        resolve(result);
    })
}


export const handler = async (event) => {
  let apod = await getNasaApod();

  let result = await insertApod(apod);

  console.log(result);

  const response = {
    statusCode: 200,
    body: JSON.stringify("Successfully inserted apod document"),
    apod: apod
  };
  return response;
};