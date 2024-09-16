import express from 'express';
import { addEvent, joinEvent, deleteEventById, updateAll, updateAriaResult, checkPhones, delete_members_invitation, getMembersNotifications, addGoogleEvent, idGoogle, deleteGoogleEvent } from '../controllers/Event.js';
const router = express.Router();
router.post('/', addEvent);
router.post('/checkPhones', checkPhones);
router.post('/Join', joinEvent);
router.put('/:id/All', updateAll)
router.put('/:id/AriaResult', updateAriaResult)
router.put('/:id/:username/DeleteById', deleteEventById)
router.delete('/:id/:username/DeleteInvitation', delete_members_invitation)
router.get('/getMembersNotifications', getMembersNotifications);
router.post('/AddGoogleEvent', addGoogleEvent);
router.put('/GoogleEvent', idGoogle)
router.delete('/GoogleEvent/:id/:username/', deleteGoogleEvent)
export default router
