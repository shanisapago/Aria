import { MongoClient } from 'mongodb';
import jwt from 'jsonwebtoken';
async function checkUsername(username) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        let result = await user.find({ username: username }).toArray();
        if (result.length === 1) {
            return false;
        }
        else {
            return true;
        }
    }
    finally {
        await client.close();
    }
}

async function addUser(username, password, phoneNumber, token, fullName) {
    const DEFAULT_VALUE = 0
    const SUNDAY = 0
    const MONDAY = 1
    const TUESDAY = 2
    const WEDNWSDAY = 3
    const THURSDAY = 4
    const FRIDAY = 5
    const SATURDAY = 6
    phoneNumber = "+972" + phoneNumber
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        let e = 1
        let mat = [{ "day": SUNDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": MONDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": TUESDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": WEDNWSDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": THURSDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": FRIDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }, { "day": SATURDAY, "values": [{ "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }, { "time": "", "q_value": DEFAULT_VALUE }] }]
        let result = await user.find({ username: username }).toArray();
        if (result.length === 1) {
            return false;
        }
        await user.insertOne({ username: username, fullName: fullName, password: password, phoneNumber: phoneNumber, appToken: token, calendarTimes: mat, epsilon: e, ariaTimes: mat, ariaEpsilon: e });
    }
    finally {
        await client.close();
    }
    return true;
}

async function getEvents(token) {
    const FALSE = 0
    var arrayEvents = [];
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const event = db.collection('Events');
        const openChat = db.collection('OpenChats');
        const users_collection = db.collection('User');
        try {
            const key = "secret key"
            const data = jwt.verify(token, key);
            let result = await event.find({}).toArray();
            let resultChat = await openChat.find({}).toArray();
            for (var i = 0; i < result.length; i++) {
                var arrayUsersPhones = [];
                for (var u = 0; u < result[i].users.length; u++) {
                    let user = await users_collection.find({ "username": result[i].users[u] }).toArray()
                    arrayUsersPhones.push(user[0].phoneNumber)
                }
                let flagChat = true;
                for (var u = 0; u < result[i].users.length; u++) {
                    if (result[i].users[u] == data.username) {
                        for (var j = 0; j < result[i].alert.length; j++) {
                            if (result[i].alert[j].username == data.username) {
                                for (var k = 0; k < resultChat.length; k++) {
                                    if (resultChat[k].id == result[i].id) {
                                        flagChat = false;
                                    }
                                }
                                if (flagChat == true) {
                                    const res = {
                                        "id": result[i].id,
                                        "title": result[i].title,
                                        "description": result[i].description,
                                        "date": result[i].date,
                                        "phoneNumbers": arrayUsersPhones,
                                        "start": result[i].start,
                                        "end": result[i].end,
                                        "alertString": result[i].alert[j].alert,
                                        "requestCode": result[i].alert[j].requestCode
                                    }
                                    arrayEvents.push(res);
                                }
                            }
                        }
                    }
                }
            }
            return arrayEvents;
        }
        catch (err) {
            return FALSE;
        }
    }
    finally {
        await client.close();
    }
}

async function updateToken(id, token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const users = db.collection('User');
        try {
            let user = await users.find({ username: id }).toArray();
            if (user.length == 1) {
                await users.updateOne(
                    { username: id },
                    {
                        $set: {
                            appToken: token
                        }
                    }
                );
            }
            else {
                return false;
            }
        }
        catch (err) {
            return false;
        }
    }
    finally {
        await client.close();
    }
}

