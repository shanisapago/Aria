import admin from 'firebase-admin'
import serviceAccount from '../models/aria-69b15-firebase-adminsdk-7o9ur-9c260f601a.json'assert { type: 'json' };;

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

async function sendMessage(title, description, token) {
    const registrationToken = token
    const message = {
        notification: {
            title: title,
            body: description,
        },
        token: registrationToken
    };
    admin.messaging().send(message)
}

export default {
    sendMessage
}
