import express from 'express';
import { addUser, getEvents, checkUsername, updateToken, getTimes, updateCalendarTimes, getTimesAriaSort, updateAriaTimes } from '../controllers/User.js';
const router = express.Router();
router.post('/', addUser);
router.get('/CheckUsername', checkUsername)
router.get('/', getEvents);
router.put('/:id/UpdateToken', updateToken)
router.get('/:day/GetTimes', getTimes);
router.get('/GetTimesAriaSort', getTimesAriaSort);
router.put('/CalendarTimes', updateCalendarTimes);
router.put('/AriaTimes', updateAriaTimes);
export default router
