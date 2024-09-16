import { MongoClient } from 'mongodb';
import jwt from 'jsonwebtoken';
async function createToken(username, password) {
    const ERROR = 0
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const usersPass = db.collection('User');
        let result = await usersPass.find({ username: username }).toArray();
        if (result.length === 1) {
            if (result[0].password === password) {
                const key = "secret key"
                const data = { username: username }
                const token = jwt.sign(data, key)
                let res = {
                    "token": token,
                    "fullName": result[0].fullName
                }
                return JSON.stringify(res)
            }
        }
    }
    finally {
        await client.close();
    }
    return ERROR;
}

export default {
    createToken
}