import UserModel from '../models/User.js'

async function addUser(req,res){
    console.log("in controllers")
    const result = await UserModel.addUser(req.body.username,req.body.password, req.body.phoneNumber);
    if (!result){
        res.status(409);
    }
    res.end();
}

async function getEvents (req,res){
    console.log("in controllers get events")
    //console.log(req)
    //console.log("////////////////////////////////////////////////////////")
    //console.log(req.params.id)
    const result = await UserModel.getEvents(req.headers.authorization);
    //console.log("result:")
    //console.log(result)
    //const result = await UserModel.getEvents(req.headers.authorization.split(" ")[1], req.params.id);
    if (result==0){
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
async function userCheck(req, res) {
    console.log("in controllers check user")
    const result = await UserModel.checkUser(req.body.phones);
    if (!result) {
        res.status(409);
    }
    console.log("controller result")
    console.log(result)
    console.log(JSON.stringify(result))
    res.end(JSON.stringify(result));
}

export{
    addUser,
    getEvents,
    userCheck
}