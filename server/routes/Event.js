import express from 'express';
import { addEvent, deleteByDate, updateTitle, updateDescription, updateDate, updateStart, updateEnd, joinEvent, updateAlert, deleteEventById, updateAll } from '../controllers/Event.js';
const router = express.Router();
console.log("inRoutes events")
router.post('/', addEvent);
router.post('/Join', joinEvent);
router.put('/:id/Title', updateTitle);
router.put('/:id/Description', updateDescription);
router.put('/:id/Date', updateDate);
router.put('/:id/Start', updateStart);
router.put('/:id/End', updateEnd);
router.delete('/:id/Date', deleteByDate);
router.put('/:id/Alert', updateAlert)
router.put('/:id/All', updateAll)
router.delete('/:id/:username/DeleteById', deleteEventById)
export default router