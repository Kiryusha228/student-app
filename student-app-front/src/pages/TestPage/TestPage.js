import React, { useState, useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import "./TestPage.css";

const apiUrl = process.env.REACT_APP_API_URL;

const fetchExamQuestions = async (examId) => {
  try {
    const token = localStorage.getItem("authToken");
    const res = await fetch(`${apiUrl}/api/test/get`, {
      method: "GET",
    });

    if (!res.ok) {
      const errorText = await res.text();
      throw new Error(errorText || "Ошибка при получении вопросов");
    }

    const contentType = res.headers.get("content-type");
    if (contentType && contentType.includes("application/json")) {
      const data = await res.json();
      return data;
    } else {
      throw new Error("Сервер вернул некорректный формат данных");
    }
  } catch (error) {
    console.error("Ошибка:", error);
    alert(error.message || "Произошла ошибка при загрузке вопросов.");
    return [];
  }
};

const submitAnswers = async (answers) => {
  try {
    const token = localStorage.getItem("authToken");
    const res = await fetch(`${apiUrl}/api/test/check`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
      body: JSON.stringify(answers),
    });

    if (!res.ok) {
      throw new Error(errorText || "Ошибка при отправке ответов");
    }
  } catch (error) {
    console.error("Ошибка:", error);
    return null;
  }
};

const TestPage = () => {
  const { examId } = useParams();
  const navigate = useNavigate();

  const [questions, setQuestions] = useState([]);
  const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState([]);
  const [confirmedAnswers, setConfirmedAnswers] = useState([]);
  const [timer, setTimer] = useState(600); // 10 минут
  const [isTestFinished, setIsTestFinished] = useState(false);

  useEffect(() => {
    fetchExamQuestions(examId).then((data) => setQuestions(data));

    const countdown = setInterval(() => {
      setTimer((prev) => {
        if (prev <= 1) {
          clearInterval(countdown);
          handleFinishTest();
          setIsTestFinished(true);
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => clearInterval(countdown);
  }, [examId]);

  const handleAnswerSelect = (questionIndex, answerIndex) => {
    if (isTestFinished) return;
    const newAnswers = [...selectedAnswers];
    newAnswers[questionIndex] = answerIndex;
    setSelectedAnswers(newAnswers);
  };

  const handleConfirmAnswer = () => {
    const selected = selectedAnswers[currentQuestionIndex];
    if (selected === undefined) return;

    const newConfirmed = [...confirmedAnswers];
    newConfirmed[currentQuestionIndex] = selected;
    setConfirmedAnswers(newConfirmed);

    if (currentQuestionIndex < questions.length - 1) {
      setCurrentQuestionIndex(currentQuestionIndex + 1);
    } else {
      handleFinishTest();
      setIsTestFinished(true);
    }
  };

  const handleFinishTest = async () => {
    setIsTestFinished(true);

    // Формируем массив ответов с учетом неотвеченных вопросов
    const answersToSend = Array.from({ length: questions.length }, (_, index) => {
      const confirmedAnswer = confirmedAnswers[index];
      return confirmedAnswer !== undefined ? confirmedAnswer + 1 : -1; // +1 для сервера, -1 для неотвеченных
    });

    const result = await submitAnswers(answersToSend);

    if (result) {
      console.log("Ответы успешно отправлены!", result);
      navigate("/personalAccount");
    }
  };

  const handleQuestionClick = (index) => {
    if (!isTestFinished) setCurrentQuestionIndex(index);
  };

  const formatTime = (time) => {
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;
    return `${minutes}:${seconds < 10 ? "0" : ""}${seconds}`;
  };

  return (
    <div className="test-page">
      <div className="test-container">
        <div className="left-panel">
          <h2>Экзамен по {examId === "2" ? "Java" : "JavaScript"}</h2>
          <h3>{currentQuestionIndex + 1}. Вопрос</h3>
          <p>{questions[currentQuestionIndex]?.testQuestion}</p>

          <div className="answers">
            {questions[currentQuestionIndex]?.answers.map((answer, index) => (
              <div key={index} className="answer-option">
                <input
                  type="radio"
                  id={`answer-${index}`}
                  name={`question-${currentQuestionIndex}`}
                  checked={selectedAnswers[currentQuestionIndex] === index}
                  onChange={() => handleAnswerSelect(currentQuestionIndex, index)}
                  disabled={isTestFinished || confirmedAnswers[currentQuestionIndex] !== undefined}
                />
                <label htmlFor={`answer-${index}`}>{answer}</label>
              </div>
            ))}
          </div>

          <button
            className="submit-btn"
            onClick={handleConfirmAnswer}
            disabled={
              selectedAnswers[currentQuestionIndex] === undefined ||
              confirmedAnswers[currentQuestionIndex] !== undefined
            }
          >
            Ответить
          </button>
        </div>

        <div className="right-panel">
          <div className="timer-box">
            <div className="timer-title">Таймер</div>
            <div className="timer-value">{formatTime(timer)}</div>
            <div className="question-grid">
              {questions.map((_, index) => (
                <div
                  key={index}
                  className={`grid-btn ${
                    confirmedAnswers[index] !== undefined ? "answered" : ""
                  }`}
                  onClick={() => handleQuestionClick(index)}
                >
                  {index + 1}
                </div>
              ))}
            </div>
            {!isTestFinished && (
              <button className="finish-btn" onClick={handleFinishTest}>
                Закончить
              </button>
            )}
          </div>
        </div>
      </div>

      {isTestFinished && (
        <div className="finished">
          <h2>Тест завершён!</h2>
          <Link to="/personalAccount">Вернуться в личный кабинет</Link>
        </div>
      )}
    </div>
  );
};

export default TestPage;