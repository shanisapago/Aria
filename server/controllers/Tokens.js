import TokenModel from '../models/Tokens.js'


async function createToken(req, res) {
    console.log("in token controllers")
    const result = await TokenModel.createToken(req.body.username,req.body.password);
    if (result==0){
        res.status(404);
        res.end();
    }
    else{
        res.end( result );
    }
}

export{
    createToken
}