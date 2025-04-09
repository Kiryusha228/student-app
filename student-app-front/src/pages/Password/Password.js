import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./Password.css";

const apiUrl = process.env.REACT_APP_API_URL;

const PasswordPage = () => {
  const [password, setPassword] = useState("");
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [email, setEmail] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const location = useLocation();


  useEffect(() => {
    if (!location.state?.email) {

      navigate("/", { replace: true });
      return;
    }
    setEmail(location.state.email);
  }, [location, navigate]);

  const handlePasswordChange = (e) => {
    setPassword(e.target.value);
    setError(""); 
  };

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  const handleLogin = async () => {
    if (!password) {
      setError("Пожалуйста, введите пароль");
      return;
    }

    try {
      const url = `${apiUrl}/api/auth/authenticate`;
      
      const response = await fetch(url, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          login: email, 
          password: password//getSHA256Hash(password)
        })
      });
    
      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.message || `Ошибка сервера: ${response.status}`);
      }
    
      const data = await response.json();
      

      localStorage.setItem("authToken", data.token);
      
   
      navigate("/personalAccount");
    
    } catch (err) {
      console.error('Ошибка при аутентификации:', err);
      setError(err.message || "Неверный email или пароль");
    }
  };

  if (!email) {
    return <div className="password-page">Загрузка...</div>;
  }

  async function getSHA256Hash(str) {
    const buf = new TextEncoder().encode(str);
    const digest = await crypto.subtle.digest('SHA-256', buf);
    return Array.from(new Uint8Array(digest))
      .map(b => b.toString(16).padStart(2, '0'))
      .join('');
  }


  return (
    <div className="password-page">
      <div className="password-container">
        <button className="password-back-button" onClick={() => navigate(-1)}>←</button>
        
        <h1>Здравствуйте, {email}!</h1>

        <div className="password-form">
          <div className="password-input-container">
            <input
              type={passwordVisible ? "text" : "password"}
              value={password}
              onChange={handlePasswordChange}
              placeholder="Введите пароль"
              className="password-input"
              required
            />
            <span 
              className="password-toggle-password" 
              onClick={togglePasswordVisibility}
            >
              {passwordVisible ? "🙈" : "👁"}
            </span>
          </div>

          {error && <div className="password-error">{error}</div>}

          <button 
            className="password-login-button" 
            onClick={handleLogin}
            disabled={!password}
          >
            Войти
          </button>
        </div>
      </div>
    </div>
  );
};

export default PasswordPage;