import React, { useState } from "react";
import {
  changeThreadPriority
} from "../../api/ThreadApi";

const PriorityControl = () => {
  const [index, setIndex] = useState();
  const [priority, setPriority] = useState(5);
  const [message, setMessage] = useState("");

  const handleChangePriority = async () => {
    try {
      const response = await changeThreadPriority(index, priority);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  return (
    <div>
      <h1>Change Thread Priority</h1>

      <div>
        <h2>Change Thread Priority</h2>
        <label>
            Thread Index:
          <input
            type="text"
            value={index}
            onChange={(e) => setIndex(Number(e.target.value))}
          />
        </label>
        <label>
          Priority:
          <select
              value={priority}
              onChange={(e) => setPriority(e.target.value)}
            >
              {Array.from({ length: 10 }, (_, i) => i + 1).map((num) => (
                <option key={num} value={num}>
                  {num}
                </option>
              ))}
            </select>
        </label>
        <button onClick={handleChangePriority}>Change Sender Priority</button>
      </div>

    

      {message && <p>{message}</p>}
    </div>
  );
};

export default PriorityControl;
