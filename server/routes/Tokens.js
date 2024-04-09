import express from 'express';
import { createToken }from '../controllers/Tokens.js';
const router = express.Router();
console.log("in token routes")
router.post('/', createToken);

export default router