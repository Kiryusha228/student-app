import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./PersonalAccount.css";


const examMetaData = {
  1: { title: "Анкета", description: "Не стесняйтесь рассказывать о своём опыте, так как анкета влияет на итоговый результат.", totalQuestions: 10, time: null },
  2: { title: "Экзамен по Java", description: "Решите экзамен. На выполнение даётся 10 минут.", totalQuestions: 10, time: "10 минут" },
  3: { title: "Экзамен по JavaScript", description: "Решите экзамен. На выполнение даётся 10 минут.", totalQuestions: 10, time: "10 минут" },
};

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState("description");
  const [exams, setExams] = useState([]);
  const [applicationSubmitted, setApplicationSubmitted] = useState(false);
  const [resultStatus, setResultStatus] = useState("pending");
  const [teamName, setTeamName] = useState("");
  const [workshop, setWorkshop] = useState(null);
  const [isRegistered, setIsRegistered] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {

    const fetchLastWorkshop = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/project-workshop/get/last',

        );
        if (!response.ok) throw new Error('Не удалось загрузить данные мастерской');
        const data = await response.json();
        setWorkshop(data);
        return data.id;
      } catch (err) {
        setError(err.message);
        throw err;
      }
    };


const checkRegistration = async (workshopId) => {
  try {
    const token = localStorage.getItem('authToken');
    if (!token) {
      throw new Error('Требуется авторизация');
    }

    const response = await fetch(
      `http://localhost:8080/api/student-project-workshop/check-registration?projectWorkshopId=${workshopId}`, 
      {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
          
        }
      }
    );

    if (!response.ok) {
      if (response.status === 401) {
        localStorage.removeItem('authToken');
        throw new Error('Сессия истекла, пожалуйста, войдите снова');
      }
      throw new Error('Не удалось проверить регистрацию');
    }

    const isRegistered = await response.json();
    setIsRegistered(isRegistered);
    return isRegistered;
  } catch (err) {
    setError(err.message);
    throw err;
  }
};


    const loadData = async () => {
      try {
        setLoading(true);
        const workshopId = await fetchLastWorkshop();
        const isReg = await checkRegistration(workshopId);
        

        const userData = await fetchUserData();
        setApplicationSubmitted(userData.hasSubmittedApplication || isReg);
        setExams(userData.exams);
        setResultStatus(userData.resultStatus);
        setTeamName(userData.teamName);
        
        if (userData.hasSubmittedApplication || isReg) {
          setActiveTab("exams");
        }
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  // Имитация API-запроса данных пользователя
  const fetchUserData = async () => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          hasSubmittedApplication: false,
          exams: [
            { id: 1, status: "Не начато", questionsAnswered: 0 },
            { id: 2, status: "Не начато", questionsAnswered: 0 },
            { id: 3, status: "Не начато", questionsAnswered: 0 },
          ],
          resultStatus: "pending",
          teamName: ""
        });
      }, 500);
    });
  };

  const handleApplicationClick = () => {
    setApplicationSubmitted(true);
    setActiveTab("exams");
  };

  if (loading) return <div className="dashboard">Загрузка данных...</div>;
  if (error) return <div className="dashboard">Ошибка: {error}</div>;

  return (
    <div className="dashboard">
      <div className="content">
        <h1>Проектная мастерская</h1>

        <div className="tabs">
          <div className={`tab ${activeTab === "description" ? "active" : ""}`} onClick={() => setActiveTab("description")}>
            Описание
          </div>
          <div className={`tab ${activeTab === "exams" ? "active" : ""} ${!applicationSubmitted ? "disabled" : ""}`} onClick={() => applicationSubmitted && setActiveTab("exams")}>
            Экзамены
          </div>
          <div className={`tab ${activeTab === "results" ? "active" : ""} ${!applicationSubmitted ? "disabled" : ""}`} onClick={() => applicationSubmitted && setActiveTab("results")}>
            Результаты
          </div>
        </div>

        {/* Описание */}
        {activeTab === "description" && (
          <div className="description-text">
            {workshop && (
              <>
                <h2>{workshop.name} ({workshop.year})</h2>
                <p>
                  <strong>Дата начала:</strong> {new Date(workshop.startDateTime).toLocaleDateString()}
                  <br />
                  <strong>Дата окончания:</strong> {new Date(workshop.endDateTime).toLocaleDateString()}
                  <br />
                  <strong>Статус:</strong> {workshop.isEnable ? 'Активна' : 'Завершена'}
                </p>
                <hr />
              </>
            )}
            
            <p>
              Привет! Для участия в проектной мастерской необходимо заполнить анкету, пройти отбор по Java и JavaScript и решить контест.
            </p>
            
            <button 
              className="apply-button" 
              disabled={applicationSubmitted || !workshop?.isEnable} 
              onClick={handleApplicationClick}
            >
              {applicationSubmitted 
                ? isRegistered 
                  ? "Вы уже зарегистрированы" 
                  : "Заявка подана" 
                : workshop?.isEnable 
                  ? "Подать заявку" 
                  : "Регистрация закрыта"}
            </button>
            
            {isRegistered && (
              <div className="registration-status success">
                Вы успешно зарегистрированы на эту мастерскую!
              </div>
            )}
          </div>
        )}

        {/* Результаты */}
{activeTab === "results" && (
  <div className="results-section">
    {resultStatus === "pending" && (
      <div className="result-card pending">
        <div className="icon">⏳</div>
        <h2>Идёт отбор</h2>
        <p>Ваша заявка на рассмотрении. Ожидайте результатов.</p>
      </div>
    )}

    {resultStatus === "passed" && (
      <div className="result-card success">
        <div className="icon">🎉</div>
        <h2>Поздравляем!</h2>
        <p>Вы прошли отбор и попали в команду <strong>{teamName}</strong>!</p>
      </div>
    )}

    {resultStatus === "failed" && (
      <div className="result-card failed">
        <div className="icon">😔</div>
        <h2>Набор завершён</h2>
        <p>К сожалению, вы не прошли отбор в этот раз.</p>
      </div>
    )}
  </div>
)}
      </div>
    </div>
  );
};

export default Dashboard;

