import React, { useState } from "react";
import styles from './PriorityControl.module.css';
import { changeSenderThreadPriority } from "../../api/SenderApi";
import { changeReceiverThreadPriority } from "../../api/ReceiverApi";

const PriorityControl = () => {
  const [senderIndex, setSenderIndex] = useState(0);
  const [receiverIndex, setReceiverIndex] = useState(0);
  const [senderPriority, setSenderPriority] = useState(5);
  const [receiverPriority, setReceiverPriority] = useState(5);
  const [message, setMessage] = useState("");

  
  const handleChangeSenderPriority = async () => {
    try {
      const response = await changeSenderThreadPriority(senderIndex, senderPriority);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  
  const handleChangeReceiverPriority = async () => {
    try {
      const response = await changeReceiverThreadPriority(receiverIndex, receiverPriority);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  return (
    <div className={styles["priority-container"]}>
      <h2 className={styles["priority-title"]}>Change Thread Priority</h2>
      <div className={styles["priority-form"]}>
        <label>Sender Thread Index:</label>
        <input
          type="number"
          value={senderIndex}
          onChange={(e) => {
            const value = e.target.value;
            if (value === "") {
              setSenderIndex("");
            } else {
              setSenderIndex(Number(value));
            }
          }}
        />
        <label>Sender Priority:</label>
        <select value={senderPriority} onChange={(e) => setSenderPriority(e.target.value)}>
          {Array.from({ length: 10 }, (_, i) => i + 1).map((num) => (
            <option key={num} value={num}>
              {num}
            </option>
          ))}
        </select>
        <button onClick={handleChangeSenderPriority}>Change Sender Priority</button>
      </div>

      <div className={styles["priority-form"]}>
        <label>Receiver Thread Index:</label>
        <input
          type="number"
          value={receiverIndex}
          onChange={(e) => {
            const value = e.target.value;
            if (value === "") {
              setReceiverIndex("");
            } else {
              setReceiverIndex(Number(value));
            }
          }}
        />
        <label>Receiver Priority:</label>
        <select value={receiverPriority} onChange={(e) => setReceiverPriority(e.target.value)}>
          {Array.from({ length: 10 }, (_, i) => i + 1).map((num) => (
            <option key={num} value={num}>
              {num}
            </option>
          ))}
        </select>
        <button onClick={handleChangeReceiverPriority}>Change Receiver Priority</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default PriorityControl;
