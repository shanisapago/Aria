import express from 'express';
<<<<<<< HEAD
import { addMessage, addChat, deleteChat, getOpenClosedLst } from '../controllers/Chats.js';
const router = express.Router();
console.log("inRoutes chats ");
router.post('/AddMessage', addMessage);
router.post('/AddChat', addChat);
router.delete('/:id/:token', deleteChat);
router.get('/GetAriaList', getOpenClosedLst)
=======
import { addMessage, addChat, deleteChat } from '../controllers/Chats.js';
const router = express.Router();
console.log("inRoutes chats ")
router.post('/AddMessage', addMessage);
router.post('/AddChat', addChat);
router.delete('/:id/:token', deleteChat);
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
export default router