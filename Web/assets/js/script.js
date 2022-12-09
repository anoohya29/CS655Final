// List of users, when a user is available, we added it to this list
const listUsers = document.getElementById('listUsers');

// Form to input a port number 
const portNumForm = document.getElementById('port-number-form');

// Input port number value
const portNumInputField = document.getElementById('port-number-input-field');

// Url of manager node
const BASE_URL = 'pcvm3-7.geni.uchicago.edu';
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

    const form = document.createElement('form');
    form.id = `form-${userID}`;

    const div1 = document.createElement('div');
    div1.className = `list-item mb-2`;
    div1.setAttribute("data-id", "16");

    const div9 = document.createElement('div');
    div9.setAttribute("class", "no-wrap");
    const result = document.createElement('label');
    result.setAttribute("class", "item-date text-muted text-sm d-none d-md-block");
    result.setAttribute("id", `password-crack-result-${userID}`);
    result.textContent = "Result: ";
    div9.appendChild(result);

    const div2 = document.createElement('div');
    const a1 = document.createElement('a');
    a1.setAttribute("href", "#");
    a1.setAttribute("data-abc", "true");
    const span1 = document.createElement('span');
    span1.setAttribute("class", "w-48 avatar gd-info");
    span1.textContent = `${userID}`;
    a1.appendChild(span1);
    div2.appendChild(a1);
    div1.appendChild(div2);

    const div3 = document.createElement('div');
    div3.setAttribute("class", "flex");
    const a3 = document.createElement('a');
    a3.setAttribute("href", "#");
    a3.setAttribute("class", "item-author text-color");
    a3.setAttribute("data-abc", "true");
    a3.textContent = `User ${userID}: `;
    const div4 = document.createElement('div');
    div4.setAttribute("class", "item-except text-muted text-sm h-1x");
    div4.textContent = `This is a user number ${userID}`;
    div3.appendChild(a3);
    div3.appendChild(div4);
    div1.appendChild(div3);

    const div5 = document.createElement('div');
    div5.setAttribute("class", "no-wrap");
    const input1 = document.createElement('input');
    input1.setAttribute("type", "text");
    input1.setAttribute("placeholder", `${PASSWORD_LEN}-character password`);
    input1.setAttribute("id", `input-password-crack-${userID}`);
    input1.setAttribute("class", "form-control form-control-lg");
    input1.setAttribute("minlength", "5");
    input1.setAttribute("maxlength", "5");
    div5.appendChild(input1);
    div1.appendChild(div5);

    const div6 = document.createElement('div');
    div6.setAttribute("class", "no-wrap");
    const submitBtn = document.createElement('button');
    submitBtn.setAttribute("class", "btn btn-success btn-block");
    submitBtn.setAttribute("id", "submitBtn");
    submitBtn.setAttribute("type", "submit");
    submitBtn.textContent = "Submit";
    div6.appendChild(submitBtn);
    div1.appendChild(div6);

    const div7 = document.createElement('div');
    div7.setAttribute("class", "no-wrap");
    const randomBtn = document.createElement('button');
    randomBtn.setAttribute("class", "btn btn-primary btn-block");
    randomBtn.setAttribute("id", `randomBtn-${userID}`);
    randomBtn.setAttribute("type", "button");
    randomBtn.textContent = "Random";
    randomBtn.onclick = () => {
        if (result.textContent !== 'Pending') {
            input1.value = generateRandomString(PASSWORD_LEN);
        } else {
            alert('The cracking is running!');
        }
    }
    div7.appendChild(randomBtn);
    div1.appendChild(div7);

    const div8 = document.createElement('div');
    div8.setAttribute("class", "no-wrap");
    const removeBtn = document.createElement('button');
    removeBtn.setAttribute("class", "btn btn-danger btn-block");
    removeBtn.setAttribute("id", `removeBtn-${userID}`);
    removeBtn.setAttribute("type", "button");
    removeBtn.textContent = "Remove";
    removeBtn.onclick = () => {
        if (result.textContent !== 'Pending') {
            removeUserForm(userID);
        } else {
            alert('The cracking is running!');
        }
    }
    div8.appendChild(removeBtn);
    div1.appendChild(div8);

    div1.appendChild(div9);

    form.appendChild(div1);

    form.onsubmit = function(e) {
        e.preventDefault();
        const password = input1.value;
        if (result.textContent !== 'Pending') {
            submitPassword(password, portNum, userID, result, input1);
        } else {
            alert('The cracking is running!');
        }
    }
    
    return form;
}

function addUser() {
    const newForm = createUserForm(userNum);
    listUsers.appendChild(newForm);
    //defineButtonsActions(userNum);
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