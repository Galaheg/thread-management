import React, { useEffect, useState } from "react";
import { ThreadStateEnum } from "../utils/enums/ThreadStateEnum";
import { ThreadTypeEnum } from "../utils/enums/ThreadTypeEnum";
import { startThread, stopThread, restartThread } from "../../api/ThreadApi";
import styles from './ThreadDetails.module.css';

const ThreadList = () => {
  const [threads, setThreads] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/api/threads/stream");

    eventSource.addEventListener("threads-update", (event) => {
      const threadData = JSON.parse(event.data);
      setThreads(threadData);
    });

    eventSource.onerror = () => {
      console.error("SSE bağlantı hatası");
      eventSource.close();
    };

    return () => {
      eventSource.close(); // Component unmount edildiğinde bağlantıyı kapat
    };
  }, []);

  const handleStart = async (index) => {
    try {
      const response = await startThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  const handleStop = async (index) => {
    try {
      const response = await stopThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  const handleRestart = async (index) => {
    try {
      const response = await restartThread(index);
      alert(response); 
    } catch (error) {
      alert(error); 
    }
  };

  return (
    <div className={styles["thread-list-container"]}>
      <h2 className={styles["thread-list-title"]}>Thread List</h2>
      <div className={styles["thread-list-table-wrapper"]}>
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
            {threads.map((thread) => (
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
                    onClick={() => handleStart(thread.index)}>
                    Start
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="stop-button" 
                    onClick={() => handleStop(thread.index)}>
                    Stop
                  </button>
                  <button 
                    className={styles["thread-button"]}
                    id="restart-button"  
                    onClick={() => handleRestart(thread.index)}>
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
