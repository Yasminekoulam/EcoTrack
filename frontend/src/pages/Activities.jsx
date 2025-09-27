import React, { useState, useEffect } from 'react';
import AccountNavbar from '../components/AccountNavbar';
import axios from 'axios';

const Activities = () => {
  const [userActivities, setUserActivities] = useState([]);
  const [userId, setUserId] = useState();
  const [editingActivity, setEditingActivity] = useState(null);
  const [editForm, setEditForm] = useState({
    quantity: '',
    nbrPersonnes: '',
    date: ''
  });
  
  useEffect(() => {
    const token = localStorage.getItem("jwtToken");

    axios
      .get("http://localhost:8080/api/user/", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => {
        const userDTO = response.data;
        if (userDTO) setUserId(userDTO.id);
      })
      .catch((error) => {
        console.error("Error fetching user:", error);
      });
  }, []);

  useEffect(() => {
    if (!userId) return;

    const token = localStorage.getItem("jwtToken");
    axios
      .get(`http://localhost:8080/api/user_activities/byUser/${userId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(async (response) => {
        const activities = response.data;
        
        const enrichedActivities = await Promise.all(
          activities.map(async (activity) => {
            try {
              const typeResponse = await axios.get(
                `http://localhost:8080/api/activity-types/${activity.activityTypeId}`,
                { headers: { Authorization: `Bearer ${token}` } }
              );
              const categoryResponse = await axios.get(
                `http://localhost:8080/api/categories/${typeResponse.data.categoryId}`,
                { headers: { Authorization: `Bearer ${token}` } }
              ) 
              return {
                ...activity,
                activityTypeName: typeResponse.data.name,
                categoryType: categoryResponse.data.categoryType,
              };
            } catch (err) {
              console.error("Erreur fetch activityType:", err);
              return activity;
            }
          })
        );

        setUserActivities(enrichedActivities);
      })
      .catch((error) =>
        console.log("Erreur lors du chargement des User activities:", error)
      );
  }, [userId]);

  const deleteActivity = (activityId) => {
    const token = localStorage.getItem("jwtToken")
    axios
      .delete(`http://localhost:8080/api/user_activities/${activityId}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then(() => {
        setUserActivities(userActivities.filter((activity) => activity.id !== activityId))
      })
      .catch((error) => console.log("Erreur lors de la suppression de activity:", error))
  }

  const handleEditClick = (activity) => {
  setEditingActivity(activity.id);
  setEditForm({
    quantity: activity.quantity,
    nbrPersonnes: activity.nbrPersonnes,
    date: activity.date
  });
};

const handleUpdateActivity = async (activityId) => {
  const token = localStorage.getItem("jwtToken");
  
  const activityToUpdate = userActivities.find(a => a.id === activityId);
  const updatedActivity = {
    ...activityToUpdate,
    quantity: editForm.quantity,
    nbrPersonnes: editForm.nbrPersonnes,
    date: editForm.date,
     id: activityId,
     userId,
  };

  try {
    const response = await axios.put(
      "http://localhost:8080/api/user_activities",
      updatedActivity,
      { headers: { Authorization: `Bearer ${token}` } }
    );
    
    setUserActivities(userActivities.map(activity => 
      activity.id === activityId ? { ...activity, ...editForm } : activity
    ));
    
    setEditingActivity(null);
  } catch (error) {
    console.error("Erreur lors de la mise à jour:", error);
  }
};

const handleCancelEdit = () => {
  setEditingActivity(null);
  setEditForm({ quantity: '', nbrPersonnes: '', date: '' });
};

  return (
    <div>
      <AccountNavbar />
      <div className="flex flex-col items-center">
        <h2 className="font-bold text-orange-600 text-lg underline">Activities List</h2>
        <div className="flex-col shadow-xl w-96 min-h-screen mt-6 text-green-600 w-screen">
          <ol className="list-decimal pl-5">
            {userActivities.length > 0 ? (
              userActivities.map((activity) => (
                <li key={activity.id} className="shadow-xl p-6 rounded-lg bg-white border-l-4 border-orange-600 mt-2">
                <p><span className="font-semibold">Type:</span> {activity.activityTypeName}</p>
                <p><span className="font-semibold">Category:</span> {activity.categoryType}</p>
                
                {editingActivity === activity.id ? (
                
              <div className="space-y-2 mt-2">
                  <div>
                    <label className="font-semibold">Quantity:</label>
                    <input
                      type="number"
                      value={editForm.quantity}
                      onChange={(e) => setEditForm({...editForm, quantity: e.target.value})}
                      className="ml-2 border rounded px-2 py-1"
                    />
                  </div>
                  <div>
                    <label className="font-semibold">Share with:</label>
                    <input
                      type="number"
                      value={editForm.nbrPersonnes}
                      onChange={(e) => setEditForm({...editForm, nbrPersonnes: e.target.value})}
                      className="ml-2 border rounded px-2 py-1"
                    />
                  </div>
                  <div>
                    <label className="font-semibold">Date:</label>
                    <input
                      type="date"
                      value={editForm.date}
                      onChange={(e) => setEditForm({...editForm, date: e.target.value})}
                      className="ml-2 border rounded px-2 py-1"
                    />
                  </div>
                  <div className="flex gap-2 mt-3">
                    <button
                      onClick={() => handleUpdateActivity(activity.id)}
                      className="text-white bg-green-600 rounded-lg px-3 py-1"
                    >
                      Save
                    </button>
                    <button
                      onClick={handleCancelEdit}
                      className="text-white bg-gray-600 rounded-lg px-3 py-1"
                    >
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                
                  <>
                    <p><span className="font-semibold">Quantity:</span> {activity.quantity}</p>
                    <p><span className="font-semibold">Share with:</span> {activity.nbrPersonnes}</p>
                      <p><span className="font-semibold">Date:</span> {activity.date}</p>
                      <div className="flex gap-2 mt-3">
                        <button
                          onClick={() => deleteActivity(activity.id)}
                          className="text-white bg-red-600 rounded-lg px-3 py-1"
                      >
                          Delete
                        </button>
                        <button
                          onClick={() => handleEditClick(activity)}
                          className="text-white bg-blue-500 rounded-lg px-3 py-1"
                        >
                          Update
                        </button>
                      </div>
                    </>
                    )}
                  </li>
              ))
            ) : (
              <p className="text-gray-500 italic">No activity for now</p>
            )}
          </ol>
        </div>
      </div>
    </div>
  );
};

export default Activities;