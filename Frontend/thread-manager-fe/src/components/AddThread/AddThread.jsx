import React, { useState } from "react";
import { addSenders, addReceivers } from "../../api/ThreadApi";

const AddThreads = () => {
  const [senderCount, setSenderCount] = useState(0);
  const [senderPriorityChangeable, setSenderPriorityChangeable] = useState(false);
  const [receiverCount, setReceiverCount] = useState(0);
  const [receiverPriorityChangeable, setReceiverPriorityChangeable] = useState(false);
  const [message, setMessage] = useState("");

  const handleAddSenders = async () => {
    try {
      const response = await addSenders(senderCount, senderPriorityChangeable);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleAddReceivers = async () => {
    try {
      const response = await addReceivers(receiverCount, receiverPriorityChangeable);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  return (
    <div>
      <h1>Thread Management</h1>

      <div>
        <h2>Add Sender Threads</h2>
        <label>
          Count:
          <input
            type="number"
            value={senderCount}
            onChange={(e) => setSenderCount(Number(e.target.value))}
          />
        </label>
        <label>
          Priority Changeable:
          <input
            type="checkbox"
            checked={senderPriorityChangeable}
            onChange={(e) => setSenderPriorityChangeable(e.target.checked)}
          />
        </label>
        <button onClick={handleAddSenders}>Add Senders</button>
      </div>

      <div>
        <h2>Add Receiver Threads</h2>
        <label>
          Count:
          <input
            type="number"
            value={receiverCount}
            onChange={(e) => setReceiverCount(Number(e.target.value))}
          />
        </label>
        <label>
          Priority Changeable:
          <input
            type="checkbox"
            checked={receiverPriorityChangeable}
            onChange={(e) => setReceiverPriorityChangeable(e.target.checked)}
          />
        </label>
        <button onClick={handleAddReceivers}>Add Receivers</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default AddThreads;