function getRandomRange() {
    const HOURS = 24
    const MINUTE = 60
    const TIME_JUMP = 5
    const FIFTH_HOUR = MINUTE / TIME_JUMP
    const ONE_DIGIT = 10
    const PADDING_ZERO = "0"
    const FIRST_SMALLER = 1
    const FIRST_BIGGER = 2
    let hourtime1 = Math.floor(Math.random() * HOURS)
    let minutestime1 = Math.floor(Math.random() * FIFTH_HOUR) * TIME_JUMP
    let hourtime2 = Math.floor(Math.random() * HOURS)
    let minutestime2 = Math.floor(Math.random() * FIFTH_HOUR) * TIME_JUMP
    while (hourtime1 == hourtime2 && minutestime1 == minutestime2) {
        hourtime2 = Math.floor(Math.random() * HOURS)
        minutestime2 = Math.floor(Math.random() * FIFTH_HOUR) * TIME_JUMP
    }
    let flagTime = FIRST_SMALLER
    if (hourtime1 > hourtime2) {
        flagTime = FIRST_BIGGER
    }
    else if (hourtime1 == hourtime2) {
        if (minutestime1 > minutestime2) {
            flagTime = FIRST_BIGGER
        }
    }
    let hourtime1str, minutestime1str, hourtime2str, minutestime2str, range
    hourtime1str = String(hourtime1)
    if (hourtime1 < ONE_DIGIT) {
        hourtime1str = PADDING_ZERO + hourtime1str
    }
    minutestime1str = String(minutestime1)
    if (minutestime1 < ONE_DIGIT) {
        minutestime1str = PADDING_ZERO + minutestime1str
    }
    hourtime2str = String(hourtime2)
    if (hourtime2 < ONE_DIGIT) {
        hourtime2str = PADDING_ZERO + hourtime2str
    }
    minutestime2str = String(minutestime2)
    if (minutestime2 < ONE_DIGIT) {
        minutestime2str = PADDING_ZERO + minutestime2str
    }
    if (flagTime === FIRST_SMALLER) {
        range = hourtime1str + ":" + minutestime1str + "-" + hourtime2str + ":" + minutestime2str
    }
    else {
        range = hourtime2str + ":" + minutestime2str + "-" + hourtime1str + ":" + minutestime1str
    }
    return range
}

async function getTimes(token, day) {
    const ERROR_RESULT = 1
    const NUMBER_OF_TIMES_OFFER = 3
    const NUMBER_OF_RANGES = 5
    const EPSILON_DISCOUNT = 0.95
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        try {
            const key = "secret key"
            const username1 = jwt.verify(token, key);
            const username = username1.username
            var arrayTimes = []
            let result = await user.find({ username: username }).toArray();
            if (result.length !== ERROR_RESULT) {
                return arrayTimes;
            }
            let rndNumber = 0, maxNumber = 0
            for (let i = 0; i < NUMBER_OF_TIMES_OFFER; i++) {
                let randomNumber = Math.random();
                if (randomNumber < result[0].epsilon) {
                    rndNumber++
                }
                else {
                    maxNumber++
                }
            }
            let counter = 0
            const dayItem = result[0].calendarTimes.filter(item => item.day == day);
            for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                if (dayItem[0].values[i].time != "") {
                    counter = counter + 1
                }
            }
            if (counter < maxNumber) {
                maxNumber = counter
                rndNumber = NUMBER_OF_TIMES_OFFER - counter
            }
            let maxstr = []
            for (let j = NUMBER_OF_RANGES - 1; j > 1; j--) {
                maxstr.push(dayItem[0].values[j].time)
            }
            for (let i = 0; i < rndNumber; i++) {
                let s = getRandomRange()
                while (s === maxstr[0] || s === maxstr[1] || maxstr[2] === s) {
                    s = getRandomRange()
                }
                arrayTimes.push(s)
            }
            for (let i = 0; i < maxNumber; i++) {
                arrayTimes.push(maxstr[i])
            }
          
            let new_epsilon = result[0].epsilon * EPSILON_DISCOUNT
            await user.updateOne(
                { username: username },
                { $set: { epsilon: new_epsilon } }
            );
            return arrayTimes
        }
        catch (err) {
            return arrayTimes;
        }
    }
    finally {
        await client.close();
    }
}

