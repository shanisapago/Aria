import express from 'express';
import { sendMessage } from '../controllers/Firebase.js';
const router = express.Router();
router.post('/', sendMessage);
export default router