import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
//async function addEvent(id, token, title, description, date, username, start, end, alert){
async function addEvent(title, description, date, token, start, end, alertString, flag) {
    //var idNum=parseInt(id);
    console.log("in model add event")
    console.log(token)
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        //********** */
        //const members_invitation = db.collection('MembersInvitation');
        try {
            //console.log(token)
            const key = "secret key"
            const username = jwt.verify(token, key);
            //console.log(username)
            const userArray = [username.username];
            const alertWuser = [{ alert: alertString, username: username.username }]
            let event = await events.find({}).toArray();
            var eventPlace = parseInt(event.length);
            var eventId = eventPlace + 1;
            var id = eventId.toString();
<<<<<<< HEAD
            await events.insertOne({ id: eventId, title: title, description: description, date: date, users: userArray, start: start, end: end, alert: alertWuser, chat: "" , flag: flag});
            //const id = {
            //    id: eventId
            // }
            //add to collection
            //************************ */
            //let msg = "you are invited to event " + title + " on " + date + " at " + start + ". you are invited by " + username.username
            //console.log(phones)
            //if (phones != null) {
            //    for (var u = 0; u < phones.length; u++) {
            //        await members_invitation.insertOne({ id: id, username: phones[u].username, phone: phones[u].phone, message: msg });
            //    }
            //}
            
            console.log("finish")
            console.log(id)
=======
            await events.insertOne({ id: eventId, title: title, description: description, date: date, users: userArray, start: start, end: end, alert: alertWuser, chat: "" });
            //const id = {
            //    id: eventId
            // }
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
            return id;
            //}

        }
        catch (err) {
            console.log("error")
            console.log(err)
            return false;
        }
    }
    finally {
        await client.close();
    }
}
async function checkPhones(title, description, date, token, start, end, alertString, phones, tok, flag) {
    //var idNum=parseInt(id);
    console.log("in model check phones event")
    console.log(flag)
    console.log(tok)
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
            //console.log(token)
            const key = "secret key"
            const username = jwt.verify(token, key);
            //console.log(username)
            console.log("1")
            
            
            console.log("2")
            console.log(phones)
            //if(phones == null){
               // console.log("null")
            //}
           // else{
             //   console.log("not null")
           // }
            //console.log(phones.length)
            if(phones == null){
                console.log("in null")
                id = await addEvent(title, description, date, token, start, end, alertString, flag)
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
            else{
                for (var u = 0; u < phones.length; u++) {
                    console.log("3")
                    let user = await users.findOne({
                        phoneNumber: phones[u].phone
                    });
                    console.log("4")
                    //check if there is user in this phone
                    if (user) {
                        console.log("5")
                        const res = {
                            username: user.username,
                            phone: user.phoneNumber
                        }
                        users_with_phones.push(res)
                        console.log("***************************************")
                        console.log(user)
                        console.log(user.appToken)
                        appTokens.push(user.appToken)
                    }
                    else {
                        console.log("6")
                        array_of_not_users.push(phones[u])
                    }
                }
                console.log("appTokens")
                console.log(username.username)
                console.log(appTokens)
                if (users_with_phones.length == phones.length || phones.length == 0) {
                    id = await addEvent(title, description, date, token, start, end, alertString, flag)
                    const members_invitation = db.collection('MembersInvitation');
                    let msg = "you are invited to event " + title + " on " + date + " at " + start + ". you are invited by " + username.username
                    console.log(phones)
                    if (phones.length != 0) {
                        for (var u = 0; u < phones.length; u++) {
                            await members_invitation.insertOne({ id: id, name: phones[u].name, phone: phones[u].phone, message: msg, appToken : appTokens[u], tokenSender : tok, sender : username.username, title : title, username: users_with_phones[u].username });
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
                    else{
                        const json = {
                            id: id,
                            notUsers: array_of_not_users,
                            appToken: "",
                            sender: "",
                            title: ""
                        }
                        arrayMembers.push(json)
                    }
    
    
                    //return id return as json
                    console.log("zzz")
    
    
    
                    //const json = {
                    //    id: id
    
                    //}
                    console.log("zzz2")
                    return arrayMembers;
                    //return JSON.stringify(json);
                }
                else {
                    //return json with id = -1
                    console.log("else....")
                    //var jsonNotUsers = JSON.parse(array_of_not_users)
                    const json = {
                        id: -1,
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
            console.log("error")
            console.log(err)
            return false;
        }
    }
    finally {
        await client.close();
    }
}

async function delete_members_invitation(id, username) {
    console.log("in delete models")
    
    
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        
        var idNum = parseInt(id);
        console.log(idNum)
        console.log(username)
        const db = client.db('Aria');
        const members_invitation = db.collection('MembersInvitation');
        var invitaion = await members_invitation.find({ id: id, username:username }).toArray()
        console.log(invitaion.length)
        if (invitaion.length == 1) {
            console.log("delete from invitation")
            await members_invitation.deleteOne({ id: id, username: username })

        }

        }

    
    finally {

        await client.close();
    }
}
async function getMembersNotifications(token) {
    console.log("in models get members notifications")
    var arrayMembersNotifications = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const members_invitation = db.collection('MembersInvitation');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            //console.log("data")
            //console.log(data)
            let result = await members_invitation.find({username:data.username}).toArray();
            //console.log(members_invitation.find())
            console.log(data.username)
            console.log(result.length)
            for (var i = 0; i < result.length; i++) {
                console.log("in loop")
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
            console.log("error")
            console.log(err)
            return 0;
        }
    }
    finally {
        await client.close();
    }
}





async function joinEvent(id, username, alert) {
    var idNum = parseInt(id);
    console.log("in model join event")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            console.log("username")
            console.log(username[0])
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
                    console.log(username[0])
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
                console.log("return the e")
                return JSON.stringify(e);
            }

        }
        catch (err) {
            console.log(err)
            return false;
        }
    }
    finally {
        await client.close();
    }
}

