import React, { useEffect, useState } from "react";
import { ThreadStateEnum } from "../utils/enums/ThreadStateEnum";
import { ThreadTypeEnum } from "../utils/enums/ThreadTypeEnum";
import { startSenderThread, stopSenderThread, restartSenderThread } from "../../api/SenderApi";
import { startReceiverThread, stopReceiverThread, restartReceiverThread } from "../../api/ReceiverApi";
import styles from './ThreadDetails.module.css';

const ThreadList = () => {
  const [senderThreads, setSenderThreads] = useState([]);
  const [receiverThreads, setReceiverThreads] = useState([]);

  useEffect(() => {
    const receiverEventSource = new EventSource("http://localhost:8081/api/receivers/stream");
    const senderEventSource = new EventSource("http://localhost:8082/api/senders/stream");

    senderEventSource.addEventListener("sender-threads-update", (event) => {
      const senderThreadData = JSON.parse(event.data);
      setSenderThreads(senderThreadData);
    });

    receiverEventSource.addEventListener("receiver-threads-update", (event) => {
      const receiverThreadData = JSON.parse(event.data);
      setReceiverThreads(receiverThreadData);
    });

    senderEventSource.onerror = () => {
      console.error("Sender SSE bağlantı hatası");
      senderEventSource.close();
    };

    receiverEventSource.onerror = () => {
      console.error("Receiver SSE bağlantı hatası");
      receiverEventSource.close();
    };

    return () => {
      senderEventSource.close(); // Component unmount edildiğinde bağlantıyı kapat
      receiverEventSource.close();
    };

  }, []);

  const handleSenderStart = async (index) => {
    try {
      const response = await startSenderThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };
  const handleReceiverStart = async (index) => {
    try {
      const response = await startReceiverThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  const handleSenderStop = async (index) => {
    try {
      const response = await stopSenderThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };
  const handleReceiverStop = async (index) => {
    try {
      const response = await stopReceiverThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  const handleSenderRestart = async (index) => {
    try {
      const response = await restartSenderThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  const handleReceiverRestart = async (index) => {
    try {
      const response = await restartReceiverThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  return (
    <div className={styles["thread-list-container"]}>
      <h2 className={styles["thread-list-title"]}>Thread List</h2>
      <div className={styles["thread-list-table-wrapper"]}>
      <h3>Sender Threads</h3>
        <table className={styles["thread-list-table"]}>
          <thead>
            <tr>
              <th>Index</th>
              <th>Current Data</th>
              <th>State</th>
              <th>Priority Changeable</th>
              <th>Priority</th>
              <th>Type</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {senderThreads.map((thread) => (
              <tr key={thread.index}>
                <td>{thread.index + 1}</td>
                <td>{thread.currentData}</td>
                <td>{ThreadStateEnum[thread.threadState] || "Unknown"}</td>
                <td>{thread.priorityChangeable ? "Yes" : "No"}</td>
                <td>{thread.priority}</td>
                <td>{ThreadTypeEnum[thread.type]}</td>
                <td className={styles["thread-actions"]}>
                  <button 
                    className={styles["thread-button"]}
                    id="start-button"  
                    onClick={() => handleSenderStart(thread.index)}>
                    Start
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="stop-button" 
                    onClick={() => handleSenderStop(thread.index)}>
                    Stop
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="restart-button"  
                    onClick={() => handleSenderRestart(thread.index)}>
                    Restart
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <h3>Receiver Threads</h3>
        <table className={styles["thread-list-table"]}>
          <thead>
            <tr>
              <th>Index</th>
              <th>Current Data</th>
              <th>State</th>
              <th>Priority Changeable</th>
              <th>Priority</th>
              <th>Type</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {receiverThreads.map((thread) => (
              <tr key={thread.index}>
                <td>{thread.index + 1}</td>
                <td>{thread.currentData}</td>
                <td>{ThreadStateEnum[thread.threadState] || "Unknown"}</td>
                <td>{thread.priorityChangeable ? "Yes" : "No"}</td>
                <td>{thread.priority}</td>
                <td>{ThreadTypeEnum[thread.type]}</td>
                <td className={styles["thread-actions"]}>
                  <button 
                    className={styles["thread-button"]}
                    id="start-button"  
                    onClick={() => handleReceiverStart(thread.index)}>
                    Start
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="stop-button" 
                    onClick={() => handleReceiverStop(thread.index)}>
                    Stop
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="restart-button"  
                    onClick={() => handleReceiverRestart(thread.index)}>
                    Restart
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default ThreadList;
