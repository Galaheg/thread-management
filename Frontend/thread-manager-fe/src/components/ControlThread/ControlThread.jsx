// ThreadControlComponent.js

import React, { useState } from "react";
import {
  startAllThreads,
  stopAllThreads,
  startThread,
  stopThread,
  restartThread,
} from "../../api/ThreadApi";

const ThreadControl = () => {
  const [threadIndex, setThreadIndex] = useState(0);
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
      const response = await startThread(threadIndex);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleStopThread = async () => {
    try {
      const response = await stopThread(threadIndex);
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  const handleRestartThread = async () => {
    try {
      const response = await restartThread(threadIndex);
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
            value={threadIndex}
            onChange={(e) => setThreadIndex(Number(e.target.value))}
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
            value={threadIndex}
            onChange={(e) => setThreadIndex(Number(e.target.value))}
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
            value={threadIndex}
            onChange={(e) => setThreadIndex(Number(e.target.value))}
          />
        </label>
        <button onClick={handleRestartThread}>Restart Thread</button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
};

export default ThreadControl;
