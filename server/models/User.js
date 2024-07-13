import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
async function checkUsername(username) {
    console.log("in check username models")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        let result = await user.find({ username: username }).toArray();
        if (result.length === 1) {
            return false;
        }
        else {
            return true;
        }
    }
    finally {
        await client.close();
    }
    return true;
}
async function addUser(username, password, phoneNumber, token) {
    console.log("in add user models")
    console.log(token)
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        let result = await user.find({username : username}).toArray();
        if(result.length===1){
            return false;
        }
        await user.insertOne({ username : username, password : password, phoneNumber : phoneNumber, appToken : token});
    }
    finally {
        await client.close();
    }
    return true;
}

//async function getEvent(token, username){
/*async function getEvents(token) {
    //console.log("in models get events")
    var arrayEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const event = db.collection('Events');
        const users_collection = db.collection('User');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            //console.log("data")
            //console.log(data)
            let result = await event.find({}).toArray();
            for (var i = 0; i < result.length; i++) {
                var arrayUsersPhones = [];
                for (var u = 0; u < result[i].users.length; u++) {
                    
                    let user = await users_collection.find({ "username": result[i].users[u] }).toArray()
                    console.log("user phone")
                    console.log(user[0].phoneNumber)
                    
                    arrayUsersPhones.push(user[0].phoneNumber)
                }
                console.log(arrayUsersPhones.length)
                for (var u = 0; u < arrayUsersPhones.length; u++) {
                    console.log(arrayUsersPhones[u])    
                }
         
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
                                    "phoneNumbers": arrayUsersPhones,
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
}*/


async function getEvents(token) {
    console.log("in models get events")
    var arrayEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const event = db.collection('Events');
        const openChat = db.collection('OpenChats');
        const users_collection = db.collection('User');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            //console.log("data")
            //console.log(data)
            let result = await event.find({}).toArray();
            let resultChat = await openChat.find({}).toArray();
            console.log(result.length)
            for (var i = 0; i < result.length; i++) {
                var arrayUsersPhones = [];
                for (var u = 0; u < result[i].users.length; u++) {

                    let user = await users_collection.find({ "username": result[i].users[u] }).toArray()
                    arrayUsersPhones.push(user[0].phoneNumber)
                }
                let flagChat = true;
                for (var u = 0; u < result[i].users.length; u++) {
                    if (result[i].users[u] == data.username) {
                        for (var j = 0; j < result[i].alert.length; j++) {
                            //console.log("in if")
                            if (result[i].alert[j].username == data.username) {
                                for (var k = 0; k < resultChat.length; k++) {
                                    if (resultChat[k].id == result[i].id) {
                                        flagChat = false;
                                    }
                                }
                                if (flagChat == true) {
                                    const res = {
                                        "id": result[i].id,
                                        "title": result[i].title,
                                        "description": result[i].description,
                                        "date": result[i].date,
                                        "phoneNumbers": arrayUsersPhones,
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


async function checkUser(phones) {
    console.log("in models checkUser")
    var users = [];
    var notUsers = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const usersMongo = db.collection('User');


        try {
            console.log(("1"))
            let usersResult = await usersMongo.find({}).toArray();
            console.log("4")
            console.log(phones)
            //console.log(numbers)
            console.log(phones.length);
            console.log(phones[0]);
            console.log(phones[1]);
            for (var j = 0; j < phones.length; j++) {
                //console.log("3")
                let flag = true;
                for (var i = 0; i < usersResult.length; i++) {
                    if (phones[j] == usersResult[i].phoneNumber) {
                        flag = false;
                        users.push(phones[j]);
                    }
                }
                if (flag) {
                    notUsers.push(phones[j]);
                }
            }
            console.log("2")
            const res = {
                "users": users,
                "notUsers": notUsers
            }
            console.log(res);
            return res;
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
    getEvents,
    checkUser,
    checkUsername
}
