import express from 'express';
import { addEvent, deleteByDate, updateTitle, updateDescription, updateDate, updateStart, updateEnd, joinEvent, updateAlert, deleteEventById, updateAll, updateAriaResult, checkPhones, delete_members_invitation, getMembersNotifications, addGoogleEvent, idGoogle, deleteGoogleEvent } from '../controllers/Event.js';
const router = express.Router();
console.log("inRoutes events")
router.post('/', addEvent);
router.post('/checkPhones', checkPhones);
router.post('/Join', joinEvent);
router.put('/:id/Title', updateTitle);
router.put('/:id/Description', updateDescription);
router.put('/:id/Date', updateDate);
router.put('/:id/Start', updateStart);
router.put('/:id/End', updateEnd);
router.delete('/:id/Date', deleteByDate);
router.put('/:id/Alert', updateAlert)
router.put('/:id/All', updateAll)
router.put('/:id/AriaResult', updateAriaResult )
router.delete('/:id/:username/DeleteById', deleteEventById)
router.delete('/:id/:username/DeleteInvitation', delete_members_invitation)
router.get('/getMembersNotifications', getMembersNotifications);
router.post('/AddGoogleEvent', addGoogleEvent);
router.put('/GoogleEvent', idGoogle)
router.delete('/GoogleEvent/:id/:username/', deleteGoogleEvent)
export default router
