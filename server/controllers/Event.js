import EventModel from '../models/Event.js'

async function addEvent(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.addEvent(req.body.title, req.body.description, req.body.date, req.body.token, req.body.start, req.body.end, req.body.alertString, req.body.flag);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

async function checkPhones(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.checkPhones(req.body.title, req.body.description, req.body.date, req.body.token, req.body.start, req.body.end, req.body.alertString, req.body.phones, req.body.tok, req.body.flag, req.body.requestCode);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

async function getMembersNotifications(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.getMembersNotifications(req.headers.authorization);
    if (!result) {
        res.status(ERROR_CODE);
        res.end();
    }
    else {
        res.end(JSON.stringify(result));
    }
}

async function delete_members_invitation(req, res) {
    const ERROR_CODE = 404
    const CORRECT_CODE = 204
    const resDelete = await EventModel.delete_members_invitation(req.params.id, req.params.username)
    if (!resDelete)
        res.status(ERROR_CODE)
    else
        res.status(CORRECT_CODE)
    res.end()
}

async function joinEvent(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.joinEvent(req.body.id, req.body.users, req.body.alert);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

async function updateAll(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.updateAll(req.params.id, req.body.token, req.body.title, req.body.start, req.body.end, req.body.date, req.body.alert, req.body.description, req.body.requestCode);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end();
    }
}

async function updateAriaResult(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.updateAriaResult(req.params.id, req.body.start, req.body.date, req.body.token, req.body.requestCode);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end(result);
    }
}

async function deleteEventById(req, res) {
        const resDelete = await EventModel.deleteEventById(req.params.id, req.params.username);
        res.end(resDelete);
}

async function addGoogleEvent(req, res) {
    const ERROR_CODE = 401
    const result = await EventModel.addGoogleEvent(req.body.idEvent, req.body.idGoogleEvent, req.body.token);
    if (!result) {
        res.status(ERROR_CODE)
        res.end();
    }
    else {
        res.end();
    }
}

async function idGoogle(req, res) {
    const result = await EventModel.idGoogle(req.body.idEvent, req.body.token);
    res.end(result);
}

async function deleteGoogleEvent(req, res) {
    const ERROR_CODE = 404
    const CORRECT_CODE = 204
    const resDelete = await EventModel.deleteGoogleEvent(req.params.id, req.params.username)
    if (!resDelete)
        res.status(ERROR_CODE)
    else
        res.status(CORRECT_CODE)
    res.end()
}

export {
    addEvent,
    joinEvent,
    deleteEventById,
    updateAll,
    updateAriaResult,
    checkPhones,
    delete_members_invitation,
    getMembersNotifications,
    addGoogleEvent,
    idGoogle,
    deleteGoogleEvent
}
