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
sendMessage(signupData);
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
sendMessage(loginData)
console.log("Login Data sent: ", loginData)		
}




const chaosButton = document.getElementById("chaos-button");

const imageUrls = [
    "login_signup/blue_guy.png", 
    "login_signup/freddy.png",
    "login_signup/platy.png",
    "login_signup/cj.png"

];
const sound = new Audio("login_signup/chicken-jockey.mp3");

function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min) + min);
}

chaosButton.addEventListener("click", () => {
    const randomImage = imageUrls[Math.floor(Math.random() * imageUrls.length)];
    const img = document.createElement("img");
    img.src = randomImage
    img.style.position = "absolute";
    img.style.width = "80px";
    img.style.height = "80px";
    img.style.top = `${getRandomInt(0, window.innerHeight - 80)}px`;
    img.style.left = `${getRandomInt(0, window.innerWidth - 80)}px`;
    img.style.zIndex = 998;
    document.body.appendChild(img);
    if (randomImage.endsWith("cj.png")) {
        sound.play();
    }
});

const loginScreen = document.getElementById("login");
const observer = new MutationObserver(() => {
    const displayStyle = window.getComputedStyle(loginScreen).display;
    chaosButton.style.display = displayStyle === "none" ? "none" : "block";
});

observer.observe(loginScreen, { attributes: true, attributeFilter: ['style'] });
document.getElementById("showDragonBtn").onclick = function() {
    var dragon = document.getElementById("dragon");
    if(dragon.style.display === "none" || dragon.style.display === ""){
        dragon.style.display = "block";
    }
    else {
        dragon.style.display = "none";
    }
}