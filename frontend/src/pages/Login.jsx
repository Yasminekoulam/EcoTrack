import React, { useState } from "react";
import {useNavigate} from 'react-router-dom';
import { loginBackground } from "../assets";
import InputField from "../components/InputField";
import axios from 'axios'
import Popup from '../components/Popup'

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [showPopup, setShowPopup] = useState(false)
  const [popupMessage, setPopupMessage] = useState("")
  const [popupType, setPopupType] = useState("")

  const navigate = useNavigate();
  const handleSubmit = async (e) => {
    e.preventDefault();
    try{
      const response = await axios.post("http://localhost:8080/api/auth/login",{
        email,
        password
      },{
        headers: {
          'Content-Type': 'application/json'
        }
      })
      if (response.status === 200) {
     
      const token = response.data;
      localStorage.setItem("jwtToken", token);

      setTimeout(() => {
        navigate('/HomePage');
      }, 1000);
    }

    } catch(error){
      setPopupMessage("Login failed" )
      setPopupType("error")
      setShowPopup(true)
      console.log(error);
    }
  };
  const closePopup = () => setShowPopup(false)

  return (
    <div
      className="min-h-screen flex items-center justify-center relative"
      style={{
        backgroundImage: `url(${loginBackground})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
     
      <div className="relative z-10 w-full max-w-md p-10 rounded-xl bg-white/20 backdrop-blur-md shadow-lg">
        <h2 className="text-3xl font-bold text-orange-400 mb-6 text-center">
          Login
        </h2>
        <form onSubmit={handleSubmit} className="flex flex-col gap-4">
          
          <InputField
          label='Email'
          type='email'
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          placeholder='Enter your email'
        />
          <InputField
          label='Password'
          type='password'
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder='Enter your password'
        />
          <button
            type="submit"
            className="bg-orange-400 text-white rounded-lg py-2 mt-2 hover:bg-orange-500 transition-colors"
          >
            Login
          </button>        
        </form>
        <Popup 
          isOpen={showPopup}
          message={popupMessage}
          type={popupType}
          onClose={closePopup}
        />
        <p className="mt-4 text-center text-white/80">
          Don't have an account?{" "}
          <a
            href="/register"
            className="underline text-orange-300 hover:text-orange-400"
          >
            Sign up
          </a>
        </p>
      </div>
    </div>
  );
};

export default Login;