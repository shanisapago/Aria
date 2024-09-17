import { MongoClient } from 'mongodb';
import jwt from 'jsonwebtoken';

async function addEvent(title, description, date, token, start, end, alertString, flag) {
    const REQUEST_CODE_ERROR = -1
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            const userArray = [username.username];
            const alertWuser = [{ alert: alertString, username: username.username, requestCode: REQUEST_CODE_ERROR }]
            let event = await events.find({}).toArray();
            var eventPlace = parseInt(event.length);
            var eventId = eventPlace + 1;
            var id = eventId.toString();
            await events.insertOne({ id: eventId, title: title, description: description, date: date, users: userArray, start: start, end: end, alert: alertWuser, chat: "", flag: flag });
            return id;
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}

async function checkPhones(title, description, date, token, start, end, alertString, phones, tok, flag, requestCode) {
    const EMPTY = 0
    const ID_ERROR = -1
    let id;
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    var users_with_phones = [];
    var array_of_not_users = [];
    var arrayMembers = [];
    var appTokens = [];
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        const users = db.collection('User');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            if (phones == null) {
                id = await addEvent(title, description, date, token, start, end, alertString, flag)
                let idNum = parseInt(id);
                await events.updateOne(
                    { "id": idNum, "alert.username": username.username },
                    {
                        $set: {
                            "alert.$.requestCode": requestCode
                        }
                    }
                );
                const json = {
                    id: id,
                    notUsers: array_of_not_users,
                    appToken: "",
                    sender: "",
                    title: ""
                }
                arrayMembers.push(json)
                return arrayMembers;
            }
            else {
                for (var u = 0; u < phones.length; u++) {
                    let user = await users.findOne({
                        phoneNumber: phones[u].phone
                    });
                    if (user) {
                        const res = {
                            username: user.username,
                            phone: user.phoneNumber
                        }
                        users_with_phones.push(res)
                        appTokens.push(user.appToken)
                    }
                    else {
                        array_of_not_users.push(phones[u])
                    }
                }
                if (users_with_phones.length == phones.length || phones.length == EMPTY) {
                    id = await addEvent(title, description, date, token, start, end, alertString, flag)
                    let idNum = parseInt(id);
                    await events.updateOne(
                        { "id": idNum, "alert.username": username.username },
                        {
                            $set: {
                                "alert.$.requestCode": requestCode
                            }
                        }
                    );
                    var user = await users.find({ username: username.username }).toArray()
                    const members_invitation = db.collection('MembersInvitation');
                    let msg = "you are invited to event " + title + " on " + date + " at " + start + ". you are invited by " + user[0].fullName
                    if (phones.length != EMPTY) {
                        for (var u = 0; u < phones.length; u++) {
                            await members_invitation.insertOne({ id: id, name: phones[u].name, phone: phones[u].phone, message: msg, appToken: appTokens[u], tokenSender: tok, sender: username.username, title: title, username: users_with_phones[u].username });
                            const member = {
                                id: id,
                                notUsers: [],
                                appToken: appTokens[u],
                                sender: username.username,
                                title: title
                            }
                            arrayMembers.push(member);
                        }
                    }
                    else {
                        const json = {
                            id: id,
                            notUsers: array_of_not_users,
                            appToken: "",
                            sender: "",
                            title: ""
                        }
                        arrayMembers.push(json)
                    }
                    return arrayMembers;
                }
                else {
                    const json = {
                        id: ID_ERROR,
                        notUsers: array_of_not_users,
                        appToken: "",
                        sender: "",
                        title: ""
                    }
                    arrayMembers.push(json)
                    return arrayMembers;
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

async function delete_members_invitation(id, username) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const members_invitation = db.collection('MembersInvitation');
        var invitaion = await members_invitation.find({ id: id, username: username }).toArray()
        if (invitaion.length == 1) {
            await members_invitation.deleteOne({ id: id, username: username })
        }
    }
    finally {
        await client.close();
    }
}

async function getMembersNotifications(token) {
    const FALSE = 0
    var arrayMembersNotifications = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const members_invitation = db.collection('MembersInvitation');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            let result = await members_invitation.find({ username: data.username }).toArray();
            for (var i = 0; i < result.length; i++) {
                const msg = {
                    id: result[i].id,
                    message: result[i].message,
                    appToken: result[i].appToken,
                    tokenSender: result[i].tokenSender,
                    name: result[i].name,
                    title: result[i].title
                }
                arrayMembersNotifications.push(msg);
            }
            return arrayMembersNotifications;
        }
        catch (err) {
            return FALSE;
        }
    }
    finally {
        await client.close();
    }
}

