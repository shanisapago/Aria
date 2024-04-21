import express from 'express';
import { addUser, getEvents, userCheck } from '../controllers/User.js';
//import { addUser, userDetails } from '../controllers/User.js';
console.log("inRoutes")
const router = express.Router();
router.post('/',addUser);
router.get('/', getEvents);
router.put('/Phones', userCheck);

export default router