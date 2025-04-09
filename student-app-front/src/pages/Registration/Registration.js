import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Registration.css";

const Registration = () => {
  const [formData, setFormData] = useState({
    name: "",
    mail: "",
    password: ""
  });
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [agreeToTerms, setAgreeToTerms] = useState(false);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const togglePasswordVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");

    if (!formData.name || !formData.mail || !formData.password) {
      setError("Все поля обязательны для заполнения");
      return;
    }

    if (!agreeToTerms) {
      setError("Необходимо согласиться с условиями");
      return;
    }

    if (!/^\S+@\S+\.\S+$/.test(formData.mail)) {
      setError("Введите корректный email");
      return;
    }

    if (formData.password.length < 6) {
      setError("Пароль должен содержать минимум 6 символов");
      return;
    }

    try {
      setIsLoading(true);
      const response = await fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: formData.name,
          password: formData.password,//getSHA256Hash(formData.password),
          mail: formData.mail
        })
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Ошибка регистрации");
      }

      const data = await response.json();

      localStorage.setItem("authToken", data.token);
      

      navigate("/personalAccount");

    } catch (err) {
      console.error('Ошибка регистрации:', err);
      setError(err.message || "Произошла ошибка при регистрации");
    } finally {
      setIsLoading(false);
    }
  };

  async function getSHA256Hash(str) {
    const buf = new TextEncoder().encode(str);
    const digest = await crypto.subtle.digest('SHA-256', buf);
    return Array.from(new Uint8Array(digest))
      .map(b => b.toString(16).padStart(2, '0'))
      .join('');
  }

  return (
    <div className="registration-wrapper">
      <div className="registration-container">
        <h2>Регистрация</h2>
        {error && <div className="registration-error">{error}</div>}
        
        <form className="registration-form" onSubmit={handleSubmit}>
          <label className="registration-label">ФИО</label>
          <input
            type="text"
            name="name"
            className="registration-input"
            placeholder="Введите фамилию, имя и отчество"
            value={formData.name}
            onChange={handleChange}
            required
          />

          <label className="registration-label">Email</label>
          <input
            type="email"
            name="mail"
            className="registration-input"
            placeholder="example@email.com"
            value={formData.mail}
            onChange={handleChange}
            required
          />

          <label className="registration-label">Пароль</label>
          <div className="registration-password-container">
            <input
              type={passwordVisible ? "text" : "password"}
              name="password"
              className="registration-password-input"
              placeholder="Введите пароль (минимум 6 символов)"
              value={formData.password}
              onChange={handleChange}
              minLength="6"
              required
            />
            <span
              className="registration-toggle-password"
              onClick={togglePasswordVisibility}
            >
              {passwordVisible ? "🙈" : "👁"}
            </span>
          </div>

          <div className="registration-checkbox-container">
            <input 
              type="checkbox" 
              id="terms" 
              checked={agreeToTerms}
              onChange={() => setAgreeToTerms(!agreeToTerms)}
            />
            <label htmlFor="terms">
              Я соглашаюсь с <a href="#">Условиями использования</a> и{" "}
              <a href="#">Политикой конфиденциальности</a>
            </label>
          </div>

          <button 
            type="submit" 
            className="register-button"
            disabled={isLoading}
          >
            {isLoading ? "Регистрация..." : "Зарегистрироваться"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Registration;