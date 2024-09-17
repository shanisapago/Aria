import { MongoClient } from 'mongodb';
import jwt from 'jsonwebtoken';

async function addChat(id, phone, time, msg1, msg2, token) {
    
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        const openChats = db.collection('OpenChats');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
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

async function addMessage(phone, token, message1, timemsg) {
    const ID_ERROR = -1
    let message = JSON.parse(message1)
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const openchats = db.collection('OpenChats');
        const events = db.collection('Events')
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            let my_open_events = await openchats.find({ username: username.username }).toArray();
            try {
                let result;
                for (const item of my_open_events) {
                    result = await item_function(item);
                    if (result) {
                        break;
                    }
                }
                if (!result) {
                    const array = {
                        id: ID_ERROR,
                        array: []
                    }
                    return JSON.stringify(array);

                }
                else {
                    const array = {
                        id: result.id,
                        array: result.chat
                    }
                    return JSON.stringify(array);
                }
            } catch (error) {
                return JSON.stringify("error");
            }

            async function item_function(item) {
                let event = await events.findOne({ id: item.id });
                if (event.chat.phone == phone) {
                    await events.updateOne(
                        { "id": item.id },
                        { $push: { "chat.chat": message } }
                    );
                    await events.updateOne(
                        { "id": item.id },
                        {
                            $set: { "chat.time": timemsg }
                        }
                    );
                    let result = await events.findOne({ id: item.id });
                    let chat_result = result.chat.chat
                    const final_result = {
                        id: item.id,
                        chat: chat_result
                    }
                    return (final_result);
                }
            }
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}

async function getOpenClosedLst(token) {
    const FALSE = 0;
    var arrayEvents = [];
    var arrayOpenEvents = [];
    var arrayClosedEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const event = db.collection('Events');
        const openChat = db.collection('OpenChats');
        const closedChat = db.collection('ClosedChats');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            let openChats = await openChat.find({ username: data.username }).toArray();
            for (var u = 0; u < openChats.length; u++) {
                let eventById = await event.findOne({ id: openChats[u].id });
                const res = {
                    "title": eventById.title,
                    "description": eventById.description
                }
                arrayOpenEvents.push(res);
            }
            let closedChats = await closedChat.find({ username: data.username }).toArray();
            for (var u = 0; u < closedChats.length; u++) {
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
            return FALSE;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addMessage,
    addChat,
    getOpenClosedLst
}
