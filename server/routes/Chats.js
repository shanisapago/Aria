import express from 'express';
import { addMessage, addChat, deleteChat } from '../controllers/Chats.js';
const router = express.Router();
console.log("inRoutes chats ")
router.post('/AddMessage', addMessage);
router.post('/AddChat', addChat);
router.delete('/:id/:token', deleteChat);
export default router