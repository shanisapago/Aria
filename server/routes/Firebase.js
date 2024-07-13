import express from 'express';
import { sendMessage } from '../controllers/Firebase.js';
const router = express.Router();
console.log("inRoutes Firebase ");
router.post('/', sendMessage);
export default router