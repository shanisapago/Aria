import UserModel from '../models/User.js'

async function checkUsername(req, res) {
    const ERROR_CODE = 409
    const result = await UserModel.checkUsername(req.headers.username);
    if (!result) {
        res.status(ERROR_CODE);
    }
    res.end();
}

async function addUser(req, res) {
    const ERROR_CODE = 409
    const result = await UserModel.addUser(req.body.username, req.body.password, req.body.phoneNumber, req.body.token, req.body.fullName);
    if (!result) {
        res.status(ERROR_CODE);
    }
    res.end();
}

async function getEvents(req, res) {
    const ERROR_CODE = 401
    const result = await UserModel.getEvents(req.headers.authorization);
    if (result == 0) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

async function updateToken(req, res) {
    const ERROR_CODE = 401
    const result = await UserModel.updateToken(req.params.id, req.body.token);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end();
    }
}

async function getTimes(req, res) {
    const ERROR_CODE = 401
    const result = await UserModel.getTimes(req.headers.authorization, req.params.day);
    if (!result) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

async function getTimesAriaSort(req, res) {
    const ERROR_CODE = 401
    const result = await UserModel.getTimesAriaSort(req.headers.authorization, req.params.day);
    if (!result) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

async function updateCalendarTimes(req, res) {
    const ERROR_CODE = 409
    const result = await UserModel.updateCalendarTimes(req.body.day, req.body.time, req.body.flag, req.body.token);
    if (!result) {
        res.status(ERROR_CODE);
    }
    res.end(JSON.stringify(result));
}

async function updateAriaTimes(req, res) {
    const ERROR_CODE = 409
    const result = await UserModel.updateAriaTimes(req.body.day, req.body.time, req.body.flag, req.body.token);
    if (!result) {
        res.status(ERROR_CODE);
    }
    res.end(JSON.stringify(result));
}

export {
    addUser,
    getEvents,
    checkUsername,
    updateToken,
    getTimes,
    updateCalendarTimes,
    updateAriaTimes,
    getTimesAriaSort
}