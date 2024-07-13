import EventModel from '../models/Event.js'


async function addEvent(req, res) {
    //console.log("in controllers add event")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.addEvent(req.body.title, req.body.description, req.body.date, req.body.token, req.body.start, req.body.end, req.body.alertString, req.body.flag);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        //console.log("in else")
        //console.log(result)
        res.end(result);
    }

}
async function checkPhones(req, res) {
    console.log("in controllers checkPhones")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.checkPhones(req.body.title, req.body.description, req.body.date, req.body.token, req.body.start, req.body.end, req.body.alertString, req.body.phones, req.body.tok, req.body.flag);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        console.log("in else check phones")
        console.log(JSON.stringify(result))
        res.end(JSON.stringify(result));
    }

}
async function getMembersNotifications(req, res) {
    console.log("in controllers get members notifications")
    //console.log(req)
    //console.log("////////////////////////////////////////////////////////")
    //console.log(req.params.id)
    const result = await EventModel.getMembersNotifications(req.headers.authorization);
    //console.log("result:")
    //console.log(result)
    //const result = await UserModel.getEvents(req.headers.authorization.split(" ")[1], req.params.id);
    if (result == 0) {
        console.log("return null")
        res.status(401);
        res.end();
    }
    else {

        //res.end("shani");
        //console.log("result:")
        //console.log(result)
        console.log("////////////////////////////////////////////////////////")
        //console.log(JSON.stringify(result))
        res.end(JSON.stringify(result));
    }
}
async function delete_members_invitation(req, res) {
    console.log("in delete controllers")
    var id = req.params.id
    console.log(id)
    const resDelete = await EventModel.delete_members_invitation(id, req.params.username)
    if (!resDelete)
        res.status(404)
    else
        res.status(204)
    res.end()
}

async function joinEvent(req, res) {
    console.log("in controllers join event")
    console.log(req.body.users)
    console.log(req.body.users[0])
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.joinEvent(req.body.id, req.body.users, req.body.alert);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        //console.log("in else")
        console.log(result)
        res.end(result);
    }

}

async function deleteByDate(req,res){
    //console.log("in controllers delete date")
    const result = await EventModel.deleteByDate(req.params.id);
    if (result == 1) {
        res.status(401)
        res.end();
    }
    else {
        res.end();
    }
}

async function updateTitle(req, res) {
    //console.log("in update title")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    //console.log(req.body.title)
    //console.log(req.params.id)
    const result = await EventModel.updateTitle(req.params.id, req.body.title);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}
async function updateDescription(req, res) {
    //console.log("in update description")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.updateDescription(req.params.id, req.body.description);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}
async function updateDate(req, res) {
    //console.log("in update date")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.updateDate(req.params.id, req.body.date);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}
async function updateStart(req, res) {
    //console.log("in update start")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.updateStart(req.params.id, req.body.start);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}

async function updateEnd(req, res) {
    //console.log("in update end")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.updateEnd(req.params.id, req.body.end);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}

/*async function updateAlert(req, res) {
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.updateAlert(req.body.id, req.body.username, req.body.alert);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}*/

async function updateAlert(req, res) {
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    console.log("in controllers updateAlert")
    const result = await EventModel.updateAlert(req.params.id, req.body.username, req.body.alert);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}
async function updateAll(req, res) {
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    console.log("in controllers updateAll")
    const result = await EventModel.updateAll(req.params.id, req.body.token, req.body.title, req.body.start, req.body.end, req.body.date, req.body.alert, req.body.description);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {

        res.end();
    }

}
async function updateAriaResult(req, res) {
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    console.log("in controllers updateAll")
    const result = await EventModel.updateAriaResult(req.params.id, req.body.start, req.body.date,req.body.token);
    console.log(result)
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        console.log(result)
        res.end(result);
    }

}




async function deleteEventById(req, res) {
    console.log("in delete controllers")
    var id = req.params.id
    console.log(id)
    const resDelete = await EventModel.deleteEventById(id, req.params.username)
    if (!resDelete)
        res.status(404)
    else
        res.status(204)
    res.end()

}
async function addGoogleEvent(req, res) {
    //console.log("in controllers add event")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await EventModel.addGoogleEvent(req.body.idEvent, req.body.idGoogleEvent, req.body.token);
    if (!result) {
        res.status(401)
        res.end();
    }
    else {
        //console.log("in else")
        //console.log(result)
        res.end();
    }

}

async function idGoogle(req, res) {
    console.log("in controllers idGoogle")
    const result = await EventModel.idGoogle(req.body.idEvent, req.body.token);
    /*if (result == -1) {
        //console.log("there is user in this username")
        res.status(409);
    }*/
    res.end(result);
}

async function deleteGoogleEvent(req, res) {
    console.log("in delete google event controllers")
    var id = req.params.id
    console.log(id)
    const resDelete = await EventModel.deleteGoogleEvent(id, req.params.username)
    if (!resDelete)
        res.status(404)
    else
        res.status(204)
    res.end()
}



export{
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
    updateAriaResult,
    checkPhones,
    delete_members_invitation,
    getMembersNotifications,
    addGoogleEvent,
    idGoogle,
    deleteGoogleEvent
}
