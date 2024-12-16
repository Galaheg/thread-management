import React from "react";
import styles from './ControlThread.module.css';
import { startAllThreads, stopAllThreads, restartAllThreads } from "../../api/ThreadApi";

const ControlThread = () => {

  const handleStartAllThreads = async () => {
    try {
      const response = await startAllThreads();
      alert(response);
    } catch (error) {
      alert("Error: " + error);
    }
  };

  const handleStopAllThreads = async () => {
    try {
      const response = await stopAllThreads();
      alert(response);
    } catch (error) {
      alert("Error: " + error);
    }
  };

  const handleRestartAllThreads = async () => {
    try {
      const response = await restartAllThreads();
      alert(response);
    } catch (error) {
      alert("Error: " + error);
    }
  };

  return (
    <div className={styles["control-container"]}>
      <h2 className={styles["control-title"]}>Control Threads</h2>
      <div className={styles["control-buttons"]}>
        <button onClick={handleStartAllThreads}>Start All Threads</button>
        <button onClick={handleStopAllThreads}>Stop All Threads</button>
        <button onClick={handleRestartAllThreads}>Restart All Threads</button>
      </div>
    </div>
  );
};

export default ControlThread;