async function joinEvent(id, username, alert) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    { $push: { users: username[0] } }
                );
                await events.updateOne(
                    { id: idNum },
                    { $push: { alert: alert[0] } }
                );
                var alert_string = ""
                for (var j = 0; j < event[0].alert.length; j++) {
                    if (event[0].alert[j].username == username[0]) {
                        alert_string = event[0].alert[j].alert
                    }
                }
                const e = {
                    "id": event[0].id,
                    "title": event[0].title,
                    "description": event[0].description,
                    "date": event[0].date,
                    "phoneNumbers": [],
                    "start": event[0].start,
                    "end": event[0].end,
                    "alertString": alert_string
                }
                return JSON.stringify(e);
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

async function updateTitle(id, title) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    { $set: { title: title } }
                );
            }
            else {
                return false;
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

async function updateDescription(id, description) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    {
                        $set: { description: description }
                    }
                );
            }
            else {
                return false;
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

async function updateDate(id, date) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    {
                        $set: { date: date }
                    }
                );
            }
            else {
                return false;
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

async function updateStart(id, start) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    {
                        $set: { start: start }
                    }
                );
            }
            else {
                return false;
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

async function updateEnd(id, end) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { id: idNum },
                    {
                        $set: { end: end }
                    }
                );
            }
            else {
                return false;
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

async function updateAlert(id, token, alert) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { "id": idNum, "alert.username": username.username },
                    {
                        $set: {
                            "alert.$.alert": alert
                        }
                    }
                );
            }
            else {
                return false;
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

async function updateRequestCode(id, token, requestCode) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {
                await events.updateOne(
                    { "id": idNum, "alert.username": username.username },
                    {
                        $set: {
                            "alert.$.requestCode": requestCode
                        }
                    }
                );
            }
            else {
                return false;
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

async function deleteEventById(id, token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        let title = "";
        const key = "secret key"
        const username = jwt.verify(token, key);
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const events = db.collection('Events');
        var event = await events.find({ id: idNum }).toArray()
        const open_events = db.collection('OpenChats')
        var open_event = await open_events.find({ id: idNum }).toArray()
        if (open_event.length == 1) {
            await open_events.deleteOne({ id: idNum })
        }
        if (event.length == 1) {
            title = event[0].title

            if (event[0].users.length == 1) {
                await events.deleteOne({ id: idNum })
            }
            else {
                await events.updateOne(
                    { "id": idNum },
                    { $pull: { "users": username.username } }
                )
                await events.updateOne(
                    { "id": idNum },
                    { $pull: { "alert": { "username": username.username } } }
                )
            }
            const json={
                title: title
            }
            return JSON.stringify(json)
        }
        else {
            const json={
                title: "falseee"
            }
            return JSON.stringify(json)
        }
    }
    finally {
        await client.close();
    }
}

async function updateAll(id, token, title, start, end, date, alert, description, requestCode) {
    updateTitle(id, title)
    updateDescription(id, description)
    updateDate(id, date)
    updateStart(id, start)
    updateEnd(id, end)
    updateAlert(id, token, alert)
    let intRequestCode = parseInt(requestCode)
    updateRequestCode(id, token, intRequestCode)
}

async function updateAriaResult(id, start, date, token, requestCode) {
    const MINUTE = 60
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const key = "secret key"
        const username = jwt.verify(token, key);
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const open_events = db.collection('OpenChats')
        const closed_events = db.collection('ClosedChats');
        const events = db.collection('Events')
        var open_event = await open_events.find({ id: idNum }).toArray()
        if (open_event.length == 1) {
            await closed_events.insertOne({ id: idNum, username: username.username });
            await open_events.deleteOne({ id: idNum })
        }
        updateDate(id, date)
        updateStart(id, start)
        var event = await events.find({ id: idNum }).toArray()
        var duration_treatment = event[0].end
        let [startHours, startMinutes] = start.split(':').map(Number);
        let [durationHours, durationMinutes] = duration_treatment.split(':').map(Number);
        let totalMinutes = (startHours * MINUTE + startMinutes) + (durationHours * MINUTE + durationMinutes);
        let endHours = Math.floor(totalMinutes / MINUTE);
        let endMinutes = totalMinutes % MINUTE;
        let formattedEndHours = String(endHours).padStart(2, '0');
        let formattedEndMinutes = String(endMinutes).padStart(2, '0');
        const result = `${ formattedEndHours }:${ formattedEndMinutes }`
        updateEnd(id, result)
        let a = ""
        for (var j = 0; j < event[0].alert.length; j++) {
            if (event[0].alert[j].username == username.username) {
                a = event[0].alert[j].alert
                if (a !== "None") {
                    await events.updateOne(
                        { "id": idNum, "alert.username": username.username },
                        {
                            $set: {
                                "alert.$.requestCode": requestCode
                            }
                        }
                    );
                }
            }
        }
        const json = {
            flag: event[0].flag,
            title: event[0].title,
            description: event[0].description,
            alertString: a,
            end: result
        }

        return JSON.stringify(json)
    }
    finally {
        await client.close();
    }
}