//async function deleteByDate(token, date){
async function deleteByDate(date){
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        let maxYear = parseInt(date.substring(6,10));
        let maxMonth = parseInt(date.substring(3,5));
        let maxDay = parseInt(date.substring(0,2));
        try{
            //const key = "secret key"
            //const data = jwt.verify(token, key);
            let allEvents = await events.find({}).toArray();
            for (var i=0;i<allEvents.length;i++){
                    let year = parseInt(allEvents[i].date.substring(6,10));
                    let month = parseInt(allEvents[i].date.substring(3,5));
                    let day = parseInt(allEvents[i].date.substring(0,2));
                    if(maxYear > year){
                        await events.deleteOne({id : allEvents[i].id});
                    } else if (maxYear == year){
                        if (maxMonth > month){
                            await events.deleteOne({id : allEvents[i].id});
                        } else if (maxMonth == month){
                            if(maxDay > day){
                                await events.deleteOne({id : allEvents[i].id});
                            }
                        }
                    }
            }
            return 0;
        }
        catch(err){
            return 1;
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
            //const key = "secret key"
            //const data = jwt.verify(token, key);
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
async function updateDescription(id,description ) {
    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            //const key = "secret key"
            //const data = jwt.verify(token, key);
            let event = await events.find({ id: idNum }).toArray();
            if (event.length == 1) {

                await events.updateOne(
                    { id: idNum },
                    {
                        $set: {description:description } }
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
    console.log("date")
    console.log(date)

    var idNum = parseInt(id);
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try {
            //const key = "secret key"
            //const data = jwt.verify(token, key);
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
            //const key = "secret key"
            //const data = jwt.verify(token, key);
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
            //const key = "secret key"
            //const data = jwt.verify(token, key);
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
    console.log("in models updateAlert")
    //console.log(username)
    console.log(alert)
    console.log(id)
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
                console.log("there is a event in this id")
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
                console.log("not find")
                return false;

            }

        }
        catch (err) {
            console.log("error")
            console.log(err)
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
        const key = "secret key"
        const username = jwt.verify(token, key);
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const events = db.collection('Events');
        var event = await events.find({ id: idNum }).toArray()
        const open_events = db.collection('OpenChats')
<<<<<<< HEAD
        
=======
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
        var open_event = await open_events.find({ id: idNum }).toArray()
        console.log(id)
        console.log("event len")
        console.log(event.length)
        console.log(open_event.length)
        console.log(username)

        if (open_event.length == 1) {
            console.log("delete from open")
            await open_events.deleteOne({ id: idNum })
<<<<<<< HEAD
            


=======
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
        }


        //there is event in this id
        if (event.length == 1) {
            console.log("find event")
            //there is only one user-> we want to delete the event
            if (event[0].users.length == 1) {
                console.log("there is only one user")
                await events.deleteOne({ id: idNum })

            }
            else {
                //there are more than one user-> we need to update the users list and the alert list
                console.log("there are more than one user")
                console.log(username)
                await events.updateOne(
                    { "id": idNum },
                    { $pull: { "users": username.username } }
                )

                await events.updateOne(
                    { "id": idNum },
                    { $pull: { "alert": { "username": username.username } } }
                )

               

            }
        }

   

        else {
            return true

        }
    }
    finally {

        await client.close();
    }
}

async function updateAll(id, token, title, start, end, date, alert, description) {
    updateTitle(id, title)
    updateDescription(id, description)
    updateDate(id, date)
    updateStart(id, start)
    updateEnd(id, end)
    updateAlert(id, token, alert)
}
<<<<<<< HEAD
async function updateAriaResult(id, start, date,token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const key = "secret key"
        const username = jwt.verify(token, key);
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const open_events = db.collection('OpenChats')
        const closed_events = db.collection('ClosedChats');
        const events = db.collection('Events')
=======
async function updateAriaResult(id, start, end, date) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const open_events = db.collection('OpenChats')
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
        var open_event = await open_events.find({ id: idNum }).toArray()
        console.log(open_event.length)
        console.log(idNum)
        console.log(id)
        if (open_event.length == 1) {
            console.log("delete from open")
<<<<<<< HEAD
            await closed_events.insertOne({ id: idNum, username: username.username });
            await open_events.deleteOne({ id: idNum })
            
        }
        updateDate(id, date)
        updateStart(id, start)

        var event = await events.find({ id: idNum }).toArray()
        var duration_treatment = event[0].end
        console.log(duration_treatment)

        const [hours1, minutes1] = duration_treatment.split(':').map(Number);
        const [hours2, minutes2] = start.split(':').map(Number);

        const totalMinutes1 = hours1 * 60 + minutes1;
        const totalMinutes2 = hours2 * 60 + minutes2;

        const totalMinutes = totalMinutes1 + totalMinutes2;


        const resultHours = Math.floor(totalMinutes / 60);
        const resultMinutes = totalMinutes % 60;

        const formattedHours = String(resultHours).padStart(2, '0');
        const formattedMinutes = String(resultMinutes).padStart(2, '0');

        const result = '${formattedHours}:${formattedMinutes}';
        console.log(result)
        updateEnd(id, result)
        //var updatedEvent = await events.find({ id: idNum }).toArray()
        console.log("eventttt")  
        console.log(event)
        const json = {
            flag: event[0].flag,
            title: event[0].title,
            description: event[0].description,
            end: formattedHours+":"+formattedMinutes
        }

        return JSON.stringify(json)
    }
    finally {

        await client.close();
    }
}
async function idGoogle(idEvent, token) {
    console.log("in id Google models")
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
            return "-1";
        }
        else {
            return "-1";
        }
        //if (result.length === 1) {
        //    return false;
        // }
        // else {
        //     return true;
        // }
    }
    finally {
        await client.close();
    }
}

