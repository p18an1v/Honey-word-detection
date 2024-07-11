const Login_btn = document.querySelector("#Login-btn");
const Register_btn = document.querySelector("#Register-btn");
const container = document.querySelector(".container");

Register_btn.addEventListener("click", () => {
  container.classList.add("Register-mode");
});

Login_btn.addEventListener("click", () => {
  container.classList.remove("Register-mode");
});


document.addEventListener("DOMContentLoaded", function() {
  const loginForm = document.querySelector(".Login-form");
  const registerForm = document.querySelector(".Register-form");
  const forgotPasswordForm = document.querySelector(".ForgotPassword-form");
  const resetPasswordForm = document.querySelector(".ResetPassword-form");

  const loginBtn = document.querySelector("#Login-btn");
  const registerBtn = document.querySelector("#Register-btn");
  const forgotPasswordLink = document.querySelector("#ForgotPassword-link");
  const backToLoginLink = document.querySelector("#BackToLogin-link");

  registerBtn.addEventListener("click", () => {
    loginForm.style.display = "none";
    registerForm.style.display = "block";
    forgotPasswordForm.style.display = "none";
    resetPasswordForm.style.display = "none";
  });

  loginBtn.addEventListener("click", () => {
    loginForm.style.display = "block";
    registerForm.style.display = "none";
    forgotPasswordForm.style.display = "none";
    resetPasswordForm.style.display = "none";
  });

  forgotPasswordLink.addEventListener("click", (e) => {
    e.preventDefault();
    loginForm.style.display = "none";
    registerForm.style.display = "none";
    forgotPasswordForm.style.display = "block";
    resetPasswordForm.style.display = "none";
  });

  backToLoginLink.addEventListener("click", (e) => {
    e.preventDefault();
    loginForm.style.display = "block";
    registerForm.style.display = "none";
    forgotPasswordForm.style.display = "none";
    resetPasswordForm.style.display = "none";
  });

  function updatePlaceholder() {
    const select = document.getElementById("securityQuestionSelect");
    const selectedOption = select.options[select.selectedIndex].text;
    document.getElementById("securityAnswerInput").placeholder = selectedOption;
  }

  function clearPlaceholder() {
    document.getElementById("securityAnswerInput").placeholder = "";
  }

  document.getElementById("securityQuestionSelect").addEventListener("change", updatePlaceholder);
  document.getElementById("securityAnswerInput").addEventListener("focus", clearPlaceholder);

  function getQueryParams() {
    const params = {};
    const queryString = window.location.search.substring(1);
    const vars = queryString.split("&");
    vars.forEach(function (pair) {
      const [key, value] = pair.split("=");
      params[key] = value;
    });
    return params;
  }

  window.onload = function () {
    const params = getQueryParams();
    if (params["error"] && params["error"] === "wrong_password") {
      document.getElementById("errorMessage").style.display = "block";
    }
  };
});
