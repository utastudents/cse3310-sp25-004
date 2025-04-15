document.getElementById("show-signup").onclick = function() {
    document.getElementById("login").style.display = "none";
    document.getElementById("new_account").style.display = "block";
};

document.getElementById("back-to-login").onclick = function() {
    document.getElementById("new_account").style.display = "none";
    document.getElementById("login").style.display = "block";
};

document.getElementById("signup-btn").onclick = function(e){
    e.preventDefault();
    
    const username = document.getElementById("signup-username").value; //username from signup form
    const password = document.getElementById("signup-password").value; //password from signup form
    const confirmPassword = document.getElementById("signup-confirm-password").value;
    //Hide error messages by default
    document.getElementById("username-error").style.display = "none";
    document.getElementById("password-error").style.display = "none";
    document.getElementById("confirm-password-error").style.display = "none";


    let valid = true;
if(username.length < 3){
    document.getElementById("username-error").style.display = "inline";
    valid = false;
}
if(password.length < 5){
    document.getElementById("password-error").style.display = "inline";
    valid = false;
}
if(password !== confirmPassword){
    document.getElementById("confirm-password-error").style.display = "inline";
    valid = false;
    return;
}
if(!username || !password || !confirmPassword){
alert("All fields are required.");
return;
}
if(!valid) return;

const signupData = { 
    //Data into JSON object
    action: "new_user",
    UserName : username, 
    Password:password
};
//Send data as JSON string JSON.stringify
sendMessage(JSON.stringify(signupData));
console.log("Signup Data sent: ", signupData)		
};
//Event handler for login button
document.getElementById("login-btn").onclick = function(e){
e.preventDefault();

const loginUsername = document.getElementById("login-username").value; //username from signup form
const loginPassword = document.getElementById("login-password").value; //password from signup form

document.getElementById("login-username-error").style.display = "none";
document.getElementById("login-password-error").style.display = "none";
    
// More Data Validation can go here
if(!loginUsername || !loginPassword){ //Username or Password not filled in
    alert("Invalid username and/or password.");
    return;
}
const loginData = { //Data into JSON object
    action: "login",
    UserName : loginUsername, 
    Password:loginPassword
};
//Send data as JSON string JSON.stringify
sendMessage(JSON.stringify(loginData))
console.log("Login Data sent: ", loginData)		
}
