import bodyParser from 'body-parser';
import express from 'express';
import http from 'http';
import routerUser from './routes/User.js'
import routerEvent from './routes/Event.js'
import routerToken from './routes/Tokens.js'
import routerChat from './routes/Chats.js'
import cors from 'cors'
//import { Server } from "socket.io";

const server = express()
server.use(bodyParser());
console.log("in server")
//server.use(express.static('public'))
server.use('/Events', routerEvent);
server.use('/Users', routerUser);
server.use('/Tokens', routerToken);
server.use('/Chats', routerChat);
server.use(cors());
server.listen(3000);

/*const app = express()
const server = http.createServer(app);
//npn const io = new Server(server);
app.use(bodyParser());
//app.use(express.static('public'))
app.use('/Events', routerEvent);
//app.use('/Users', routerUser);
app.use(cors());

server.listen(3000);*/