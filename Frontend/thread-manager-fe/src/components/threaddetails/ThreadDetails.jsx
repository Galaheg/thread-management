import React, { useEffect, useState } from "react";
import { ThreadStateEnum } from "../utils/enums/ThreadStateEnum";
import { ThreadTypeEnum } from "../utils/enums/ThreadTypeEnum"; 

const ThreadList = () => {
  const [threads, setThreads] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/api/threads/stream");

    // "threads-update" dinle
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
          </tr>
        </thead>
        <tbody>
          {threads.map((thread) => (
            <tr key={thread.index}>
              <td>{thread.index}</td>
              <td>{thread.currentData}</td>
              <td>{ThreadStateEnum[thread.threadState] || "Bilinmiyor"}</td>
              <td>{thread.priorityChangeable ? "Yes" : "No"}</td>
              <td>{thread.priority}</td>
              <td>{ThreadTypeEnum[thread.type]}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ThreadList;