async function getTimesAriaSort(token) {
    const ERROR_RESULT = 1
    const NUMBER_OF_TIMES_OFFER = 10
    const WEEK_DAYS = 7
    const NUMBER_OF_RANGES = 5
    const EPSILON_DISCOUNT = 0.95
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const user = db.collection('User');
        try {
            const key = "secret key"
            const username1 = jwt.verify(token, key);
            const username = username1.username
            var arrayTimes = []
            let result = await user.find({ username: username }).toArray();
            if (result.length !== ERROR_RESULT) {
                return arrayTimes;
            }
            let rndNumber = 0, maxNumber = 0
            let arr = []
            for (let i = 0; i < NUMBER_OF_TIMES_OFFER; i++) {
                let randomNumber = Math.random();
                if (randomNumber < result[0].ariaEpsilon) {
                    rndNumber++
                    arr.push(0)
                }
                else {
                    maxNumber++
                    arr.push(1)
                }
            }
            let counterOfMax = 0
            for (let k = 0; k < WEEK_DAYS; k++) {
                const dayItem = result[0].ariaTimes.filter(item => item.day == k);
                for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                    if (dayItem[0].values[i].time != "") {
                        counterOfMax = counterOfMax + 1
                    }
                }
            }
            if (counterOfMax < maxNumber) {
                let d = maxNumber - counterOfMax
                maxNumber = counterOfMax
                rndNumber = NUMBER_OF_TIMES_OFFER - counterOfMax
                for (let i = 9; i >= 0; i--) {
                    if (arr[i] == 1 && d > 0) {
                        arr[i] = 0
                        d = d - 1
                    }
                }
            }
            let arrayTemp = []
            let arrayMax = []
            for (let k = 0; k < WEEK_DAYS; k++) {
                const dayItem = result[0].ariaTimes.filter(item => item.day == k);
                let counter = 4
                

                let json = {
                    "k": k,
                    "time": dayItem[0].values[counter].time,
                    "q_value": dayItem[0].values[counter].q_value,
                    "counter": counter
                }
                arrayTemp.push(json)
            }
            let maxvalue = 0
            let imax = 0
            let numberofdays = 7
            for (let j = 0; j < maxNumber; j++) {
                imax = 0
                maxvalue = 0
                for (let i = 0; i < numberofdays; i++) {
                    if (maxvalue < arrayTemp[i].q_value) {
                        maxvalue = arrayTemp[i].q_value
                        imax = i
                    }
                }
                arrayMax.push(arrayTemp[imax])
                let k = arrayTemp[imax].k
                const dayItem = result[0].ariaTimes.filter(item => item.day == k);
                if (arrayTemp[imax].counter - 1 >= 0) {
                    let json = {
                        "k": k,
                        "time": dayItem[0].values[arrayTemp[imax].counter - 1].time,   //4 change
                        "q_value": dayItem[0].values[arrayTemp[imax].counter - 1].q_value, //4 change
                        "counter": arrayTemp[imax].counter - 1
                    }
                    arrayTemp.push(json)
                }
                else {
                    numberofdays = numberofdays - 1
                }
                arrayTemp.splice(imax, 1)
            }
            let indexmax = 0
            for (let i = 0; i < arr.length; i++) {
                if (arr[i] == 0) {
                    let s = getRandomRange()
                    for (let i = 0; i < NUMBER_OF_TIMES_OFFER; i++) {
                        while (s === arrayMax[i]) {
                            s = getRandomRange()
                        }
                    }
                    let k = Math.floor(Math.random() * 7);
                    let json = {
                        "k": k,
                        "time": s
                    }
                    arrayTimes.push(json)
                }
                else {
                    arrayTimes.push(arrayMax[indexmax])
                    indexmax = indexmax + 1
                }
            }
            let new_epsilon = result[0].ariaEpsilon * EPSILON_DISCOUNT
            await user.updateOne(
                { username: username },
                { $set: { ariaEpsilon: new_epsilon } }
            );
            let arrayResult = []
            const today = new Date();
            const currentDay = today.getDay();
            for (let i = 0; i < NUMBER_OF_TIMES_OFFER; i++) {
                let diff = arrayTimes[i].k - currentDay;
                if (diff <= 0) {
                    diff += 7;
                }
                const resultDate = new Date(today);
                resultDate.setDate(today.getDate() + diff);
                const formattedDate = resultDate.toLocaleDateString('en-GB');
                let date = formattedDate

                let json = {
                    "date": date,
                    "time": arrayTimes[i].time
                }
                
                arrayResult.push(json)
            }
            
            return arrayResult
        }
        catch (err) {
            
            return arrayTimes;
        }
    }
    finally {
        
        await client.close();
    }
}

async function sort(token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    const db = client.db('Aria');
    const usersMongo = db.collection('User');
    const key = "secret key"
    const data = jwt.verify(token, key);
    const username = data.username;
    let userResult = await usersMongo.findOne({
        username: username
    });
    userResult.calendarTimes.forEach(day => {
        day.values.sort((a, b) => a.q_value - b.q_value);
    });
    await usersMongo.updateOne(
        { "username": username },
        { $set: { "calendarTimes": userResult.calendarTimes } }
    );
}

async function sortAria(token) {
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    const db = client.db('Aria');
    const usersMongo = db.collection('User');
    const key = "secret key"
    const data = jwt.verify(token, key);
    const username = data.username;
    let userResult = await usersMongo.findOne({
        username: username
    });
    userResult.ariaTimes.forEach(day => {
        day.values.sort((a, b) => a.q_value - b.q_value);
    });
    await usersMongo.updateOne(
        { "username": username },
        { $set: { "ariaTimes": userResult.ariaTimes } }
    );
}

