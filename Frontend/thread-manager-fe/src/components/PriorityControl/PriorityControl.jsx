import React, { useState } from "react";
import styles from './PriorityControl.module.css';
import { changeThreadPriority } from "../../api/ThreadApi";

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
    <div className={styles["priority-container"]}>
      <h2 className={styles["priority-title"]}>Change Thread Priority</h2>
      <div className={styles["priority-form"]}>
        <label>Thread Index:</label>
        <input
          type="number"
          value={index}
          onChange={(e) => setIndex(Number(e.target.value))}
        />
        <label>Priority:</label>
        <select value={priority} onChange={(e) => setPriority(e.target.value)}>
          {Array.from({ length: 10 }, (_, i) => i + 1).map((num) => (
            <option key={num} value={num}>
              {num}
            </option>
          ))}
        </select>
        <button onClick={handleChangePriority}>Change Priority</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default PriorityControl;
