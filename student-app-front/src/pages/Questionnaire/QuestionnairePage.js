import React, { useEffect, useState } from "react";
import "./QuestionnairePage.css";

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
    github: ""
  });

  // Имитация запроса к базе данных 
  useEffect(() => {
    setTimeout(() => {
      const mockDBData = {
        university: "РГРТУ",
        graduationYear: "2023",
        faculty: "Факультет ВМК",
        experience: "Работал над несколькими pet-проектами и стажировался в стартапе.",
        programmingSkills: "Python — 4, JavaScript — 3, C++ — 2",
        technologies: "React, Node.js, PostgreSQL",
        telegram: "@exampleuser",
        role: "Fullstack разработчик",
        github: "https://github.com/example"
      };
      setFormData(mockDBData);
    }, 1000); // эмуляция задержки
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleBack = () => {
    window.history.back();
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Отправка данных в БД:", formData);
    alert("Данные отправлены!");
  };

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
            name="graduationYear"
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
          <label>4. Расскажите о своём прошлом опыте. Над какими проектами работали? с какими сложностями сталкивались? Чем можете похвастаться? Не стесняйтесь</label>
          <textarea
            name="experience"
            value={formData.experience}
            onChange={handleChange}
            placeholder="Ответ"
          ></textarea>
        </div>
        <div className="form-group-only">
          <label>5. Оцените степень владения языками программирования по шкале от 1 до 5, где 1 — читаю код, 3 — уверенно использую в учебе, 5 — реализовал проект в промышленной разработке. Если знакомы с фреймворками и библиотеками, расскажите и про них. Например: "Python 4, TensorFlow 3, JavaScript 2" </label>
          <textarea
            name="programmingSkills"
            value={formData.programmingSkills}
            onChange={handleChange}
            placeholder="Введите текст"
          ></textarea>
        </div>
        <div className="form-group-only">
          <label>6. Расскажите кратко о своем опыте работы с языками/технологиях</label>
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
          <label>8. В роли кого бы ты хотел быть на проектной мастерской? Варианты: разработчик backend, разработчик frontend, QA, аналитик, могу всё.</label>
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
        <button type="submit" className="submit-button-only">Отправить</button>
      </form>
    </div>
  );
};

export default QuestionnairePage;
