import React from "react";
import styles from './ControlThread.module.css';
import { startAllSenderThreads, stopAllSenderThreads, restartAllSenderThreads } from "../../api/SenderApi";
import { startAllReceiverThreads, stopAllReceiverThreads, restartAllReceiverThreads } from "../../api/ReceiverApi";

const ControlThread = () => {

  const handleStartAllThreads = async () => {
    try {
      const responseSender = await startAllSenderThreads();
      const responseReceiver = await startAllReceiverThreads();
      alert(
        responseReceiver + "\n" + responseSender
      );
    } catch (error) {
      alert("Error: " + error);
    }
  };

  const handleStopAllThreads = async () => {
    try {
      const responseSender = await stopAllSenderThreads();
      const responseReceiver = await stopAllReceiverThreads();
      alert(
        responseReceiver + "\n" + responseSender
      );
    } catch (error) {
      alert("Error: " + error);
    }
  };

  const handleRestartAllThreads = async () => {
    try {
      const responseSender = await restartAllSenderThreads();
      const responseReceiver = await restartAllReceiverThreads();
      alert(
        responseReceiver + "\n" + responseSender
      );
    } catch (error) {
      alert("Error: " + error);
    }
  };

  return (
    <div className={styles["control-container"]}>
      <h2 className={styles["control-title"]}>Control Threads</h2>
      <div className={styles["control-buttons"]}>
        <button id="start-button" onClick={handleStartAllThreads}>Start All Threads</button>
        <button id="stop-button" onClick={handleStopAllThreads}>Stop All Threads</button>
        <button id="restart-button" onClick={handleRestartAllThreads}>Restart All Threads</button>
      </div>
    </div>
  );
};

export default ControlThread;
