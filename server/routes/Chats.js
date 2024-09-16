import express from 'express';
import { addMessage, addChat, getOpenClosedLst } from '../controllers/Chats.js';
const router = express.Router();
router.post('/AddMessage', addMessage);
router.post('/AddChat', addChat);
router.get('/GetAriaList', getOpenClosedLst)
export default router