async function addGoogleEvent(idEvent, idGoogleEvent, token) {
    //var idNum=parseInt(id);
    console.log("in model add google event")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const googleEvents = db.collection('GoogleEvents');
        try {
            //console.log(token)
            const key = "secret key"
            console.log(token)
            const username = jwt.verify(token, key);
            console.log(username)
            const name = username.username;
            console.log(idEvent)
            console.log(idGoogleEvent)
            console.log(name)
            await googleEvents.insertOne({ idAria: idEvent, idGoogle: idGoogleEvent, user: name });
            //const id = {
            //    id: eventId
            // }
            return true;
            //}

        }
        catch (err) {
            console.log(err)
            return false;
        }
    }
    finally {
        await client.close();
    }
}


async function deleteGoogleEvent(id, token) {
    console.log("delete google")
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
                    console.log(result[i])
                    await googleEvents.deleteOne({ idAria: idNum, user: name })
                }
            }
            return 0;
        }
        else {
            return false;
        }
=======
            await open_events.deleteOne({ id: idNum })
        }
        updateDate(id, date)
        updateStart(id, start)
        updateEnd(id, end)
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
    }
    finally {

        await client.close();
    }
}

export default {
    addEvent,
    joinEvent,
    deleteByDate,
    updateTitle,
    updateDescription,
    updateDate,
    updateStart,
    updateEnd,
    deleteEventById,
    updateAlert,
    updateAll,
<<<<<<< HEAD
    updateAriaResult,
    checkPhones,
    delete_members_invitation,
    getMembersNotifications,
    addGoogleEvent,
    idGoogle,
    deleteGoogleEvent
}
=======
    updateAriaResult
}
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
