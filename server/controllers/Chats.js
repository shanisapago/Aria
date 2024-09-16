import ChatModel from '../models/Chats.js'

async function addChat(req, res) {
    const ERROR_CODE = 401
    const result = await ChatModel.addChat(req.body.id, req.body.phone, req.body.time, req.body.msg1, req.body.msg2, req.body.token);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

async function addMessage(req, res) {
    const ERROR_CODE = 401
    const result = await ChatModel.addMessage(req.body.phone, req.body.token, req.body.message, req.body.time);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

async function getOpenClosedLst(req, res) {
    const ERROR_CODE = 401
    const result = await ChatModel.getOpenClosedLst(req.headers.authorization);
    if (!result) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

export {
    addMessage,
    addChat,
    getOpenClosedLst
}