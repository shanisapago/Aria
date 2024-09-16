import FirebaseModel from '../models/Firebase.js'

async function sendMessage(req, res) {
    const ERROR_CODE = 401
    const result = await FirebaseModel.sendMessage(req.body.title, req.body.description, req.body.token);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

export {
    sendMessage
}