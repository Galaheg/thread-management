import React, { useState } from "react";
import {
  startAllThreads,
  stopAllThreads,
  restartAllThreads
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

  const handleRestartAllThreads = async () => {
    try {
      const response = await restartAllThreads();
      setMessage(response);
    } catch (error) {
      setMessage("Error: " + error);
    }
  };

  /*const handleStartThread = async () => {
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
*/
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
        <h2>Restart All Threads</h2>
        <button onClick={handleRestartAllThreads}>Restart All Threads</button>
      </div>
      

      {message && <p>{message}</p>}
    </div>
  );
};

export default ControlThread;
