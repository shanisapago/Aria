import {MongoClient} from 'mongodb';
import jwt from 'jsonwebtoken';
//async function addEvent(id, token, title, description, date, username, start, end, alert){
async function addEvent(title, description, date, token, start, end, alertString) {
    //var idNum=parseInt(id);
    console.log("in model add event")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
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
            await events.insertOne({ id: eventId, title: title, description: description, date: date, users: userArray, start: start, end: end, alert: alertWuser, chat: "" });
            //const id = {
            //    id: eventId
            // }
            return id;
            //}

        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}
async function joinEvent(id, username, alert){
    var idNum=parseInt(id);
    console.log("in model join event")
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const events = db.collection('Events');
        try{
           
            let event = await events.find({id:idNum}).toArray();
            if (event.length == 1)  {

                await events.updateOne(
                    { id: idNum },
                     {$push:{ users: username[0] } }
                );
                await events.updateOne(
                    { id: idNum },
                    { $push: { alert: alert[0] } }
                );
            }
               
        }
        catch(err){
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
        var open_event = await open_events.find({ id: idNum }).toArray()
        console.log(id)
        console.log("event len")
        console.log(event.length)
        console.log(open_event.length)
        console.log(username)

        if (open_event.length == 1) {
            console.log("delete from open")
            await open_events.deleteOne({ id: idNum })
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
async function updateAriaResult(id, start, end, date) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        var idNum = parseInt(id);
        const db = client.db('Aria');
        const open_events = db.collection('OpenChats')
        var open_event = await open_events.find({ id: idNum }).toArray()
        console.log(open_event.length)
        console.log(idNum)
        console.log(id)
        if (open_event.length == 1) {
            console.log("delete from open")
            await open_events.deleteOne({ id: idNum })
        }
        updateDate(id, date)
        updateStart(id, start)
        updateEnd(id, end)
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
    updateAriaResult
}