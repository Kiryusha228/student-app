import React from 'react';

import {
  BrowserRouter as Router,
  Routes,
  Link,
  Route,
} from 'react-router-dom';

import Home from "./pages/Home/Home"; //Главная страница
import Questionnaire from "./pages/Questionnaire/QuestionnairePage"; //страница анкеты
import TestPage from "./pages/TestPage/TestPage"; //страница теста
import Registration from "./pages/Registration/Registration";//страница регистрации
import Password from "./pages/Password/Password";//страница ввода пароля
import PersonalAccount from "./pages/PersonalAccount/PersonalAccount";//страница ЛК

import Header from "./components/Header"; //шапка
import Footer from "./components/Footer"; //подвал

function App(){
  return(
    <Router>
      <Header /> 
        <main className='container'>
          <Routes>
            <Route path = '/' element = {<Home />}/>
            <Route path = "/registration" element = {<Registration />}/>
            <Route path = "/password" element = {<Password />}/>
            <Route path = "/personalAccount" element = {<PersonalAccount/>}/>
            <Route path="/test/:examId" element={<TestPage />} /> {/* Параметр examId */}
            <Route path = "/questionnaire" element = {<Questionnaire/>}/>
          </Routes>
        </main>
      <Footer />
    </Router>
  );
}

export default App;



