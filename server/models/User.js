import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function addUser(username, password, phoneNumber) {
    console.log("in add user models")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        let result = await user.find({username : username}).toArray();
        if(result.length===1){
            return false;
        }
        await user.insertOne({ username : username, password : password, phoneNumber : phoneNumber});
    }
    finally {
        await client.close();
    }
    return true;
}

//async function getEvent(token, username){
async function getEvents(token) {
    //console.log("in models get events")
    var arrayEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const event = db.collection('Events');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            //console.log("data")
            //console.log(data)
            let result = await event.find({}).toArray();
            for (var i = 0; i < result.length; i++) {
                for (var u = 0; u < result[i].users.length; u++) {
                    if (result[i].users[u] == data.username) {
                        for (var j = 0; j < result[i].alert.length; j++) {
                            //console.log("in if")
                            if (result[i].alert[j].username == data.username) {
                                console.log("in");
                                console.log(result[i].alert[j]);
                                const res = {
                                    "id": result[i].id,
                                    "title": result[i].title,
                                    "description": result[i].description,
                                    "date": result[i].date,
                                    "users": result[i].users,
                                    "start": result[i].start,
                                    "end": result[i].end,
                                    "alertString": result[i].alert[j].alert
                                }
                                arrayEvents.push(res);
                            }
                        }
                    }
                }
            }
            return arrayEvents;
        }
        catch (err) {
            return 0;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addUser,
    getEvents
}