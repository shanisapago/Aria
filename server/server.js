import bodyParser from 'body-parser';
import express from 'express';
import routerUser from './routes/User.js'
import routerEvent from './routes/Event.js'
import routerToken from './routes/Tokens.js'
import routerChat from './routes/Chats.js'
import routerFirebase from './routes/Firebase.js'
import cors from 'cors'

const server = express()
server.use(bodyParser());
server.use('/Events', routerEvent);
server.use('/Users', routerUser);
server.use('/Tokens', routerToken);
server.use('/Chats', routerChat);
server.use('/Firebase', routerFirebase);
server.use(cors());
server.listen(3000);