async function updateCalendarTimes(day, time, flag, token) {
    const NUMBER_OF_RANGES = 5
    const SMALL_REWARD = 5
    const BIG_REWARD = 10
    const ALPHA = 0.1
    const GAMMA = 0.9
    //q[old_s][old_a] = q[old_s][old_a] + 0.1 * (r + 0.9 * (q[new_s][new_a]) - q[old_s][old_a]) 
    let isin = 0;
    let lastEmpty = 0;
    let oldQValue = 0;
    let maxQValue = -1;
    let reward = SMALL_REWARD
    if (flag) {
        reward = BIG_REWARD
    }
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const usersMongo = db.collection('User');
        const key = "secret key"
        const data = jwt.verify(token, key);
        const username = data.username;
        try {
            let userResult = await usersMongo.findOne({
                username: username
            });
            const jsonDay = userResult.calendarTimes.filter(item => item.day === day);
            for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                let timeMongo = jsonDay[0].values[i].time;
                let qValueMongo = jsonDay[0].values[i].q_value;
                if (timeMongo === time) {
                    isin = 1
                    oldQValue = qValueMongo
                }
                if (i === NUMBER_OF_RANGES - 1) {
                    if (timeMongo === "") {
                        lastEmpty = 1
                    }
                }
                if (maxQValue < qValueMongo) {
                    maxQValue = qValueMongo;
                    maxTime = timeMongo;
                }
            }
            if (isin) {
                const valuesArray = jsonDay[0].values;
                const valueIndex = valuesArray.findIndex(val => val.time === time);
                let newReward = oldQValue + ALPHA * (reward + GAMMA * (maxQValue - oldQValue))
                const updatePath = `calendarTimes.$[calElem].values.${ valueIndex }.time`;
                const updateQValuePath = `calendarTimes.$[calElem].values.${ valueIndex }.q_value`;
                await usersMongo.updateOne(
                    { "username": username },
                    {
                        $set: {
                            [updatePath]: time,
                            [updateQValuePath]: newReward
                        }
                    },
                    {
                        arrayFilters: [
                            { "calElem.day": day }
                        ]
                    }
                );
            }
            else {
                if (lastEmpty) {
                    const valuesArray = jsonDay[0].values;
                    const valueIndex = valuesArray.findIndex(val => val.time === "");
                    let newReward = ALPHA * (reward + (GAMMA * maxQValue))
                    const updatePath = `calendarTimes.$[calElem].values.${ valueIndex }.time`;
                    const updateQValuePath = `calendarTimes.$[calElem].values.${ valueIndex }.q_value`;
                    await usersMongo.updateOne(
                        { "username": username },
                        {
                            $set: {
                                [updatePath]: time,
                                [updateQValuePath]: newReward
                            }
                        },
                        {
                            arrayFilters: [
                                { "calElem.day": day }
                            ]
                        }
                    );
                }
                else {
                    let minTime = "-1"
                    let minQValue = -1
                    for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                        let qValueMongo = jsonDay[0].values[i].q_value;
                        let timeMongo = jsonDay[0].values[i].time;
                        if (minQValue === -1) {
                            minQValue = qValueMongo
                            minTime = timeMongo
                        }
                        else {
                            if (qValueMongo < minQValue) {
                                minQValue = qValueMongo
                                minTime = timeMongo
                            }
                        }
                    }
                    const valuesArray = jsonDay[0].values
                    const valueIndex = valuesArray.findIndex(val => val.time === minTime);
                    let newReward = ALPHA * (reward + (GAMMA * maxQValue))
                    const updatePath = `calendarTimes.$[calElem].values.${ valueIndex }.time`;
                    const updateQValuePath = `calendarTimes.$[calElem].values.${ valueIndex }.q_value`;
                    await usersMongo.updateOne(
                        { "username": username },
                        {
                            $set: {
                                [updatePath]: time,
                                [updateQValuePath]: newReward
                            }
                        },
                        {
                            arrayFilters: [
                                { "calElem.day": day }
                            ]
                        }
                    );
                }

            }
            sort(token)
            return true;
        }
        catch (err) {
            return 0;
        }
    }
    finally {
        await client.close();
    }
}

