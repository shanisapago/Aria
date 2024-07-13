import FirebaseModel from '../models/Firebase.js'



async function sendMessage(req, res) {
    console.log("in controllers send message Firebase")
    const result = await FirebaseModel.sendMessage(req.body.title, req.body.description, req.body.token);
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
export{
    sendMessage
}