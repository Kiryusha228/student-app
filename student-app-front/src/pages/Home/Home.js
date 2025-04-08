import React, { useState } from "react";
import "./Home.css";
import { useNavigate } from "react-router-dom";

const HomePage = () => {
  const [email, setEmail] = useState("");
  const navigate = useNavigate();

  const handleEmailChange = (e) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async () => {
    if (!email) {
      alert("Пожалуйста, введите email");
      return;
    }

    try {
      const url = `http://localhost:8080/api/student/check?userMail=${encodeURIComponent(email)}`;
      
      const response = await fetch(url, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        }
      });

      if (!response.ok) {
        throw new Error(`Ошибка сервера: ${response.status}`);
      }

      const data = await response.json();
      
      if (data === true) {
        navigate("/password", { state: { email } });
      } else {
        navigate("/registration", { state: { email } });
      }

    } catch (err) {
      console.error('Ошибка при выполнении запроса:', err);
      alert("Произошла ошибка при проверке email");
    }
  };

  return (
    <div className="home-page">
      <div className="home-content-wrapper">
        <h1>Добро пожаловать!</h1>
        <p>Введите электронную почту, чтобы войти или зарегистрироваться.</p>

        <div className="home-input-container">
          <input
            type="email"
            value={email}
            onChange={handleEmailChange}
            placeholder="Введите электронную почту"
            required
            className="home-input"
          />
          <button className="home-continue-button" onClick={handleSubmit}>
            Продолжить
          </button>
        </div>

        <div className="home-employee-login">
          <a href="/employee-login">Вход для сотрудников</a>
        </div>
      </div>
    </div>
  );
};

export default HomePage;