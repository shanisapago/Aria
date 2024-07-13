import admin from 'firebase-admin'
import serviceAccount from '../models/aria-69b15-firebase-adminsdk-7o9ur-9c260f601a.json'assert { type: 'json' };;

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

async function sendMessage(title, description, token) {
console.log("in models sendMessage firebase");
const registrationToken = token;

const message = {
    notification: {
        title: title,
        body: description,
    },
    token: registrationToken
};

// Send a message to the device corresponding to the provided registration token
admin.messaging().send(message)
    .then((response) => {
        console.log('Successfully sent message:', response);
    })
    .catch((error) => {
        console.error('Error sending message:', error);
    });
}

export default {
    sendMessage
}
