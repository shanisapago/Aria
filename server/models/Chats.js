import { MongoClient } from 'mongodb';
import jwt from 'jsonwebtoken';
import e from 'express';
async function addChat(id, phone, time, msg1, msg2, token) {
    console.log("in add chat");
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        const openChats = db.collection('OpenChats');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            //const username = "shani"

            await openChats.insertOne({ id: idNum, username: username.username });

            var jsonMsg1 = { role: "user", content: msg1 };
            var jsonMsg2 = { role: "system", content: msg2 };
            var chatArray = [jsonMsg1, jsonMsg2];
            var jsonChat = { phone: phone, time: time, chat: chatArray };


            await events.updateOne(
                { id: idNum },
                { $set: { chat: jsonChat } }
            );
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}
async function deleteChat(id, token) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const openChats = db.collection('OpenChats');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            //const username = "shani"

            await openChats.deleteOne({ id: idNum, username: username.username });
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}
async function addMessage(phone, token, message1, timemsg) {
    //var idNum=parseInt(id);
    console.log("in model add message")
    console.log(message1)
    let message = JSON.parse(message1)
    console.log("message")
    console.log(message)
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const openchats = db.collection('OpenChats');
        const events = db.collection('Events')
        try {
            //console.log(token)
            const key = "secret key"
            const username = jwt.verify(token, key);
            // const username="naama"
            console.log("username")
            console.log(username)
            let my_open_events = await openchats.find({ username: username.username }).toArray();
            console.log("before function")
            try {
                let result;
                for (const item of my_open_events) {
                    result = await item_function(item);
                    if (result) { 
                        break; 
                    }
                }
                if(!result)
                {
                    const array = {
                        id: -1,
                        array: []
                    }
                    await client.close();
                    return JSON.stringify(array);

                }
                else{
                console.log("print the result");
                console.log(result);
                const array = {
                    id: result.id,
                    array: result.chat
                }
                await client.close();
                return JSON.stringify(array);
            }
            } catch (error) {
                console.log("return");
                await client.close();
                return JSON.stringify("error");
            }

            async function item_function(item) {
                console.log("in function")
                console.log(item.id)
                console.log(typeof (item.id))
                let event = await events.findOne({ id: item.id });
                console.log("afterrrr")
                console.log(event == null)
                console.log(phone)
                console.log(typeof (phone))
                console.log(event.chat.phone)
                console.log(typeof (event.chat.phone))
                if (event.chat.phone == phone) {
                    console.log("the right phone")

                    await events.updateOne(
                        { "id": item.id },
                        { $push: { "chat.chat": message } }
                    );
                    console.log("after update1")
                    await events.updateOne(
                        { "id": item.id },
                        {
                            $set: { "chat.time": timemsg }
                        }
                    );
                    console.log("after update 2")
                    let result = await events.findOne({ id: item.id });
                    let chat_result = result.chat.chat
                    console.log(JSON.stringify(chat_result))
                    const final_result={
                        id:item.id,
                        chat:chat_result
                    }
                    return (final_result);

                }
              
            }


        }
        catch (err) {
            await client.close();
            return false;
        }
    }
    finally {
       //await client.close();
    }
}
async function getOpenClosedLst(token) {
    console.log("in models get open close lst")
    var arrayEvents = [];
    var arrayOpenEvents = [];
    var arrayClosedEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        console.log("in try")
        const db = client.db('Aria');
        const event = db.collection('Events');
        const openChat = db.collection('OpenChats');
        const closedChat = db.collection('ClosedChats');
        try {
            console.log("in try 2")
            const key = "secret key"
            const data = jwt.verify(token, key);
            console.log(data)
            console.log("after data")
            console.log(data.username)
            let openChats = await openChat.find({ username: data.username }).toArray();
            console.log(openChats.length)
            for (var u = 0; u < openChats.length; u++) {
                console.log("in open chat loop")
                let eventById = await event.findOne({ id: openChats[u].id });
                const res = {
                    "title": eventById.title,
                    "description": eventById.description
                }
                arrayOpenEvents.push(res);

            }
            let closedChats = await closedChat.find({ username: data.username }).toArray();
            console.log(closedChats.length)
            for (var u = 0; u < closedChats.length; u++) {
                console.log("in close chat loop")
                let eventById = await event.findOne({ id: closedChats[u].id });
                const res = {
                    "title": eventById.title,
                    "description": eventById.description,
                    "time": eventById.start,
                    "date": eventById.date

                }
                arrayClosedEvents.push(res);

            }
            arrayEvents.push(arrayOpenEvents);
            arrayEvents.push(arrayClosedEvents);
            return arrayEvents;



           
            
        }
        catch (err) {
            console.log("in error")
            console.log(err)
            return 0;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addMessage,
    addChat,
    deleteChat,
    getOpenClosedLst
}