async function updateAriaTimes(day, time, flag, token) {
    const NUMBER_OF_RANGES = 5
    const SMALL_REWARD = 5
    const BIG_REWARD = 10
    const ALPHA = 0.1
    const GAMMA = 0.9
    //q[old_s][old_a] = q[old_s][old_a] + 0.1 * (r + 0.9 * (q[new_s][new_a]) - q[old_s][old_a]) 
    const client = new MongoClient("mongodb://127.0.0.1:27017");
    try {
        const db = client.db('Aria');
        const usersMongo = db.collection('User');
        const key = "secret key"
        const data = jwt.verify(token, key);
        const username = data.username;
        for (let k = 0; k < time.length; k++) {
            let isin = 0;
            let lastEmpty = 0;
            let oldQValue = 0;
            let maxQValue = -1;
            let reward = SMALL_REWARD
            if (flag[k]) {
                reward = BIG_REWARD
            }
            try {
                let userResult = await usersMongo.findOne({
                    username: username
                });
                const jsonDay = userResult.ariaTimes.filter(item => item.day === day[k]);
                for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                    let timeMongo = jsonDay[0].values[i].time;
                    let qValueMongo = jsonDay[0].values[i].q_value;
                    if (timeMongo === time[k]) {
                        isin = 1
                        oldQValue = qValueMongo
                    }
                    if (i === NUMBER_OF_RANGES - 1) {
                        if (timeMongo === "") {
                            lastEmpty = 1
                        }
                    }
                    if (maxQValue < qValueMongo) {
                        maxQValue = qValueMongo;
                        maxTime = timeMongo;
                    }
                }
                if (isin) {
                    const valuesArray = jsonDay[0].values;
                    const valueIndex = valuesArray.findIndex(val => val.time === time[k]);
                    let newReward = oldQValue + ALPHA * (reward + GAMMA * (maxQValue - oldQValue))
                    const updatePath = `ariaTimes.$[calElem].values.${ valueIndex }.time`;
                    const updateQValuePath = `ariaTimes.$[calElem].values.${ valueIndex }.q_value`;
                    await usersMongo.updateOne(
                        { "username": username },
                        {
                            $set: {
                                [updatePath]: time[k],
                                [updateQValuePath]: newReward
                            }
                        },
                        {
                            arrayFilters: [
                                { "calElem.day": day[k] }
                            ]
                        }
                    );
                }
                else {
                    if (lastEmpty) {
                        const valuesArray = jsonDay[0].values;
                        const valueIndex = valuesArray.findIndex(val => val.time === "");
                        let newReward = ALPHA * (reward + (GAMMA * maxQValue))
                        const updatePath = `ariaTimes.$[calElem].values.${ valueIndex }.time`;
                        const updateQValuePath = `ariaTimes.$[calElem].values.${ valueIndex }.q_value`;
                        await usersMongo.updateOne(
                            { "username": username },
                            {
                                $set: {
                                    [updatePath]: time[k],
                                    [updateQValuePath]: newReward
                                }
                            },
                            {
                                arrayFilters: [
                                    { "calElem.day": day[k] }
                                ]
                            }
                        );
                    }
                    else {
                        let minTime = "-1"
                        let minQValue = -1
                        for (let i = 0; i < NUMBER_OF_RANGES; i++) {
                            let qValueMongo = jsonDay[0].values[i].q_value;
                            let timeMongo = jsonDay[0].values[i].time;
                            if (minQValue === -1) {
                                minQValue = qValueMongo
                                minTime = timeMongo
                            }
                            else {
                                if (qValueMongo < minQValue) {
                                    minQValue = qValueMongo
                                    minTime = timeMongo
                                }
                            }
                        }
                        const valuesArray = jsonDay[0].values
                        const valueIndex = valuesArray.findIndex(val => val.time === minTime);
                        let newReward = ALPHA * (reward + (GAMMA * maxQValue))
                        const updatePath = `ariaTimes.$[calElem].values.${ valueIndex }.time`;
                        const updateQValuePath = `ariaTimes.$[calElem].values.${ valueIndex }.q_value`;
                        await usersMongo.updateOne(
                            { "username": username },
                            {
                                $set: {
                                    [updatePath]: time[k],
                                    [updateQValuePath]: newReward
                                }
                            },
                            {
                                arrayFilters: [
                                    { "calElem.day": day[k] }
                                ]
                            }
                        );
                    }
                }
            }
            catch (err) {
                return 0;
            }
        }
        sortAria(token)
        return true;
    }
    finally {
        await client.close();
    }
}

export default {
    addUser,
    getEvents,
    checkUsername,
    updateToken,
    getTimes,
    updateCalendarTimes,
    updateAriaTimes,
    getTimesAriaSort
}