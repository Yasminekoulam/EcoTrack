import React, { useState, useEffect } from 'react';
import { profil, accountMenu, accountCross, notification } from '../assets';
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';

const AccountNavbar = () => {
  const [showMenu, setShowMenu] = useState(false);
  const [showProfileMenu, setShowProfileMenu] = useState(false);
  const navigate = useNavigate();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    axios
      .get("http://localhost:8080/api/notifications/unread", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => setNotifications(response.data))
      .catch((error) =>
        console.log("Erreur lors du chargement des notifications:", error)
      );
  }, []);

  useEffect(() => {
    document.body.style.overflow = showMenu ? 'hidden' : 'auto';
    return () => {
      document.body.style.overflow = 'auto';
    };
  }, [showMenu]);

  return (
    <>
      <div className="fixed top-2 left-2 z-50">
        {!showMenu && (
          <button onClick={() => setShowMenu(true)}>
            <img src={accountMenu} className="h-8 w-8 rounded-full" alt="Menu" />
          </button>
        )}
      </div>

      {showMenu && (
        <div className="fixed top-0 left-0 flex flex-col text-white bg-green-600 shadow-md h-screen w-40 gap-4 p-2 z-40">
          <div className="flex justify-start cursor-pointer">
            <img
              onClick={() => setShowMenu(false)}
              src={accountCross}
              className="w-8"
              alt="Close menu"
            />
          </div>
          <Link to="/homePage">Home</Link>
          <Link to="/myAccount">Account</Link>
          <Link to="/addActivity">Add Activity</Link>
          <Link to="/activities">My activities</Link>
          <Link to="/statistics">Statistics</Link>
          <Link to="/advices">Advices</Link>
          <Link to="/goals">My goals</Link>
          <Link to="/badge">My badge</Link>
          <Link to="/review">Add review</Link>
          <Link to="/settings">Settings</Link>
          <button 
            onClick={() => {
              localStorage.removeItem("jwtToken");
              setShowMenu(false);
              navigate("/login");
            }}
            className="text-left"
          >
            Sign out
          </button>
        </div>
      )}

      <div className="fixed top-2 right-2 flex items-center gap-3 z-50">
      
        <div className="relative">
          <button onClick={() => navigate("/notifications")}>
            <img
              src={notification}
              className="h-8 w-8 rounded-full"
              alt="notification"
            />
            {notifications.length > 0 && (
              <span className="absolute -top-1 -right-1 bg-red-600 text-white text-xs font-bold px-1.5 py-0.5 rounded-full">
                {notifications.length}
              </span>
            )}
          </button>
        </div>

        <div className="relative">
          <button onClick={() => setShowProfileMenu(!showProfileMenu)}>
            <img src={profil} className="h-8 w-8 rounded-full" alt="Profile" />
          </button>

          {showProfileMenu && (
            <div className="absolute right-0 mt-2 text-green-600 w-40 bg-white shadow-lg rounded-lg py-2 z-50">
              <Link to="/myAccount" className="block px-4 py-2 hover:bg-gray-100">
                Account
              </Link>
              <button 
                onClick={() => {
                  localStorage.removeItem("jwtToken");
                  setShowProfileMenu(false);
                  navigate("/login");
                }}
                className="block px-4 py-2 hover:bg-gray-100 text-left w-full"
              >
                Sign out
              </button>
            </div>
          )}
        </div>
      </div>
    </>
  );
};

export default AccountNavbar;
