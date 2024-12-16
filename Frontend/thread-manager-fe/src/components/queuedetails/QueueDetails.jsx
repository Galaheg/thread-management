import React, { useEffect, useState } from "react";
import styles from "./QueueDetails.module.css"; // Styles import et

const QueueDetails = () => {
  const [queue, setQueue] = useState([]);

  useEffect(() => {
    const eventSource = new EventSource("http://localhost:8080/api/threads/queue-stream");

    eventSource.addEventListener("queue-update", (event) => {
      const queueData = JSON.parse(event.data);
      setQueue(queueData);
    });

    eventSource.onerror = () => {
      console.error("SSE bağlantı hatası");
      eventSource.close();
    };

    return () => {
      eventSource.close();
    };
  }, []);

  return (
    <div className={styles["queue-container"]}>
      <h1 className={styles["queue-title"]}>Queue State</h1>
      <ul className={styles["queue-list"]}>
        {queue.length > 0 ? (
          queue.map((item, index) => (
            <li key={index} className={styles["queue-item"]}>
              {item}
            </li>
          ))
        ) : (
          <li className={styles["empty-queue"]}>No items in the queue.</li>
        )}
      </ul>
    </div>
  );
};

export default QueueDetails;
