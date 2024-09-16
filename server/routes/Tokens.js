import express from 'express';
import { createToken } from '../controllers/Tokens.js';
const router = express.Router();
router.post('/', createToken);

export default router