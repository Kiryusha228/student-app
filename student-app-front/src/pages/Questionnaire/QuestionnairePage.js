import React, { useEffect, useState } from "react";
import "./QuestionnairePage.css";
import { useNavigate } from "react-router-dom";

const apiUrl = process.env.REACT_APP_API_URL;

const QuestionnairePage = () => {
  const [formData, setFormData] = useState({
    university: "",
    graduationYear: "",
    faculty: "",
    experience: "",
    programmingSkills: "",
    technologies: "",
    telegram: "",
    role: "",
    github: "",
  });

  const [isDataLoaded, setIsDataLoaded] = useState(false);

  // Инициализация хука useNavigate внутри компонента
  const navigate = useNavigate();

  // Загрузка данных при монтировании компонента
  useEffect(() => {
    const fetchData = async () => {
      try {
        const token = localStorage.getItem("authToken");
        if (!token) {
          throw new Error("Требуется авторизация");
        }

        const res = await fetch(`${apiUrl}/api/questionnaire/get`, {
          method: "GET",
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) {
          // Если статус ответа не успешный, проверяем наличие текстового ответа
          const errorText = await res.text();
          throw new Error(errorText || "Ошибка при получении данных");
        }

        // Проверяем, что ответ содержит JSON
        const contentType = res.headers.get("content-type");
        if (contentType && contentType.includes("application/json")) {
          const data = await res.json();
          setFormData({
            university: data.university || "",
            graduationYear: data.graduationYear || "",
            faculty: data.faculty || "",
            experience: data.experience || "",
            programmingSkills: data.languageProficiency || "",
            technologies: data.languageExperience || "",
            telegram: data.telegram || "",
            role: data.role || "",
            github: data.github || "",
          });
        } else {
          throw new Error("Сервер вернул некорректный формат данных");
        }
      } catch (error) {
        console.error("Ошибка:", error);
      } finally {
        setIsDataLoaded(true);
      }
    };

    fetchData();
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleBack = () => {
    window.history.back();
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem("authToken");
      if (!token) {
        throw new Error("Требуется авторизация");
      }

      // Сначала проверяем существование анкеты
      const checkResponse = await fetch(`${apiUrl}/api/questionnaire/get`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      const questionnaireExists = checkResponse.ok;
      const url = questionnaireExists
        ? `${apiUrl}/api/questionnaire/update`
        : `${apiUrl}/api/questionnaire/add`;

      const method = questionnaireExists ? "PATCH" : "POST";

      const res = await fetch(url, {
        method,
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          university: formData.university,
          graduationYear: formData.graduationYear,
          faculty: formData.faculty,
          experience: formData.experience,
          languageProficiency: formData.programmingSkills,
          languageExperience: formData.technologies,
          telegram: formData.telegram,
          role: formData.role,
          github: formData.github,
        }),
      });

      if (!res.ok) {
        // Попытка получить текст ошибки из ответа
        const errorText = await res.text();
        throw new Error(errorText || "Ошибка при отправке данных");
      }

      console.log("Данные успешно отправлены!");
      navigate("/personalAccount"); 
    } catch (error) {
      console.error("Ошибка:", error);
    }
  };

  if (!isDataLoaded) {
    return <div>Загрузка данных...</div>;
  }

  return (
    <div className="questionnaire-page-only">
      <div className="questionnaire-header-only" onClick={handleBack}>
        ← <span>Проектная мастерская</span>
      </div>
      <h2 className="questionnaire-title-only">Расскажите о себе</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group-only">
          <label>1. Укажите ВУЗ, в котором вы учились или учитесь</label>
          <input
            type="text"
            name="university"
            value={formData.university}
            onChange={handleChange}
            placeholder="ВУЗ"
          />
        </div>
        <div className="form-group-only">
          <label>2. Укажите год окончания ВУЗа</label>
          <input
            type="text"
            name="graduationYear"
            value={formData.graduationYear}
            onChange={handleChange}
            placeholder="Год"
          />
        </div>
        <div className="form-group-only">
          <label>3. На каком факультете вы учились или учитесь</label>
          <input
            type="text"
            name="faculty"
            value={formData.faculty}
            onChange={handleChange}
            placeholder="Факультет"
          />
        </div>
        <div className="form-group-only">
          <label>
            4. Расскажите о своём прошлом опыте. Над какими проектами работали? С какими сложностями сталкивались? Чем можете похвастаться? Не стесняйтесь
          </label>
          <textarea
            name="experience"
            value={formData.experience}
            onChange={handleChange}
            placeholder="Ответ"
          ></textarea>
        </div>
        <div className="form-group-only">
          <label>
            5. Оцените степень владения языками программирования по шкале от 1 до 5, где 1 — читаю код, 3 — уверенно использую в учебе, 5 — реализовал проект в промышленной разработке. Если знакомы с фреймворками и библиотеками, расскажите и про них. Например: "Python 4, TensorFlow 3, JavaScript 2"
          </label>
          <textarea
            name="programmingSkills"
            value={formData.programmingSkills}
            onChange={handleChange}
            placeholder="Введите текст"
          ></textarea>
        </div>
        <div className="form-group-only">
          <label>6. Расскажите кратко о своем опыте работы с языками/технологиями</label>
          <textarea
            name="technologies"
            value={formData.technologies}
            onChange={handleChange}
            placeholder="Введите текст"
          ></textarea>
        </div>
        <div className="form-group-only">
          <label>7. Укажите ваш ник в telegram для связи в формате @...</label>
          <input
            type="text"
            name="telegram"
            value={formData.telegram}
            onChange={handleChange}
            placeholder="@username"
          />
        </div>
        <div className="form-group-only">
          <label>
            8. В роли кого бы ты хотел быть на проектной мастерской? Варианты: разработчик backend, разработчик frontend, QA, аналитик, могу всё.
          </label>
          <input
            type="text"
            name="role"
            value={formData.role}
            onChange={handleChange}
            placeholder="Ответ"
          />
        </div>
        <div className="form-group-only">
          <label>9. Прикрепите ссылку на ваш GitHub</label>
          <input
            type="text"
            name="github"
            value={formData.github}
            onChange={handleChange}
            placeholder="Ответ"
          />
        </div>
        <button type="submit" className="submit-button-only">
          Отправить
        </button>
      </form>
    </div>
  );
};

export default QuestionnairePage;