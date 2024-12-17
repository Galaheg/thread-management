import React, { useState } from "react";
import styles from './AddThread.module.css';
import { createSenders } from "../../api/SenderApi";
import { createReceivers } from "../../api/ReceiverApi";

const AddThreads = () => {
  const [senderCount, setSenderCount] = useState(1);
  const [senderPriorityChangeable, setSenderPriorityChangeable] = useState(false);
  const [receiverCount, setReceiverCount] = useState(1);
  const [receiverPriorityChangeable, setReceiverPriorityChangeable] = useState(false);
  const [message, setMessage] = useState("");

  const handleAddSenders = async () => {
    if (senderCount <= 0) {
      alert("Invalid number of Sender Threads");
      return;
    }
    try {
      const response = await createSenders(senderCount, senderPriorityChangeable);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleAddReceivers = async () => {
    if (receiverCount <= 0) {
      alert("Invalid number of Receiver Threads");
      return;
    }
    try {
      const response = await createReceivers(receiverCount, receiverPriorityChangeable);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  return (
    <div className={styles["thread-container"]}>
      <h2 className={styles["thread-title"]}>Create Sender Threads</h2>
      <div className={styles["thread-form"]}>
        <label>Count:</label>
        <input
          type="number"
          value={senderCount}
          onChange={(e) => {
            const value = e.target.value;
            if (value === "") {
              setSenderCount("");
            } else {
              setSenderCount(Number(value));
            }
          }}
        />
        <div className={styles["checkbox-group"]}>
        <label>Priority Changeable:</label>
        <input
          type="checkbox"
          checked={senderPriorityChangeable}
          onChange={(e) => setSenderPriorityChangeable(e.target.checked)}
        />
        </div>
       
        <button onClick={handleAddSenders}>Create Senders</button>
      </div>

      <h2 className={styles["thread-title"]}>Create Receiver Threads</h2>
      <div className={styles["thread-form"]}>
        <label>Count:</label>
        <input
          type="number"
          value={receiverCount}
          onChange={(e) => {
            const value = e.target.value;
            if (value === "") {
              setReceiverCount("");
            } else {
              setReceiverCount(Number(value));
            }
          }}
        />
        <div className={styles["checkbox-group"]}>
        <label>Priority Changeable:</label>
        <input
          type="checkbox"
          checked={receiverPriorityChangeable}
          onChange={(e) => setReceiverPriorityChangeable(e.target.checked)}
        />
        </div>
        
        <button onClick={handleAddReceivers}>Create Receivers</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default AddThreads;
