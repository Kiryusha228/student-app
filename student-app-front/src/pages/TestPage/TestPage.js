import React, { useState, useEffect } from "react";
import { useParams, Link } from "react-router-dom";
import "./TestPage.css";

const fetchExamQuestions = async (examId) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve([
        {
          testQuestion: "Что такое JVM?",
          answers: [
            "Java Virtual Machine",
            "Java Visual Machine",
            "Java Virtual Memory",
            "Java Visual Memory",
            "Все выше перечисленные"
          ],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой аннотацией обозначается главный класс Spring Boot приложения?",
          answers: [
            "@SpringBootApplication",
            "@SpringApplication",
            "@BootApplication",
            "@MainApplication"
          ],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int", "float", "double", "String"],
          rightAnswer: 0
        },
        {
          testQuestion: "Какой тип данных в Java используется для хранения целых чисел?",
          answers: ["int"],
          rightAnswer: 0
        }
        // Остальные вопросы 
      ]);
    }, 500);
  });
};

const TestPage = () => {
  const { examId } = useParams();
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
      setIsTestFinished(true);
      console.log("Ответы отправлены:", newConfirmed); 
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
              <button className="finish-btn" onClick={() => setIsTestFinished(true)}>
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
