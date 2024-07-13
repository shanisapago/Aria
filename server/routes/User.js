import express from 'express';
<<<<<<< HEAD
import { addUser, getEvents, userCheck, checkUsername } from '../controllers/User.js';
//import { addUser, userDetails } from '../controllers/User.js';
console.log("inRoutes")
const router = express.Router();
router.post('/', addUser);
router.get('/CheckUsername', checkUsername)
=======
import { addUser, getEvents, userCheck } from '../controllers/User.js';
//import { addUser, userDetails } from '../controllers/User.js';
console.log("inRoutes")
const router = express.Router();
router.post('/',addUser);
>>>>>>> 848f7af15d969dd6a2d6c7bb7d69ce781767d6bb
router.get('/', getEvents);
router.put('/Phones', userCheck);

export default router