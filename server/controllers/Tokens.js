import TokenModel from '../models/Tokens.js'


async function createToken(req, res) {
    const ERROR_CODE = 404
    const result = await TokenModel.createToken(req.body.username, req.body.password);
    if (!result) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(result);
    }
}

export {
    createToken
}