async function idGoogle(idEvent, token) {
    const ERROR = "-1"
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const googleEvents = db.collection('GoogleEvents');
        const key = "secret key"
        const username = jwt.verify(token, key);
        const name = username.username;
        let result = await googleEvents.find({ user: name }).toArray();
        if (result.length > 0) {
            for (var i = 0; i < result.length; i++) {
                if (result[i].idAria == idEvent) {
                    var idGoogle = result[i].idGoogle
                    return idGoogle.toString();
                }
            }
            return ERROR;
        }
        else {
            return ERROR;
        }
    }
    finally {
        await client.close();
    }
}

async function addGoogleEvent(idEvent, idGoogleEvent, token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const googleEvents = db.collection('GoogleEvents');
        try {
            const key = "secret key"
            const username = jwt.verify(token, key);
            const name = username.username;
            await googleEvents.insertOne({ idAria: idEvent, idGoogle: idGoogleEvent, user: name });
            return true;
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}

async function deleteGoogleEvent(id, token) {
    const CORRECT = 0
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const key = "secret key"
        const username = jwt.verify(token, key);
        const name = username.username;
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const googleEvents = db.collection('GoogleEvents');
        var result = await googleEvents.find({ user: name }).toArray()
        if (result.length > 0) {
            for (var i = 0; i < result.length; i++) {
                if (result[i].idAria == idNum) {
                    await googleEvents.deleteOne({ idAria: idNum, user: name })
                }
            }
            return CORRECT;
        }
        else {
            return false;
        }
    }
    finally {
        await client.close();
    }
}

export default {
    addEvent,
    joinEvent,
    updateTitle,
    updateDescription,
    updateDate,
    updateStart,
    updateEnd,
    deleteEventById,
    updateAlert,
    updateAll,
    updateAriaResult,
    checkPhones,
    delete_members_invitation,
    getMembersNotifications,
    addGoogleEvent,
    idGoogle,
    deleteGoogleEvent
}
