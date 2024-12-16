import React, { useEffect, useState } from "react";
import { ThreadStateEnum } from "../utils/enums/ThreadStateEnum";
import { ThreadTypeEnum } from "../utils/enums/ThreadTypeEnum";
import { startThread, stopThread, restartThread } from "../../api/ThreadApi";

const ThreadList = () => {
  const [threads, setThreads] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/api/threads/stream");

    // "threads-update" olayını dinle
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

  // Başlatma işlemi
  const handleStart = async (index) => {
    try {
      const response = await startThread(index);
      alert(response); // Başarı mesajı göster
    } catch (error) {
      alert(error); // Hata mesajı göster
    }
  };

  // Durdurma işlemi
  const handleStop = async (index) => {
    try {
      const response = await stopThread(index);
      alert(response); // Başarı mesajı göster
    } catch (error) {
      alert(error); // Hata mesajı göster
    }
  };

  // Yeniden başlatma işlemi
  const handleRestart = async (index) => {
    try {
      const response = await restartThread(index);
      alert(response); // Başarı mesajı göster
    } catch (error) {
      alert(error); // Hata mesajı göster
    }
  };

  return (
    <div>
      <h1>Thread Status</h1>
      <table>
        <thead>
          <tr>
            <th>Index</th>
            <th>Current Data</th>
            <th>State</th>
            <th>Priority Changeable</th>
            <th>Priority</th>
            <th>Type</th>
            <th>Actions</th> {/* Yeni sütun */}
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
              <td>
                {/* İşlem butonları */}
                <button onClick={() => handleStart(thread.index)}>Start</button>
                <button onClick={() => handleStop(thread.index)}>Stop</button>
                <button onClick={() => handleRestart(thread.index)}>Restart</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ThreadList;
