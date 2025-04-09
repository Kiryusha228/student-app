import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./PersonalAccount.css";

const examMetaData = {
  1: {
    title: "Анкета",
    description: "Не стесняйтесь рассказывать о своём опыте, так как анкета влияет на итоговый результат.",
    totalQuestions: 10,
    time: null,
  },
  2: {
    title: "Экзамен по Java",
    description: "Решите экзамен. На выполнение даётся 10 минут.",
    totalQuestions: 10,
    time: "10 минут",
  },
};

const Dashboard = () => {
  const [activeTab, setActiveTab] = useState("description");
  const [exams, setExams] = useState([]);
  const [applicationSubmitted, setApplicationSubmitted] = useState(false);
  const [resultStatus, setResultStatus] = useState("pending"); // 'pending', 'passed', 'failed'
  const [teamName, setTeamName] = useState("");
  const [workshop, setWorkshop] = useState(null);
  const [isRegistered, setIsRegistered] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchLastWorkshop = async () => {
      try {
        const response = await fetch('http://localhost:8080/api/project-workshop/get/last');
        if (!response.ok) throw new Error('Не удалось загрузить данные мастерской');
        const data = await response.json();
        setWorkshop(data);
        return data.id;
      } catch (err) {
        setError(err.message);
        throw err;
      }
    };

    const fetchQuestionnaireStatus = async () => {
      const token = localStorage.getItem("authToken");
      try {
        const res = await fetch("http://localhost:8080/api/questionnaire/get", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (res.status === 404) {
          return { id: 1, status: "Не начато", questionsAnswered: 0 };
        }

        if (!res.ok) throw new Error("Анкета не найдена");
        const data = await res.json();
        return data ? { id: 1, status: "На проверке", questionsAnswered: 10 } : null;
      } catch {
        return { id: 1, status: "Не начато", questionsAnswered: 0 };
      }
    };

    const fetchTestStatus = async () => {
      const token = localStorage.getItem("authToken");
      try {
        const res = await fetch("http://localhost:8080/api/test-result/get", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (res.status === 404) {
          return { id: 2, status: "Не начато", questionsAnswered: 0 };
        }

        if (!res.ok) throw new Error("Результат теста не найден");
        const data = await res.json();
        return data && data.testResult
          ? { id: 2, status: "Завершено " + data.testResult + "/10", questionsAnswered: 10 }
          : { id: 2, status: "Не начато", questionsAnswered: 0 };
      } catch {
        return { id: 2, status: "Не начато", questionsAnswered: 0 };
      }
    };

    const checkRegistration = async (workshopId) => {
      try {
        const token = localStorage.getItem('authToken');
        const response = await fetch(
          `http://localhost:8080/api/student-project-workshop/check-registration?projectWorkshopId=${workshopId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
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
        const isUserRegistered = await checkRegistration(workshopId);

        setApplicationSubmitted(isUserRegistered);

        if (isUserRegistered) {
          const [questionnaire, test] = await Promise.all([
            fetchQuestionnaireStatus(),
            fetchTestStatus(),
          ]);

          setExams([questionnaire, test]);
          setActiveTab("exams");
        }
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  useEffect(() => {
    const checkResults = async () => {
      if (activeTab !== "results") return;

      try {
        const token = localStorage.getItem("authToken");

        const response = await fetch('http://localhost:8080/api/project-workshop/get/last');
        if (!response.ok) throw new Error("Не удалось загрузить мастерскую");
        const workshopData = await response.json();

        setWorkshop(workshopData);

        if (!workshopData.isEnabled) {
          const teamRes = await fetch("http://localhost:8080/api/student-project-workshop/team", {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          });

          if (teamRes.ok) {
            const team = await teamRes.json();
            if (Array.isArray(team) && team.length > 0) {
              setTeamName(team[0].name || "ваша команда");
              setResultStatus("passed");
            } else {
              setResultStatus("failed");
            }
          } else {
            setResultStatus("failed");
          }
        } else {
          setResultStatus("pending");
        }
      } catch (err) {
        console.error("Ошибка проверки результатов:", err);
        setResultStatus("failed");
      }
    };

    checkResults();
  }, [activeTab]);

  const handleApplicationClick = async () => {
    try {
      const token = localStorage.getItem('authToken');
      const response = await fetch('http://localhost:8080/api/student-project-workshop/add', {
        method: 'POST',
        headers: {
          Authorization: `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        if (response.status === 401) {
          localStorage.removeItem('authToken');
          throw new Error('Сессия истекла. Пожалуйста, войдите снова.');
        }
        throw new Error('Не удалось подать заявку');
      }

      setIsRegistered(true);
      setApplicationSubmitted(true);
      setActiveTab("exams");
    } catch (err) {
      alert(err.message);
    }
  };

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

        {activeTab === "description" && (
          <div className="description-text">
            <p>
              Привет! Для участия в проектной мастерской необходимо заполнить анкету, пройти отбор по Java и JavaScript и решить контест.
            </p>
            <button className="apply-button" disabled={applicationSubmitted} onClick={handleApplicationClick}>
              {applicationSubmitted ? "Заявка подана" : "Подать заявку"}
            </button>
          </div>
        )}

        {activeTab === "exams" && (
          <div className="exams-list">
            {exams.map((exam) => {
              const meta = examMetaData[exam.id];
              const isQuestionnaire = exam.id === 1;
              const isTestCompleted = exam.status.startsWith("Завершено");
              const isQuestionnaireCompleted = exam.questionsAnswered > 0;

              const buttonText = isQuestionnaire
                ? (isQuestionnaireCompleted ? "Посмотреть" : "Приступить")
                : (isTestCompleted ? "Завершено" : "Приступить");

              const isButtonDisabled = !isQuestionnaire && isTestCompleted;

              return (
                <div key={exam.id} className="exam-card">
                  <div className="exam-header">
                    <span className="status-badge">{exam.status}</span>
                  </div>
                  <h3>{meta.title}</h3>
                  <p>{meta.description}</p>
                  {isQuestionnaire ? (
                    <Link to="/questionnaire">
                      <button className="exam-button">{buttonText}</button>
                    </Link>
                  ) : (
                    <Link to={`/test/${exam.id}`}>
                      <button className="exam-button" disabled={isButtonDisabled}>
                        {buttonText}
                      </button>
                    </Link>
                  )}
                </div>
              );
            })}
          </div>
        )}

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
