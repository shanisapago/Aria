import ChatModel from '../models/Chats.js'



async function addChat(req, res) {
    console.log("in controllers add chat")
    const result = await ChatModel.addChat(req.body.id, req.body.phone, req.body.time, req.body.msg1, req.body.msg2, req.body.token);
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
async function deleteChat(req, res) {
    //console.log("in controllers add event")
<<<<<<< HEAD
    const result = await ChatModel.deleteChat(req.params.id,  req.params.token);
=======
    const result = await ChatModel.deleteChat(req.params.id, req.params.token);
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
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

async function addMessage(req, res) {
    console.log("in controllers add message")
    //const result = await EventModel.addEvent(req.params.id, req.headers.authorization.split(" ")[1], req.body.title, req.body.description, req.body.date, req.body.username, req.body.start, req.body.end, req.body.alert);
    const result = await ChatModel.addMessage(req.body.phone, req.body.token, req.body.message, req.body.time);
    console.log("result in controller")
    console.log(result)
    console.log(typeof(result))
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
<<<<<<< HEAD
async function getOpenClosedLst(req, res) {
    console.log("in controllers get events")
    //console.log(req)
    //console.log("////////////////////////////////////////////////////////")
    //console.log(req.params.id)
    const result = await ChatModel.getOpenClosedLst(req.headers.authorization);
    //console.log("result:")
    //console.log(result)
    //const result = await UserModel.getEvents(req.headers.authorization.split(" ")[1], req.params.id);
    if (result == 0) {
        res.status(401);
        res.end();
    }
    else {

        //res.end("shani");
        //console.log("result:")
        //console.log(result)
        //console.log("////////////////////////////////////////////////////////")
        //console.log(JSON.stringify(result))
        res.end(JSON.stringify(result));
    }
}
export {
    addMessage,
    addChat,
    deleteChat,
    getOpenClosedLst
=======
export {
    addMessage,
    addChat,
    deleteChat
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
}