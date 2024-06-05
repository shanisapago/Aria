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
    const result = await ChatModel.deleteChat(req.params.id, req.params.token);
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
export {
    addMessage,
    addChat,
    deleteChat
}