import React, { useState } from "react";
import {
  startAllThreads,
  stopAllThreads,
  startThread,
  stopThread,
  restartThread,
} from "../../api/ThreadApi";

const ControlThread = () => {
  const [startIndex, setStartIndex] = useState(0);
  const [stopIndex, setStopIndex] = useState(0);
  const [restartIndex, setRestartIndex] = useState(0);
  const [message, setMessage] = useState("");

  const handleStartAllThreads = async () => {
    try {
      const response = await startAllThreads();
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleStopAllThreads = async () => {
    try {
      const response = await stopAllThreads();
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleStartThread = async () => {
    try {
      const response = await startThread(startIndex);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleStopThread = async () => {
    try {
      const response = await stopThread(stopIndex);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleRestartThread = async () => {
    try {
      const response = await restartThread(restartIndex);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  return (
    <div>
      <h1>Thread Control</h1>

      <div>
        <h2>Start All Threads</h2>
        <button onClick={handleStartAllThreads}>Start All Threads</button>
      </div>

      <div>
        <h2>Stop All Threads</h2>
        <button onClick={handleStopAllThreads}>Stop All Threads</button>
      </div>

      <div>
        <h2>Start a Specific Thread</h2>
        <label>
          Thread Index:
          <input
            type="number"
            value={startIndex}
            onChange={(e) => setStartIndex(Number(e.target.value))}
          />
        </label>
        <button onClick={handleStartThread}>Start Thread</button>
      </div>

      <div>
        <h2>Stop a Specific Thread</h2>
        <label>
          Thread Index:
          <input
            type="number"
            value={stopIndex}
            onChange={(e) => setStopIndex(Number(e.target.value))}
          />
        </label>
        <button onClick={handleStopThread}>Stop Thread</button>
      </div>

      <div>
        <h2>Restart a Specific Thread</h2>
        <label>
          Thread Index:
          <input
            type="number"
            value={restartIndex}
            onChange={(e) => setRestartIndex(Number(e.target.value))}
          />
        </label>
        <button onClick={handleRestartThread}>Restart Thread</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default ControlThread;
