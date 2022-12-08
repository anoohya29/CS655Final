// List of users, when a user is available, we added it to this list
const listUsers = document.getElementById('listUsers');

// Form to input a port number 
const portNumForm = document.getElementById('port-number-form');

// Input port number value
const portNumInputField = document.getElementById('port-number-input-field');

// Url of manager node
const BASE_URL = 'pc2.instageni.utdallas.edu';

// Length of an available password
const PASSWORD_LEN = 5;

// To keep track of number of users
let userNum = 0;

// Variable for the port Number.
let portNum;


function verifyPassword(password) {
    let reg = /^[a-zA-Z]*$/;
    if(reg.test(password) && (password !== '')){
        return true;
    }
    return false;
}

function verifyPortNumber(portNumer) {
    let reg = /^[0-9]*$/;
    if(reg.test(portNumer)){
        return true;
    }
    return false;
}

function generateRandomString(length) {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  
    for (var i = 0; i < length; i++) {
      text += possible.charAt(Math.floor(Math.random() * possible.length));
    }
  
    return text;
  }

function getRequest(requestURl) {
    return new Promise((resolve, reject) => {
        fetch(requestURl, {
            mode: 'cors',
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            }
        })
        .then(body => {
            resolve(body);
        })
        .catch(err => {
            reject(err);
            alert('Connection refused, please change a port number.');
        });
    })
}

function submitPassword(password, portNum, userID, resultEle, inputEle) {
    if (!verifyPassword(password) || !(verifyPortNumber(portNum))) {
        alert('Invalid value.');
    } else {
        const md5Password = hex_md5(password);
        const queryURL = `http://${BASE_URL}:${portNum}?key=${md5Password}&id=${userID}`;
        let startTime = new Date();
        resultEle.textContent = 'Pending';
        inputEle.disabled = true;
        getRequest(queryURL).then((res) => {
            console.log(res);
            let endTime = new Date();
            if(res.length === 5){
                resultEle.textContent = `Result: ${res}. Runtime: ${(endTime-startTime) / 1000} s`;
            }
            else{
                alert('Internal Server Error!');
                resultEle.textContent = '';
            }
            inputEle.disabled = false;
        })
    }
}

function removeUserForm(userID) {
    const form = document.getElementById(`form-${userID}`);
    document.body.removeChild(form);
}

function createUserForm(userID) {

    let htmlCode = `<form id="form-${userID}">
                        <div class="list-item mb-2" data-id="16">
                            <div><a href="#" data-abc="true"><span class="w-48 avatar gd-info">F</span></a></div>
                            <div class="flex">
                                <a href="#" class="item-author text-color" data-abc="true">User ${userID}: </a>
                                <div class="item-except text-muted text-sm h-1x">This is a user</div>
                            </div>
                            <div class="no-wrap">
                                <input type="text" placeholder="${PASSWORD_LEN}-character password" id="input-password-crack-${userID}" class="form-control form-control-lg" minlength="5" maxlength="5" required>
                            </div>
                            <div class="no-wrap">
                                <button class="btn btn-success btn-block" id="submitBtn" type="submit">Submit</button>
                            </div>
                            <div class="no-wrap">
                                <button class="btn btn-primary btn-block" id="randomBtn-${userID}" type="button">Random</button>
                            </div>
                            <div class="no-wrap">
                                <button class="btn btn-danger btn-block" id="removeBtn-${userID}" type="button">Remove</button>
                            </div>
                            <div class="no-wrap">
                                <div class="item-date text-muted text-sm d-none d-md-block" id="password-crack-result-${userID}">Result: </div>
                            </div>
                        </div>
                    </form>`;
    
    return htmlCode;
}

function addUser() {
    const newForm = createUserForm(userNum);
    listUsers.innerHTML += (newForm);
    defineButtonsActions(userNum);
    userNum++;
}

function defineButtonsActions(userID){

    const form = document.getElementById("form-" + userID);
    const input = document.getElementById("input-password-crack-" + userID);
    const randomBtn = document.getElementById("randomBtn-" + userID);
    const removeBtn = document.getElementById("removeBtn-" + userID);
    const result = document.getElementById("password-crack-result-" + userID);
    

    removeBtn.onclick = () => {
        if (result.textContent !== 'Pending') {
            removeUserForm(userID);
        } else {
            alert('The cracking is running!');
        }
    }

    randomBtn.onclick = () => {
        if (result.textContent !== 'Pending') {
            input.value = generateRandomString(PASSWORD_LEN);
        } else {
            alert('The cracking is running!');
        }
    }

    form.onsubmit = function(e) {
        e.preventDefault();
        const password = input.value;
        if (result.textContent !== 'Pending') {
            submitPassword(password, portNum, userID, result, input);
        } else {
            alert('The cracking is running!');
        }
    }

}

portNumForm.onsubmit = (event) => {
    event.preventDefault();

    // Clear forms
    const forms = document.querySelectorAll('.password-form');
    for (let i = 0; i < forms.length; i++) {
        document.body.removeChild(forms[i]);
    }

    portNum = portNumInputField.value;
    
    if (portNum === '') {
        alert('Please input a port number.');
    } else {
        const queryURL = `http://${BASE_URL}:${portNum}?key=connection-check`;

        // Only when the port number is available, next steps are available.
        getRequest(queryURL).then(() =>{
            addUser();
        }).catch(() => {
            
        })
    }
}