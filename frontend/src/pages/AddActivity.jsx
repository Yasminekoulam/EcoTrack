import React, { useEffect, useState } from "react";
import AccountNavbar from "../components/AccountNavbar";
import axios from "axios";
import LabelField from "../components/LabelField";  
import Popup from "../components/Popup";

const AddActivity = () => {
  const [activityTypes, setActivityTypes] = useState([]);
  const [categories, setCategories] = useState([]);
  const [categoryType, setCategoryType] = useState("");
  const [name, setName] = useState("");
  const [nbrPersonnes, setNbrPersonnes] = useState(1);
  const [quantity, setQuantity] = useState(0.0);
  const [sharingType, setSharingType] = useState("SOLO");
  const [date, setDate] = useState("")
  const [popup, setPopup] = useState({ isOpen: false, message: "", type: "success" });

  const increment = () => setNbrPersonnes((prev) => prev + 1);
  const decrement = () => setNbrPersonnes((prev) => (prev > 1 ? prev - 1 : 1));

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");
    axios
      .get("http://localhost:8080/api/categories/", {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => setCategories(response.data))
      .catch((error) => console.log("Erreur lors du chargement des catégories:", error));
  }, []);

  useEffect(() => {
    if (!categoryType) return;
    const token = localStorage.getItem("jwtToken");
    axios
      .get(`http://localhost:8080/api/activity-types/by-category/${categoryType}`, {
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((response) => setActivityTypes(response.data))
      .catch((error) => console.log("Erreur lors du chargement des types d'activité:", error));
  }, [categoryType]);

  const handleSubmit = async () => {
    const token = localStorage.getItem("jwtToken");
    const payload = {
      activityTypeName: name,
      nbrPersonnes,
      quantity: parseFloat(quantity),
      sharingType,
      date,
    };

    try {
      await axios.post("http://localhost:8080/api/user_activities", payload, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPopup({ isOpen: true, message: "Activity added with success", type: "success" });
    } catch (error) {
      console.error("Erreur lors de l'envoi:", error);
      setPopup({ isOpen: true, message: "Failed ", type: "error" });
    }
  };

  return (
    <div>
      <AccountNavbar />
      <div className="flex justify-center items-center h-screen bg-gray-100">
        <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-xl space-y-4">

          <LabelField
            label="Sharing Type:"
            value={sharingType}
            onChange={(e) => setSharingType(e.target.value)}
            options={[
              { id: "PRIVATE", name: "PRIVATE" },
              { id: "SOLO", name: "SOLO" },
              { id: "PUBLIC", name: "PUBLIC" },
            ]}
          />

          <LabelField
            label="Category:"
            value={categoryType}
            onChange={(e) => setCategoryType(e.target.value)}
            options={categories}
            valueKey="id"
            labelKey="categoryType"
          />

          <LabelField
            label="Activity Type:"
            value={name}
            onChange={(e) => setName(e.target.value)}
            options={activityTypes}
            valueKey="name"
            labelKey="name"
          />

          <div className="flex items-center gap-2">
            <label className="mb-1 block text-orange-600 font-medium">Share whith: </label>
            <button
              type="button"
              onClick={decrement}
              className="px-3 py-1 bg-orange-400 text-white rounded-lg hover:bg-orange-500"
            >
              −
            </button>
            <input
              type="number"
              value={nbrPersonnes}
              min={1}
              onChange={(e) => setNbrPersonnes(Math.max(1, parseInt(e.target.value, 10) || 1))}
              className="w-16 text-center text-orange-800 border border-green-600 rounded-lg p-1"
            />
            <button
              type="button"
              onClick={increment}
              className="px-3 py-1 bg-orange-400 text-white rounded-lg hover:bg-orange-500"
            >
              +
            </button>
          </div>

          <div>
            <label className="mb-1 block text-orange-600 font-medium">Quantity: Kg, Km,...</label>
            <input
              type="number"
              step="0.1"
              min="0"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              className="w-24 text-center text-orange-800 border border-green-600 rounded p-1"
              placeholder="Quantity"
            />
          </div>

          <div>
            <label className="mb-1 block text-orange-600 font-medium">Date:</label>
            <input
              type="date"
              value={date}
              onChange={(e) => setDate(e.target.value)}
              className="w-24 text-center text-orange-800 border border-green-600 rounded p-1"
              placeholder="Date"
            />
          </div>



          <button
            type="button"
            onClick={handleSubmit}
            className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700 transition-colors"
          >
            Save
          </button>

          <Popup
            isOpen={popup.isOpen}
            message={popup.message}
            type={popup.type}
            onClose={() => setPopup({ ...popup, isOpen: false })}
          />
        </div>
      </div>
    </div>
  );
};

export default AddActivity;
